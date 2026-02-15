package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.binding.ConnectionSource;
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

class QueryBatchCursor<T> implements AggregateResponseBatchCursor<T> {
   private static final Logger LOGGER = Loggers.getLogger("operation");
   private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
   private static final String CURSOR = "cursor";
   private static final String POST_BATCH_RESUME_TOKEN = "postBatchResumeToken";
   private static final String OPERATION_TIME = "operationTime";
   private static final String MESSAGE_IF_CLOSED_AS_CURSOR = "Cursor has been closed";
   private static final String MESSAGE_IF_CLOSED_AS_ITERATOR = "Iterator has been closed";
   private final MongoNamespace namespace;
   private final ServerAddress serverAddress;
   private final int limit;
   private final Decoder<T> decoder;
   private final long maxTimeMS;
   private int batchSize;
   private final BsonValue comment;
   private List<T> nextBatch;
   private int count;
   private BsonDocument postBatchResumeToken;
   private BsonTimestamp operationTime;
   private final boolean firstBatchEmpty;
   private int maxWireVersion;
   private final QueryBatchCursor<T>.ResourceManager resourceManager;

   QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, Decoder<T> decoder) {
      this(firstQueryResult, limit, batchSize, decoder, (BsonValue)null, (ConnectionSource)null);
   }

   QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, Decoder<T> decoder, @Nullable BsonValue comment, @Nullable ConnectionSource connectionSource) {
      this(firstQueryResult, limit, batchSize, 0L, decoder, comment, connectionSource, (Connection)null, (BsonDocument)null);
   }

   QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, @Nullable BsonValue comment, @Nullable ConnectionSource connectionSource, @Nullable Connection connection) {
      this(firstQueryResult, limit, batchSize, maxTimeMS, decoder, comment, connectionSource, connection, (BsonDocument)null);
   }

   QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, @Nullable BsonValue comment, @Nullable ConnectionSource connectionSource, @Nullable Connection connection, @Nullable BsonDocument result) {
      this.maxWireVersion = 0;
      Assertions.isTrueArgument("maxTimeMS >= 0", maxTimeMS >= 0L);
      this.maxTimeMS = maxTimeMS;
      this.namespace = firstQueryResult.getNamespace();
      this.serverAddress = firstQueryResult.getAddress();
      this.limit = limit;
      this.comment = comment;
      this.batchSize = batchSize;
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
      if (result != null) {
         this.operationTime = result.getTimestamp("operationTime", (BsonTimestamp)null);
         this.postBatchResumeToken = this.getPostBatchResumeTokenFromResponse(result);
      }

      ServerCursor serverCursor = this.initFromQueryResult(firstQueryResult);
      if (serverCursor != null) {
         Assertions.notNull("connectionSource", connectionSource);
      }

      this.firstBatchEmpty = firstQueryResult.getResults().isEmpty();
      Connection connectionToPin = null;
      boolean releaseServerAndResources = false;
      if (connection != null) {
         this.maxWireVersion = connection.getDescription().getMaxWireVersion();
         if (this.limitReached()) {
            releaseServerAndResources = true;
         } else {
            Assertions.assertNotNull(connectionSource);
            if (connectionSource.getServerDescription().getType() == ServerType.LOAD_BALANCER) {
               connectionToPin = connection;
            }
         }
      }

      this.resourceManager = new QueryBatchCursor.ResourceManager(connectionSource, connectionToPin, serverCursor);
      if (releaseServerAndResources) {
         this.resourceManager.releaseServerAndClientResources((Connection)Assertions.assertNotNull(connection));
      }

   }

   public boolean hasNext() {
      return (Boolean)Assertions.assertNotNull((Boolean)this.resourceManager.execute("Cursor has been closed", this::doHasNext));
   }

   private boolean doHasNext() {
      if (this.nextBatch != null) {
         return true;
      } else if (this.limitReached()) {
         return false;
      } else {
         do {
            if (this.resourceManager.serverCursor() == null) {
               return false;
            }

            this.getMore();
            if (!this.resourceManager.operable()) {
               throw new IllegalStateException("Cursor has been closed");
            }
         } while(this.nextBatch == null);

         return true;
      }
   }

   public List<T> next() {
      return (List)Assertions.assertNotNull((List)this.resourceManager.execute("Iterator has been closed", this::doNext));
   }

   public int available() {
      return this.resourceManager.operable() && this.nextBatch != null ? this.nextBatch.size() : 0;
   }

   private List<T> doNext() {
      if (!this.doHasNext()) {
         throw new NoSuchElementException();
      } else {
         List<T> retVal = this.nextBatch;
         this.nextBatch = null;
         return retVal;
      }
   }

   public void setBatchSize(int batchSize) {
      this.batchSize = batchSize;
   }

   public int getBatchSize() {
      return this.batchSize;
   }

   public void remove() {
      throw new UnsupportedOperationException("Not implemented yet!");
   }

   public void close() {
      this.resourceManager.close();
   }

   @Nullable
   public List<T> tryNext() {
      return (List)this.resourceManager.execute("Cursor has been closed", () -> {
         return !this.tryHasNext() ? null : this.doNext();
      });
   }

   private boolean tryHasNext() {
      if (this.nextBatch != null) {
         return true;
      } else if (this.limitReached()) {
         return false;
      } else {
         if (this.resourceManager.serverCursor() != null) {
            this.getMore();
         }

         return this.nextBatch != null;
      }
   }

   @Nullable
   public ServerCursor getServerCursor() {
      if (!this.resourceManager.operable()) {
         throw new IllegalStateException("Iterator has been closed");
      } else {
         return this.resourceManager.serverCursor();
      }
   }

   public ServerAddress getServerAddress() {
      if (!this.resourceManager.operable()) {
         throw new IllegalStateException("Iterator has been closed");
      } else {
         return this.serverAddress;
      }
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

   private void getMore() {
      ServerCursor serverCursor = (ServerCursor)Assertions.assertNotNull(this.resourceManager.serverCursor());
      this.resourceManager.executeWithConnection((connection) -> {
         ServerCursor nextServerCursor;
         try {
            nextServerCursor = this.initFromCommandResult((BsonDocument)connection.command(this.namespace.getDatabaseName(), this.asGetMoreCommandDocument(serverCursor.getId(), connection.getDescription()), NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), CommandResultDocumentCodec.create(this.decoder, "nextBatch"), (BindingContext)Assertions.assertNotNull(this.resourceManager.connectionSource)));
         } catch (MongoCommandException e) {
            throw QueryHelper.translateCommandException(e, serverCursor);
         }

         this.resourceManager.setServerCursor(nextServerCursor);
         if (this.limitReached()) {
            this.resourceManager.releaseServerAndClientResources(connection);
         }

      });
   }

   private BsonDocument asGetMoreCommandDocument(long cursorId, ConnectionDescription connectionDescription) {
      BsonDocument document = (new BsonDocument("getMore", new BsonInt64(cursorId))).append("collection", new BsonString(this.namespace.getCollectionName()));
      int batchSizeForGetMoreCommand = Math.abs(CursorHelper.getNumberToReturn(this.limit, this.batchSize, this.count));
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

   @Nullable
   private ServerCursor initFromQueryResult(QueryResult<T> queryResult) {
      this.nextBatch = queryResult.getResults().isEmpty() ? null : queryResult.getResults();
      this.count += queryResult.getResults().size();
      LOGGER.debug(String.format("Received batch of %d documents with cursorId %d from server %s", queryResult.getResults().size(), queryResult.getCursorId(), queryResult.getAddress()));
      return queryResult.getCursor();
   }

   @Nullable
   private ServerCursor initFromCommandResult(BsonDocument getMoreCommandResultDocument) {
      QueryResult<T> queryResult = SyncOperationHelper.getMoreCursorDocumentToQueryResult(getMoreCommandResultDocument.getDocument("cursor"), this.serverAddress);
      this.postBatchResumeToken = this.getPostBatchResumeTokenFromResponse(getMoreCommandResultDocument);
      this.operationTime = getMoreCommandResultDocument.getTimestamp("operationTime", (BsonTimestamp)null);
      return this.initFromQueryResult(queryResult);
   }

   private boolean limitReached() {
      return Math.abs(this.limit) != 0 && this.count >= Math.abs(this.limit);
   }

   @Nullable
   private BsonDocument getPostBatchResumeTokenFromResponse(BsonDocument result) {
      BsonDocument cursor = result.getDocument("cursor", (BsonDocument)null);
      return cursor != null ? cursor.getDocument("postBatchResumeToken", (BsonDocument)null) : null;
   }

   @ThreadSafe
   private final class ResourceManager {
      private final Lock lock = (new StampedLock()).asWriteLock();
      private volatile QueryBatchCursor.State state;
      @Nullable
      private volatile ConnectionSource connectionSource;
      @Nullable
      private volatile Connection pinnedConnection;
      @Nullable
      private volatile ServerCursor serverCursor;
      private volatile boolean skipReleasingServerResourcesOnClose;

      ResourceManager(@Nullable ConnectionSource connectionSource, @Nullable Connection connectionToPin, @Nullable ServerCursor serverCursor) {
         this.state = QueryBatchCursor.State.IDLE;
         if (serverCursor != null) {
            this.connectionSource = ((ConnectionSource)Assertions.assertNotNull(connectionSource)).retain();
            if (connectionToPin != null) {
               this.pinnedConnection = connectionToPin.retain();
               connectionToPin.markAsPinned(Connection.PinningMode.CURSOR);
            }
         }

         this.skipReleasingServerResourcesOnClose = false;
         this.serverCursor = serverCursor;
      }

      boolean operable() {
         return this.state.operable();
      }

      @Nullable
      <R> R execute(String exceptionMessageIfClosed, Supplier<R> operation) throws IllegalStateException {
         if (!this.tryStartOperation()) {
            throw new IllegalStateException(exceptionMessageIfClosed);
         } else {
            R result;
            try {
               result = operation.get();
            } finally {
               this.endOperation();
            }

            return result;
         }
      }

      private boolean tryStartOperation() throws IllegalStateException {
         return (Boolean)Locks.withLock(this.lock, () -> {
            QueryBatchCursor.State localState = this.state;
            if (!localState.operable()) {
               return false;
            } else if (localState == QueryBatchCursor.State.IDLE) {
               this.state = QueryBatchCursor.State.OPERATION_IN_PROGRESS;
               return true;
            } else if (localState == QueryBatchCursor.State.OPERATION_IN_PROGRESS) {
               throw new IllegalStateException("Another operation is currently in progress, concurrent operations are not supported");
            } else {
               throw Assertions.fail(this.state.toString());
            }
         });
      }

      private void endOperation() {
         boolean doClose = (Boolean)Locks.withLock(this.lock, () -> {
            QueryBatchCursor.State localState = this.state;
            if (localState == QueryBatchCursor.State.OPERATION_IN_PROGRESS) {
               this.state = QueryBatchCursor.State.IDLE;
               return false;
            } else if (localState == QueryBatchCursor.State.CLOSE_PENDING) {
               this.state = QueryBatchCursor.State.CLOSED;
               return true;
            } else {
               throw Assertions.fail(localState.toString());
            }
         });
         if (doClose) {
            this.doClose();
         }

      }

      void close() {
         boolean doClose = (Boolean)Locks.withLock(this.lock, () -> {
            QueryBatchCursor.State localState = this.state;
            if (localState == QueryBatchCursor.State.OPERATION_IN_PROGRESS) {
               this.state = QueryBatchCursor.State.CLOSE_PENDING;
               return false;
            } else if (localState != QueryBatchCursor.State.CLOSED) {
               this.state = QueryBatchCursor.State.CLOSED;
               return true;
            } else {
               return false;
            }
         });
         if (doClose) {
            this.doClose();
         }

      }

      private void doClose() {
         try {
            if (this.skipReleasingServerResourcesOnClose) {
               this.serverCursor = null;
            } else if (this.serverCursor != null) {
               Connection connection = this.connection();

               try {
                  this.releaseServerResources(connection);
               } finally {
                  connection.release();
               }
            }
         } catch (MongoException ignored) {
         } finally {
            this.serverCursor = null;
            this.releaseClientResources();
         }

      }

      void onCorruptedConnection(Connection corruptedConnection) {
         Assertions.assertTrue(this.state.inProgress());
         Connection localPinnedConnection = this.pinnedConnection;
         if (localPinnedConnection != null) {
            Assertions.assertTrue(corruptedConnection == localPinnedConnection);
            this.skipReleasingServerResourcesOnClose = true;
         }

      }

      void executeWithConnection(Consumer<Connection> action) {
         Connection connection = this.connection();

         try {
            action.accept(connection);
         } catch (MongoSocketException e) {
            try {
               this.onCorruptedConnection(connection);
            } catch (Exception suppressed) {
               e.addSuppressed(suppressed);
            }

            throw e;
         } finally {
            connection.release();
         }

      }

      private Connection connection() {
         Assertions.assertTrue(this.state != QueryBatchCursor.State.IDLE);
         return this.pinnedConnection == null ? ((ConnectionSource)Assertions.assertNotNull(this.connectionSource)).getConnection() : ((Connection)Assertions.assertNotNull(this.pinnedConnection)).retain();
      }

      @Nullable
      ServerCursor serverCursor() {
         return this.serverCursor;
      }

      void setServerCursor(@Nullable ServerCursor serverCursor) {
         Assertions.assertTrue(this.state.inProgress());
         Assertions.assertNotNull(this.serverCursor);
         Assertions.assertNotNull(this.connectionSource);
         this.serverCursor = serverCursor;
         if (serverCursor == null) {
            this.releaseClientResources();
         }

      }

      void releaseServerAndClientResources(Connection connection) {
         try {
            this.releaseServerResources((Connection)Assertions.assertNotNull(connection));
         } finally {
            this.releaseClientResources();
         }

      }

      private void releaseServerResources(Connection connection) {
         try {
            ServerCursor localServerCursor = this.serverCursor;
            if (localServerCursor != null) {
               this.killServerCursor(QueryBatchCursor.this.namespace, localServerCursor, (Connection)Assertions.assertNotNull(connection));
            }
         } finally {
            this.serverCursor = null;
         }

      }

      private void killServerCursor(MongoNamespace namespace, ServerCursor serverCursor, Connection connection) {
         connection.command(namespace.getDatabaseName(), this.asKillCursorsCommandDocument(namespace, serverCursor), QueryBatchCursor.NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), new BsonDocumentCodec(), (BindingContext)Assertions.assertNotNull(this.connectionSource));
      }

      private BsonDocument asKillCursorsCommandDocument(MongoNamespace namespace, ServerCursor serverCursor) {
         return (new BsonDocument("killCursors", new BsonString(namespace.getCollectionName()))).append("cursors", new BsonArray(Collections.singletonList(new BsonInt64(serverCursor.getId()))));
      }

      private void releaseClientResources() {
         Assertions.assertNull(this.serverCursor);
         ConnectionSource localConnectionSource = this.connectionSource;
         if (localConnectionSource != null) {
            localConnectionSource.release();
            this.connectionSource = null;
         }

         Connection localPinnedConnection = this.pinnedConnection;
         if (localPinnedConnection != null) {
            localPinnedConnection.release();
            this.pinnedConnection = null;
         }

      }
   }

   private static enum State {
      IDLE(true, false),
      OPERATION_IN_PROGRESS(true, true),
      CLOSE_PENDING(false, true),
      CLOSED(false, false);

      private final boolean operable;
      private final boolean inProgress;

      private State(boolean operable, boolean inProgress) {
         this.operable = operable;
         this.inProgress = inProgress;
      }

      boolean operable() {
         return this.operable;
      }

      boolean inProgress() {
         return this.inProgress;
      }

      // $FF: synthetic method
      private static QueryBatchCursor.State[] $values() {
         return new QueryBatchCursor.State[]{IDLE, OPERATION_IN_PROGRESS, CLOSE_PENDING, CLOSED};
      }
   }
}
