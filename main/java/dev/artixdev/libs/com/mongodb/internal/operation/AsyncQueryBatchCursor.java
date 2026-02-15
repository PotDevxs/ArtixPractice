package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncAggregateResponseBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncConnectionSource;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;

class AsyncQueryBatchCursor<T> implements AsyncAggregateResponseBatchCursor<T> {
   private static final Logger LOGGER = Loggers.getLogger("operation");
   private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
   private static final String CURSOR = "cursor";
   private static final String POST_BATCH_RESUME_TOKEN = "postBatchResumeToken";
   private static final String OPERATION_TIME = "operationTime";
   private final MongoNamespace namespace;
   private final int limit;
   private final Decoder<T> decoder;
   private final long maxTimeMS;
   private volatile AsyncConnectionSource connectionSource;
   private volatile AsyncConnection pinnedConnection;
   private final AtomicReference<ServerCursor> cursor;
   private volatile QueryResult<T> firstBatch;
   private volatile int batchSize;
   private final AtomicInteger count;
   private volatile BsonDocument postBatchResumeToken;
   private final BsonTimestamp operationTime;
   private final BsonValue comment;
   private final boolean firstBatchEmpty;
   private final int maxWireVersion;
   private final Lock lock;
   private boolean isOperationInProgress;
   private boolean isClosed;
   private volatile boolean isClosePending;

   AsyncQueryBatchCursor(QueryResult<T> firstBatch, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, BsonValue comment, AsyncConnectionSource connectionSource, AsyncConnection connection) {
      this(firstBatch, limit, batchSize, maxTimeMS, decoder, comment, connectionSource, connection, (BsonDocument)null);
   }

   AsyncQueryBatchCursor(QueryResult<T> firstBatch, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, BsonValue comment, AsyncConnectionSource connectionSource, @Nullable AsyncConnection connection, @Nullable BsonDocument result) {
      this.count = new AtomicInteger();
      this.lock = new ReentrantLock();
      this.isOperationInProgress = false;
      this.isClosed = false;
      this.isClosePending = false;
      Assertions.isTrueArgument("maxTimeMS >= 0", maxTimeMS >= 0L);
      this.maxTimeMS = maxTimeMS;
      this.namespace = firstBatch.getNamespace();
      this.firstBatch = firstBatch;
      this.limit = limit;
      this.batchSize = batchSize;
      this.decoder = decoder;
      this.comment = comment;
      this.cursor = new AtomicReference(firstBatch.getCursor());
      this.count.addAndGet(firstBatch.getResults().size());
      if (result != null) {
         this.operationTime = result.getTimestamp("operationTime", (BsonTimestamp)null);
         this.postBatchResumeToken = this.getPostBatchResumeTokenFromResponse(result);
      } else {
         this.operationTime = null;
      }

      this.firstBatchEmpty = firstBatch.getResults().isEmpty();
      if (this.cursor.get() != null) {
         this.connectionSource = ((AsyncConnectionSource)Assertions.notNull("connectionSource", connectionSource)).retain();
         Assertions.assertNotNull(connection);
         if (this.limitReached()) {
            this.killCursor(connection);
         } else if (connectionSource.getServerDescription().getType() == ServerType.LOAD_BALANCER) {
            this.pinnedConnection = connection.retain();
            this.pinnedConnection.markAsPinned(Connection.PinningMode.CURSOR);
         }
      }

      this.maxWireVersion = connection == null ? 0 : connection.getDescription().getMaxWireVersion();
      this.logQueryResult(firstBatch);
   }

   public void close() {
      boolean doClose = (Boolean)Locks.withLock(this.lock, () -> {
         if (this.isOperationInProgress) {
            this.isClosePending = true;
            return false;
         } else if (!this.isClosed) {
            this.isClosed = true;
            this.isClosePending = false;
            return true;
         } else {
            return false;
         }
      });
      if (doClose) {
         this.killCursorOnClose();
      }

   }

