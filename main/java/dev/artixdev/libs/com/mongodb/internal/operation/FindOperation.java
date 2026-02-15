package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.CursorType;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.MongoQueryException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.function.AsyncCallbackSupplier;
import dev.artixdev.libs.com.mongodb.internal.async.function.RetryState;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.NoOpSessionContext;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class FindOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>> {
   private static final String FIRST_BATCH = "firstBatch";
   private final MongoNamespace namespace;
   private final Decoder<T> decoder;
   private boolean retryReads;
   private BsonDocument filter;
   private int batchSize;
   private int limit;
   private BsonDocument projection;
   private long maxTimeMS;
   private long maxAwaitTimeMS;
   private int skip;
   private BsonDocument sort;
   private CursorType cursorType;
   private boolean oplogReplay;
   private boolean noCursorTimeout;
   private boolean partial;
   private Collation collation;
   private BsonValue comment;
   private BsonValue hint;
   private BsonDocument variables;
   private BsonDocument max;
   private BsonDocument min;
   private boolean returnKey;
   private boolean showRecordId;
   private Boolean allowDiskUse;

   public FindOperation(MongoNamespace namespace, Decoder<T> decoder) {
      this.cursorType = CursorType.NonTailable;
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public Decoder<T> getDecoder() {
      return this.decoder;
   }

   public BsonDocument getFilter() {
      return this.filter;
   }

   public FindOperation<T> filter(@Nullable BsonDocument filter) {
      this.filter = filter;
      return this;
   }

   public int getBatchSize() {
      return this.batchSize;
   }

   public FindOperation<T> batchSize(int batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   public int getLimit() {
      return this.limit;
   }

   public FindOperation<T> limit(int limit) {
      this.limit = limit;
      return this;
   }

   public BsonDocument getProjection() {
      return this.projection;
   }

   public FindOperation<T> projection(@Nullable BsonDocument projection) {
      this.projection = projection;
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   public FindOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public long getMaxAwaitTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
   }

   public FindOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
      this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
      return this;
   }

   public int getSkip() {
      return this.skip;
   }

   public FindOperation<T> skip(int skip) {
      this.skip = skip;
      return this;
   }

   public BsonDocument getSort() {
      return this.sort;
   }

   public FindOperation<T> sort(@Nullable BsonDocument sort) {
      this.sort = sort;
      return this;
   }

   public CursorType getCursorType() {
      return this.cursorType;
   }

   public FindOperation<T> cursorType(CursorType cursorType) {
      this.cursorType = (CursorType)Assertions.notNull("cursorType", cursorType);
      return this;
   }

   public boolean isOplogReplay() {
      return this.oplogReplay;
   }

   public FindOperation<T> oplogReplay(boolean oplogReplay) {
      this.oplogReplay = oplogReplay;
      return this;
   }

   public boolean isNoCursorTimeout() {
      return this.noCursorTimeout;
   }

   public FindOperation<T> noCursorTimeout(boolean noCursorTimeout) {
      this.noCursorTimeout = noCursorTimeout;
      return this;
   }

   public boolean isPartial() {
      return this.partial;
   }

   public FindOperation<T> partial(boolean partial) {
      this.partial = partial;
      return this;
   }

   public Collation getCollation() {
      return this.collation;
   }

   public FindOperation<T> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public BsonValue getComment() {
      return this.comment;
   }

   public FindOperation<T> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public BsonValue getHint() {
      return this.hint;
   }

   public FindOperation<T> hint(@Nullable BsonValue hint) {
      this.hint = hint;
      return this;
   }

   public BsonDocument getLet() {
      return this.variables;
   }

   public FindOperation<T> let(@Nullable BsonDocument variables) {
      this.variables = variables;
      return this;
   }

   public BsonDocument getMax() {
      return this.max;
   }

   public FindOperation<T> max(@Nullable BsonDocument max) {
      this.max = max;
      return this;
   }

   public BsonDocument getMin() {
      return this.min;
   }

   public FindOperation<T> min(@Nullable BsonDocument min) {
      this.min = min;
      return this;
   }

   public boolean isReturnKey() {
      return this.returnKey;
   }

   public FindOperation<T> returnKey(boolean returnKey) {
      this.returnKey = returnKey;
      return this;
   }

   public boolean isShowRecordId() {
      return this.showRecordId;
   }

   public FindOperation<T> showRecordId(boolean showRecordId) {
      this.showRecordId = showRecordId;
      return this;
   }

   public FindOperation<T> retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   public boolean getRetryReads() {
      return this.retryReads;
   }

   public Boolean isAllowDiskUse() {
      return this.allowDiskUse;
   }

   public FindOperation<T> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.allowDiskUse = allowDiskUse;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
      Supplier<BatchCursor<T>> read = SyncOperationHelper.decorateReadWithRetries(retryState, binding.getOperationContext(), () -> {
         Objects.requireNonNull(binding);
         return (BatchCursor)SyncOperationHelper.withSourceAndConnection(binding::getReadConnectionSource, false, (source, connection) -> {
            retryState.breakAndThrowIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            });

            try {
               return (QueryBatchCursor)SyncOperationHelper.createReadCommandAndExecute(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.transformer(), connection);
            } catch (MongoCommandException e) {
               throw new MongoQueryException(e.getResponse(), e.getServerAddress());
            }
         });
      });
      return (BatchCursor)read.get();
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      RetryState retryState = CommandOperationHelper.initialRetryState(this.retryReads);
      binding.retain();
      AsyncCallbackSupplier<AsyncBatchCursor<T>> var10000 = AsyncOperationHelper.decorateReadWithRetriesAsync(retryState, binding.getOperationContext(), (funcCallback) -> {
         Objects.requireNonNull(binding);
         AsyncOperationHelper.withAsyncSourceAndConnection(binding::getReadConnectionSource, false, funcCallback, (source, connection, releasingCallback) -> {
            if (!retryState.breakAndCompleteIfRetryAnd(() -> {
               return !OperationHelper.canRetryRead(source.getServerDescription(), binding.getSessionContext());
            }, releasingCallback)) {
               SingleResultCallback<AsyncBatchCursor<T>> wrappedCallback = exceptionTransformingCallback(releasingCallback);
               AsyncOperationHelper.createReadCommandAndExecuteAsync(retryState, binding, source, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.asyncTransformer(), connection, wrappedCallback);
            }
         });
      });
      Objects.requireNonNull(binding);
      AsyncCallbackSupplier<AsyncBatchCursor<T>> asyncRead = var10000.whenComplete(binding::release);
      asyncRead.get(ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
   }

   private static <T> SingleResultCallback<T> exceptionTransformingCallback(SingleResultCallback<T> callback) {
      return (result, t) -> {
         if (t != null) {
            if (t instanceof MongoCommandException) {
               MongoCommandException commandException = (MongoCommandException)t;
               callback.onResult(result, new MongoQueryException(commandException.getResponse(), commandException.getServerAddress()));
            } else {
               callback.onResult(result, t);
            }
         } else {
            callback.onResult(result, (Throwable)null);
         }

      };
   }

   public <R> ReadOperation<R> asExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return new CommandReadOperation(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
   }

   public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return new CommandReadOperation(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
   }

   private BsonDocument getCommand(SessionContext sessionContext, int maxWireVersion) {
      BsonDocument commandDocument = new BsonDocument("find", new BsonString(this.namespace.getCollectionName()));
      OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, maxWireVersion, commandDocument);
      DocumentHelper.putIfNotNull(commandDocument, "filter", (BsonValue)this.filter);
      DocumentHelper.putIfNotNullOrEmpty(commandDocument, "sort", this.sort);
      DocumentHelper.putIfNotNullOrEmpty(commandDocument, "projection", this.projection);
      if (this.skip > 0) {
         commandDocument.put((String)"skip", (BsonValue)(new BsonInt32(this.skip)));
      }

      if (this.limit != 0) {
         commandDocument.put((String)"limit", (BsonValue)(new BsonInt32(Math.abs(this.limit))));
      }

      if (this.limit >= 0) {
         if (this.batchSize < 0 && Math.abs(this.batchSize) < this.limit) {
            commandDocument.put((String)"limit", (BsonValue)(new BsonInt32(Math.abs(this.batchSize))));
         } else if (this.batchSize != 0) {
            commandDocument.put((String)"batchSize", (BsonValue)(new BsonInt32(Math.abs(this.batchSize))));
         }
      }

      if (this.limit < 0 || this.batchSize < 0) {
         commandDocument.put((String)"singleBatch", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.maxTimeMS > 0L) {
         commandDocument.put((String)"maxTimeMS", (BsonValue)(new BsonInt64(this.maxTimeMS)));
      }

      if (this.isTailableCursor()) {
         commandDocument.put((String)"tailable", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.isAwaitData()) {
         commandDocument.put((String)"awaitData", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.oplogReplay) {
         commandDocument.put((String)"oplogReplay", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.noCursorTimeout) {
         commandDocument.put((String)"noCursorTimeout", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.partial) {
         commandDocument.put((String)"allowPartialResults", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.collation != null) {
         commandDocument.put((String)"collation", (BsonValue)this.collation.asDocument());
      }

      if (this.comment != null) {
         commandDocument.put("comment", this.comment);
      }

      if (this.hint != null) {
         commandDocument.put("hint", this.hint);
      }

      if (this.variables != null) {
         commandDocument.put((String)"let", (BsonValue)this.variables);
      }

      if (this.max != null) {
         commandDocument.put((String)"max", (BsonValue)this.max);
      }

      if (this.min != null) {
         commandDocument.put((String)"min", (BsonValue)this.min);
      }

      if (this.returnKey) {
         commandDocument.put((String)"returnKey", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.showRecordId) {
         commandDocument.put((String)"showRecordId", (BsonValue)BsonBoolean.TRUE);
      }

      if (this.allowDiskUse != null) {
         commandDocument.put((String)"allowDiskUse", (BsonValue)BsonBoolean.valueOf(this.allowDiskUse));
      }

      return commandDocument;
   }

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand(sessionContext, connectionDescription.getMaxWireVersion());
      };
   }

   private boolean isTailableCursor() {
      return this.cursorType.isTailable();
   }

   private boolean isAwaitData() {
      return this.cursorType == CursorType.TailableAwait;
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, QueryBatchCursor<T>> transformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = OperationHelper.cursorDocumentToQueryResult(result.getDocument("cursor"), connection.getDescription().getServerAddress());
         return new QueryBatchCursor(queryResult, this.limit, this.batchSize, this.getMaxTimeForCursor(), this.decoder, this.comment, source, connection, result);
      };
   }

   private long getMaxTimeForCursor() {
      return this.cursorType == CursorType.TailableAwait ? this.maxAwaitTimeMS : 0L;
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = OperationHelper.cursorDocumentToQueryResult(result.getDocument("cursor"), connection.getDescription().getServerAddress());
         return new AsyncQueryBatchCursor(queryResult, this.limit, this.batchSize, this.getMaxTimeForCursor(), this.decoder, this.comment, source, connection, result);
      };
   }
}
