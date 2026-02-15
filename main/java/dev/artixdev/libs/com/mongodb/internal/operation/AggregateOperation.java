package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.connection.NoOpSessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public class AggregateOperation<T> implements AsyncExplainableReadOperation<AsyncBatchCursor<T>>, ExplainableReadOperation<BatchCursor<T>> {
   private final AggregateOperationImpl<T> wrapped;

   public AggregateOperation(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder) {
      this(namespace, pipeline, decoder, AggregationLevel.COLLECTION);
   }

   public AggregateOperation(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregationLevel aggregationLevel) {
      this.wrapped = new AggregateOperationImpl(namespace, pipeline, decoder, aggregationLevel);
   }

   public List<BsonDocument> getPipeline() {
      return this.wrapped.getPipeline();
   }

   public Boolean getAllowDiskUse() {
      return this.wrapped.getAllowDiskUse();
   }

   public AggregateOperation<T> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.wrapped.allowDiskUse(allowDiskUse);
      return this;
   }

   public Integer getBatchSize() {
      return this.wrapped.getBatchSize();
   }

   public AggregateOperation<T> batchSize(@Nullable Integer batchSize) {
      this.wrapped.batchSize(batchSize);
      return this;
   }

   public long getMaxAwaitTime(TimeUnit timeUnit) {
      return this.wrapped.getMaxAwaitTime(timeUnit);
   }

   public AggregateOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
      return this;
   }

   public long getMaxTime(TimeUnit timeUnit) {
      return this.wrapped.getMaxTime(timeUnit);
   }

   public AggregateOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
      this.wrapped.maxTime(maxTime, timeUnit);
      return this;
   }

   public Collation getCollation() {
      return this.wrapped.getCollation();
   }

   public AggregateOperation<T> collation(@Nullable Collation collation) {
      this.wrapped.collation(collation);
      return this;
   }

   @Nullable
   public BsonValue getComment() {
      return this.wrapped.getComment();
   }

   public AggregateOperation<T> comment(@Nullable BsonValue comment) {
      this.wrapped.comment(comment);
      return this;
   }

   public AggregateOperation<T> let(@Nullable BsonDocument variables) {
      this.wrapped.let(variables);
      return this;
   }

   public AggregateOperation<T> retryReads(boolean retryReads) {
      this.wrapped.retryReads(retryReads);
      return this;
   }

   public boolean getRetryReads() {
      return this.wrapped.getRetryReads();
   }

   @Nullable
   public BsonDocument getHint() {
      BsonValue hint = this.wrapped.getHint();
      if (hint == null) {
         return null;
      } else if (!hint.isDocument()) {
         throw new IllegalArgumentException("Hint is not a BsonDocument please use the #getHintBsonValue() method. ");
      } else {
         return hint.asDocument();
      }
   }

   @Nullable
   public BsonValue getHintBsonValue() {
      return this.wrapped.getHint();
   }

   public AggregateOperation<T> hint(@Nullable BsonValue hint) {
      this.wrapped.hint(hint);
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      return this.wrapped.execute(binding);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      this.wrapped.executeAsync(binding, callback);
   }

   public <R> ReadOperation<R> asExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return new CommandReadOperation(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.wrapped.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
   }

   public <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable ExplainVerbosity verbosity, Decoder<R> resultDecoder) {
      return new CommandReadOperation(this.getNamespace().getDatabaseName(), ExplainHelper.asExplainCommand(this.wrapped.getCommand(NoOpSessionContext.INSTANCE, 0), verbosity), resultDecoder);
   }

   MongoNamespace getNamespace() {
      return this.wrapped.getNamespace();
   }

   Decoder<T> getDecoder() {
      return this.wrapped.getDecoder();
   }
}