   public void next(SingleResultCallback<List<T>> callback) {
      if (this.isClosed()) {
         callback.onResult(null, new MongoException("next() called after the cursor was closed."));
      } else if (this.firstBatch != null && !this.firstBatch.getResults().isEmpty()) {
         List<T> results = this.firstBatch.getResults();
         this.firstBatch = null;
         if (this.getServerCursor() == null) {
            this.close();
         }

         callback.onResult(results, (Throwable)null);
      } else {
         ServerCursor localCursor = this.getServerCursor();
         if (localCursor == null) {
            this.close();
            callback.onResult(null, (Throwable)null);
         } else {
            boolean doGetMore = (Boolean)Locks.withLock(this.lock, () -> {
               if (this.isClosed()) {
                  callback.onResult(null, new MongoException("next() called after the cursor was closed."));
                  return false;
               } else {
                  this.isOperationInProgress = true;
                  return true;
               }
            });
            if (doGetMore) {
               this.getMore(localCursor, callback);
            }
         }
      }

   }

   public void setBatchSize(int batchSize) {
      Assertions.assertFalse(this.isClosed());
      this.batchSize = batchSize;
   }

   public int getBatchSize() {
      Assertions.assertFalse(this.isClosed());
      return this.batchSize;
   }

   public boolean isClosed() {
      return (Boolean)Locks.withLock(this.lock, () -> {
         return this.isClosed || this.isClosePending;
      });
   }

   public BsonDocument getPostBatchResumeToken() {
      return this.postBatchResumeToken;
   }

   public BsonTimestamp getOperationTime() {
      return this.operationTime;
   }

   public boolean isFirstBatchEmpty() {
      return this.firstBatchEmpty;
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   private boolean limitReached() {
      return Math.abs(this.limit) != 0 && this.count.get() >= Math.abs(this.limit);
   }

   private void getMore(ServerCursor cursor, SingleResultCallback<List<T>> callback) {
      if (this.pinnedConnection != null) {
         this.getMore(this.pinnedConnection.retain(), cursor, callback);
      } else {
         this.connectionSource.getConnection((connection, t) -> {
            if (t != null) {
               this.endOperationInProgress();
               callback.onResult(null, t);
            } else {
               this.getMore((AsyncConnection)Assertions.assertNotNull(connection), cursor, callback);
            }

         });
      }

   }

   private void getMore(AsyncConnection connection, ServerCursor cursor, SingleResultCallback<List<T>> callback) {
      connection.commandAsync(this.namespace.getDatabaseName(), this.asGetMoreCommandDocument(cursor.getId(), connection.getDescription()), NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), CommandResultDocumentCodec.create(this.decoder, "nextBatch"), this.connectionSource, new AsyncQueryBatchCursor.CommandResultSingleResultCallback(connection, cursor, callback));
   }

   private BsonDocument asGetMoreCommandDocument(long cursorId, ConnectionDescription connectionDescription) {
      BsonDocument document = (new BsonDocument("getMore", new BsonInt64(cursorId))).append("collection", new BsonString(this.namespace.getCollectionName()));
      int batchSizeForGetMoreCommand = Math.abs(CursorHelper.getNumberToReturn(this.limit, this.batchSize, this.count.get()));
      if (batchSizeForGetMoreCommand != 0) {
         document.append("batchSize", new BsonInt32(batchSizeForGetMoreCommand));
      }

      if (this.maxTimeMS != 0L) {
         document.append("maxTimeMS", new BsonInt64(this.maxTimeMS));
      }

      if (ServerVersionHelper.serverIsAtLeastVersionFourDotFour(connectionDescription)) {
         DocumentHelper.putIfNotNull(document, "comment", this.comment);
      }

      return document;
   }

   private void killCursorOnClose() {
      ServerCursor localCursor = this.getServerCursor();
      if (localCursor != null) {
         if (this.pinnedConnection != null) {
            this.killCursorAsynchronouslyAndReleaseConnectionAndSource(this.pinnedConnection, localCursor);
         } else {
            this.connectionSource.getConnection((connection, t) -> {
               if (t != null) {
                  this.connectionSource.release();
               } else {
                  this.killCursorAsynchronouslyAndReleaseConnectionAndSource((AsyncConnection)Assertions.assertNotNull(connection), localCursor);
               }

            });
         }
      } else if (this.pinnedConnection != null) {
         this.pinnedConnection.release();
      }

   }

   private void killCursor(AsyncConnection connection) {
      ServerCursor localCursor = this.cursor.getAndSet(null);
      if (localCursor != null) {
         this.killCursorAsynchronouslyAndReleaseConnectionAndSource(connection.retain(), localCursor);
      } else {
         this.connectionSource.release();
      }

   }

