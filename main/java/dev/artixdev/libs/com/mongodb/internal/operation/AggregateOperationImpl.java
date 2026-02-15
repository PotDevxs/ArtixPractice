package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.AsyncBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.connection.QueryResult;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;

class AggregateOperationImpl<T> implements AsyncReadOperation<AsyncBatchCursor<T>>, ReadOperation<BatchCursor<T>> {
   private static final String RESULT = "result";
   private static final String CURSOR = "cursor";
   private static final String FIRST_BATCH = "firstBatch";
   private static final List<String> FIELD_NAMES_WITH_RESULT = Arrays.asList("result", "firstBatch");
   private final MongoNamespace namespace;
   private final List<BsonDocument> pipeline;
   private final Decoder<T> decoder;
   private final AggregateOperationImpl.AggregateTarget aggregateTarget;
   private final AggregateOperationImpl.PipelineCreator pipelineCreator;
   private boolean retryReads;
   private Boolean allowDiskUse;
   private Integer batchSize;
   private Collation collation;
   private BsonValue comment;
   private BsonValue hint;
   private long maxAwaitTimeMS;
   private long maxTimeMS;
   private BsonDocument variables;

   AggregateOperationImpl(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregationLevel aggregationLevel) {
      this(namespace, pipeline, decoder, defaultAggregateTarget((AggregationLevel)Assertions.notNull("aggregationLevel", aggregationLevel), ((MongoNamespace)Assertions.notNull("namespace", namespace)).getCollectionName()), defaultPipelineCreator(pipeline));
   }

   AggregateOperationImpl(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregateOperationImpl.AggregateTarget aggregateTarget, AggregateOperationImpl.PipelineCreator pipelineCreator) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.pipeline = (List)Assertions.notNull("pipeline", pipeline);
      this.decoder = (Decoder)Assertions.notNull("decoder", decoder);
      this.aggregateTarget = (AggregateOperationImpl.AggregateTarget)Assertions.notNull("aggregateTarget", aggregateTarget);
      this.pipelineCreator = (AggregateOperationImpl.PipelineCreator)Assertions.notNull("pipelineCreator", pipelineCreator);
   }

   MongoNamespace getNamespace() {
      return this.namespace;
   }

   List<BsonDocument> getPipeline() {
      return this.pipeline;
   }

   Decoder<T> getDecoder() {
      return this.decoder;
   }

   Boolean getAllowDiskUse() {
      return this.allowDiskUse;
   }

   AggregateOperationImpl<T> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.allowDiskUse = allowDiskUse;
      return this;
   }

   Integer getBatchSize() {
      return this.batchSize;
   }

   AggregateOperationImpl<T> batchSize(@Nullable Integer batchSize) {
      this.batchSize = batchSize;
      return this;
   }

   long getMaxAwaitTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
   }

   AggregateOperationImpl<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
      this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
      return this;
   }

   long getMaxTime(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
   }

   AggregateOperationImpl<T> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   Collation getCollation() {
      return this.collation;
   }

   AggregateOperationImpl<T> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   @Nullable
   BsonValue getComment() {
      return this.comment;
   }

   AggregateOperationImpl<T> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   AggregateOperationImpl<T> let(@Nullable BsonDocument variables) {
      this.variables = variables;
      return this;
   }

   AggregateOperationImpl<T> retryReads(boolean retryReads) {
      this.retryReads = retryReads;
      return this;
   }

   boolean getRetryReads() {
      return this.retryReads;
   }

   @Nullable
   BsonValue getHint() {
      return this.hint;
   }

   AggregateOperationImpl<T> hint(@Nullable BsonValue hint) {
      Assertions.isTrueArgument("BsonString or BsonDocument", hint == null || hint.isDocument() || hint.isString());
      this.hint = hint;
      return this;
   }

   public BatchCursor<T> execute(ReadBinding binding) {
      return (BatchCursor)SyncOperationHelper.executeRetryableRead(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, FIELD_NAMES_WITH_RESULT), this.transformer(), this.retryReads);
   }

   public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
      SingleResultCallback<AsyncBatchCursor<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      AsyncOperationHelper.executeRetryableReadAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, FIELD_NAMES_WITH_RESULT), this.asyncTransformer(), this.retryReads, errHandlingCallback);
   }

   private CommandOperationHelper.CommandCreator getCommandCreator(SessionContext sessionContext) {
      return (serverDescription, connectionDescription) -> {
         return this.getCommand(sessionContext, connectionDescription.getMaxWireVersion());
      };
   }

   BsonDocument getCommand(SessionContext sessionContext, int maxWireVersion) {
      BsonDocument commandDocument = new BsonDocument("aggregate", this.aggregateTarget.create());
      OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, maxWireVersion, commandDocument);
      commandDocument.put((String)"pipeline", (BsonValue)this.pipelineCreator.create());
      if (this.maxTimeMS > 0L) {
         commandDocument.put((String)"maxTimeMS", (BsonValue)(this.maxTimeMS > 2147483647L ? new BsonInt64(this.maxTimeMS) : new BsonInt32((int)this.maxTimeMS)));
      }

      BsonDocument cursor = new BsonDocument();
      if (this.batchSize != null) {
         cursor.put((String)"batchSize", (BsonValue)(new BsonInt32(this.batchSize)));
      }

      commandDocument.put((String)"cursor", (BsonValue)cursor);
      if (this.allowDiskUse != null) {
         commandDocument.put((String)"allowDiskUse", (BsonValue)BsonBoolean.valueOf(this.allowDiskUse));
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

      return commandDocument;
   }

   private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
      Assertions.assertNotNull(result);
      return OperationHelper.cursorDocumentToQueryResult(result.getDocument("cursor"), description.getServerAddress());
   }

   private SyncOperationHelper.CommandReadTransformer<BsonDocument, QueryBatchCursor<T>> transformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = this.createQueryResult(result, connection.getDescription());
         return new QueryBatchCursor(queryResult, 0, this.batchSize != null ? this.batchSize : 0, this.maxAwaitTimeMS, this.decoder, this.comment, source, connection, result);
      };
   }

   private AsyncOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
      return (result, source, connection) -> {
         QueryResult<T> queryResult = this.createQueryResult(result, connection.getDescription());
         return new AsyncQueryBatchCursor(queryResult, 0, this.batchSize != null ? this.batchSize : 0, this.maxAwaitTimeMS, this.decoder, this.comment, source, connection, result);
      };
   }

   private static AggregateOperationImpl.AggregateTarget defaultAggregateTarget(AggregationLevel aggregationLevel, String collectionName) {
      return () -> {
         return (BsonValue)(aggregationLevel == AggregationLevel.DATABASE ? new BsonInt32(1) : new BsonString(collectionName));
      };
   }

   private static AggregateOperationImpl.PipelineCreator defaultPipelineCreator(List<BsonDocument> pipeline) {
      return () -> {
         return new BsonArray(pipeline);
      };
   }

   interface AggregateTarget {
      BsonValue create();
   }

   interface PipelineCreator {
      BsonArray create();
   }
}
