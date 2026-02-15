package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

final class ListSearchIndexesOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>> {
   private static final String STAGE_LIST_SEARCH_INDEXES = "$listSearchIndexes";
   private final MongoNamespace namespace;
   private final Decoder<T> decoder;
   @Nullable
   private final Boolean allowDiskUse;
   @Nullable
   private final Integer batchSize;
   @Nullable
   private final Collation collation;
   @Nullable
   private final BsonValue comment;
   private final long maxTimeMS;
   @Nullable
   private final String indexName;
   private final boolean retryReads;

   ListSearchIndexesOperation(MongoNamespace namespace, Decoder<T> decoder, long maxTimeMS, @Nullable String indexName, @Nullable Integer batchSize, @Nullable Collation collation, @Nullable BsonValue comment, @Nullable Boolean allowDiskUse, boolean retryReads) {
      this.namespace = namespace;
      this.decoder = decoder;
      this.allowDiskUse = allowDiskUse;
      this.batchSize = batchSize;
      this.collation = collation;
      this.maxTimeMS = maxTimeMS;
      this.comment = comment;
      this.indexName = indexName;
      this.retryReads = retryReads;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      try {
         return this.asAggregateOperation().execute(binding);
      } catch (MongoCommandException e) {
         int cursorBatchSize = this.batchSize == null ? 0 : this.batchSize;
         if (!CommandOperationHelper.isNamespaceError(e)) {
            throw e;
         } else {
            return OperationHelper.createEmptyBatchCursor(this.namespace, this.decoder, e.getServerAddress(), cursorBatchSize);
         }
      }
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      this.asAggregateOperation().executeAsync(binding, (cursor, exception) -> {
         if (exception != null && !CommandOperationHelper.isNamespaceError(exception)) {
            callback.onResult(null, exception);
         } else if (exception != null) {
            MongoCommandException commandException = (MongoCommandException)exception;
            AsyncBatchCursor<T> emptyAsyncBatchCursor = AsyncOperationHelper.createEmptyAsyncBatchCursor(this.namespace, commandException.getServerAddress());
            callback.onResult(emptyAsyncBatchCursor, (Throwable)null);
         } else {
            callback.onResult(cursor, (Throwable)null);
         }

      });
   }

   public <R> ReadOperation<R> asExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return this.asAggregateOperation().asExplainableOperation(verbosity, resultDecoder);
   }

   public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return this.asAggregateOperation().asAsyncExplainableOperation(verbosity, resultDecoder);
   }

   private AggregateOperation<T> asAggregateOperation() {
      BsonDocument searchDefinition = this.getSearchDefinition();
      BsonDocument listSearchIndexesStage = new BsonDocument("$listSearchIndexes", searchDefinition);
      return (new AggregateOperation(this.namespace, Collections.singletonList(listSearchIndexesStage), this.decoder)).retryReads(this.retryReads).collation(this.collation).comment(this.comment).allowDiskUse(this.allowDiskUse).batchSize(this.batchSize).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   @NonNull
   private BsonDocument getSearchDefinition() {
      return this.indexName == null ? new BsonDocument() : new BsonDocument("name", new BsonString(this.indexName));
   }
}