   private void killCursorAsynchronouslyAndReleaseConnectionAndSource(AsyncConnection connection, ServerCursor localCursor) {
      connection.commandAsync(this.namespace.getDatabaseName(), this.asKillCursorsCommandDocument(localCursor), NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), new BsonDocumentCodec(), this.connectionSource, (result, t) -> {
         connection.release();
         this.connectionSource.release();
      });
   }

   private BsonDocument asKillCursorsCommandDocument(ServerCursor localCursor) {
      return (new BsonDocument("killCursors", new BsonString(this.namespace.getCollectionName()))).append("cursors", new BsonArray(Collections.singletonList(new BsonInt64(localCursor.getId()))));
   }

   private void endOperationInProgress() {
      boolean closePending = (Boolean)Locks.withLock(this.lock, () -> {
         this.isOperationInProgress = false;
         return this.isClosePending;
      });
      if (closePending) {
         this.close();
      }

   }

   private void handleGetMoreQueryResult(AsyncConnection connection, SingleResultCallback<List<T>> callback, QueryResult<T> result) {
      this.logQueryResult(result);
      this.cursor.set(result.getCursor());
      if (this.isClosePending) {
         try {
            connection.release();
            if (result.getCursor() == null) {
               this.connectionSource.release();
            }

            this.endOperationInProgress();
         } finally {
            callback.onResult(null, (Throwable)null);
         }
      } else if (result.getResults().isEmpty() && result.getCursor() != null) {
         this.getMore(connection, (ServerCursor)Assertions.assertNotNull(result.getCursor()), callback);
      } else {
         this.count.addAndGet(result.getResults().size());
         if (this.limitReached()) {
            this.killCursor(connection);
            connection.release();
         } else {
            connection.release();
            if (result.getCursor() == null) {
               this.connectionSource.release();
            }
         }

         this.endOperationInProgress();
         if (result.getResults().isEmpty()) {
            callback.onResult(null, (Throwable)null);
         } else {
            callback.onResult(result.getResults(), (Throwable)null);
         }
      }

   }

   private void logQueryResult(QueryResult<T> result) {
      LOGGER.debug(String.format("Received batch of %d documents with cursorId %d from server %s", result.getResults().size(), result.getCursorId(), result.getAddress()));
   }

   @Nullable
   ServerCursor getServerCursor() {
      return (ServerCursor)this.cursor.get();
   }

   @Nullable
   private BsonDocument getPostBatchResumeTokenFromResponse(BsonDocument result) {
      BsonDocument cursor = result.getDocument("cursor", (BsonDocument)null);
      return cursor != null ? cursor.getDocument("postBatchResumeToken", (BsonDocument)null) : null;
   }

   private class CommandResultSingleResultCallback implements SingleResultCallback<BsonDocument> {
      private final AsyncConnection connection;
      private final ServerCursor cursor;
      private final SingleResultCallback<List<T>> callback;

      CommandResultSingleResultCallback(AsyncConnection connection, ServerCursor cursor, SingleResultCallback<List<T>> callback) {
         this.connection = connection;
         this.cursor = cursor;
         this.callback = ErrorHandlingResultCallback.errorHandlingCallback(callback, AsyncQueryBatchCursor.LOGGER);
      }

      public void onResult(@Nullable BsonDocument result, @Nullable Throwable t) {
         if (t != null) {
            Throwable translatedException = t instanceof MongoCommandException ? QueryHelper.translateCommandException((MongoCommandException)t, this.cursor) : t;
            this.connection.release();
            AsyncQueryBatchCursor.this.endOperationInProgress();
            this.callback.onResult(null, (Throwable)translatedException);
         } else {
            Assertions.assertNotNull(result);
            QueryResult<T> queryResult = SyncOperationHelper.getMoreCursorDocumentToQueryResult(result.getDocument("cursor"), this.connection.getDescription().getServerAddress());
            AsyncQueryBatchCursor.this.postBatchResumeToken = AsyncQueryBatchCursor.this.getPostBatchResumeTokenFromResponse(result);
            AsyncQueryBatchCursor.this.handleGetMoreQueryResult(this.connection, this.callback, queryResult);
         }

      }
   }
}
