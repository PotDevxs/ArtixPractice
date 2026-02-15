package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.List;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.AggregateIterable;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.client.model.FindOptions;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ExplainableReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class AggregateIterableImpl<TDocument, TResult> extends MongoIterableImpl<TResult> implements AggregateIterable<TResult> {
   private final SyncOperations<TDocument> operations;
   private final MongoNamespace namespace;
   private final Class<TDocument> documentClass;
   private final Class<TResult> resultClass;
   private final CodecRegistry codecRegistry;
   private final List<? extends Bson> pipeline;
   private final AggregationLevel aggregationLevel;
   private Boolean allowDiskUse;
   private long maxTimeMS;
   private long maxAwaitTimeMS;
   private Boolean bypassDocumentValidation;
   private Collation collation;
   private BsonValue comment;
   private Bson hint;
   private String hintString;
   private Bson variables;

   AggregateIterableImpl(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel) {
      this(clientSession, new MongoNamespace(databaseName, "ignored"), documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, true);
   }

   AggregateIterableImpl(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
      this(clientSession, new MongoNamespace(databaseName, "ignored"), documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
   }

   AggregateIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
      super(clientSession, executor, readConcern, readPreference, retryReads);
      this.operations = new SyncOperations(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, true, retryReads);
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.documentClass = (Class)Assertions.notNull("documentClass", documentClass);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
      this.pipeline = (List)Assertions.notNull("pipeline", pipeline);
      this.aggregationLevel = (AggregationLevel)Assertions.notNull("aggregationLevel", aggregationLevel);
   }

   public void toCollection() {
      BsonDocument lastPipelineStage = this.getLastPipelineStage();
      if (lastPipelineStage != null && (lastPipelineStage.containsKey("$out") || lastPipelineStage.containsKey("$merge"))) {
         this.getExecutor().execute(this.operations.aggregateToCollection(this.pipeline, this.maxTimeMS, this.allowDiskUse, this.bypassDocumentValidation, this.collation, this.hint, this.hintString, this.comment, this.variables, this.aggregationLevel), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
      } else {
         throw new IllegalStateException("The last stage of the aggregation pipeline must be $out or $merge");
      }
   }

   public AggregateIterable<TResult> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.allowDiskUse = allowDiskUse;
      return this;
   }

   public AggregateIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public AggregateIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public AggregateIterable<TResult> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
      return this;
   }

   public AggregateIterable<TResult> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public AggregateIterable<TResult> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public AggregateIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public AggregateIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public AggregateIterable<TResult> hint(@Nullable Bson hint) {
      this.hint = hint;
      return this;
   }

   public AggregateIterable<TResult> hintString(@Nullable String hint) {
      this.hintString = hint;
      return this;
   }

   public AggregateIterable<TResult> let(@Nullable Bson variables) {
      this.variables = variables;
      return this;
   }

   public Document explain() {
      return (Document)this.executeExplain(Document.class, (ExplainVerbosity)null);
   }

   public Document explain(ExplainVerbosity verbosity) {
      return (Document)this.executeExplain(Document.class, (ExplainVerbosity)Assertions.notNull("verbosity", verbosity));
   }

   public <E> E explain(Class<E> explainDocumentClass) {
      return this.executeExplain(explainDocumentClass, (ExplainVerbosity)null);
   }

   public <E> E explain(Class<E> explainResultClass, ExplainVerbosity verbosity) {
      return this.executeExplain(explainResultClass, (ExplainVerbosity)Assertions.notNull("verbosity", verbosity));
   }

   private <E> E executeExplain(Class<E> explainResultClass, @Nullable ExplainVerbosity verbosity) {
      Assertions.notNull("explainDocumentClass", explainResultClass);
      return this.getExecutor().execute(this.asAggregateOperation().asExplainableOperation(verbosity, this.codecRegistry.get(explainResultClass)), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      MongoNamespace outNamespace = this.getOutNamespace();
      if (outNamespace != null) {
         this.getExecutor().execute(this.operations.aggregateToCollection(this.pipeline, this.maxTimeMS, this.allowDiskUse, this.bypassDocumentValidation, this.collation, this.hint, this.hintString, this.comment, this.variables, this.aggregationLevel), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
         FindOptions findOptions = (new FindOptions()).collation(this.collation);
         Integer batchSize = this.getBatchSize();
         if (batchSize != null) {
            findOptions.batchSize(batchSize);
         }

         return this.operations.find(outNamespace, new BsonDocument(), this.resultClass, findOptions);
      } else {
         return this.asAggregateOperation();
      }
   }

   private ExplainableReadOperation<BatchCursor<TResult>> asAggregateOperation() {
      return this.operations.aggregate(this.pipeline, this.resultClass, this.maxTimeMS, this.maxAwaitTimeMS, this.getBatchSize(), this.collation, this.hint, this.hintString, this.comment, this.variables, this.allowDiskUse, this.aggregationLevel);
   }

   @Nullable
   private BsonDocument getLastPipelineStage() {
      if (this.pipeline.isEmpty()) {
         return null;
      } else {
         Bson lastStage = (Bson)Assertions.notNull("last pipeline stage", (Bson)this.pipeline.get(this.pipeline.size() - 1));
         return lastStage.toBsonDocument(this.documentClass, this.codecRegistry);
      }
   }

   @Nullable
   private MongoNamespace getOutNamespace() {
      BsonDocument lastPipelineStage = this.getLastPipelineStage();
      if (lastPipelineStage == null) {
         return null;
      } else {
         BsonDocument mergeDocument;
         if (lastPipelineStage.containsKey("$out")) {
            if (lastPipelineStage.get("$out").isString()) {
               return new MongoNamespace(this.namespace.getDatabaseName(), lastPipelineStage.getString("$out").getValue());
            } else if (lastPipelineStage.get("$out").isDocument()) {
               mergeDocument = lastPipelineStage.getDocument("$out");
               if (mergeDocument.containsKey("db") && mergeDocument.containsKey("coll")) {
                  return new MongoNamespace(mergeDocument.getString("db").getValue(), mergeDocument.getString("coll").getValue());
               } else {
                  throw new IllegalStateException("Cannot return a cursor when the value for $out stage is not a namespace document");
               }
            } else {
               throw new IllegalStateException("Cannot return a cursor when the value for $out stage is not a string or namespace document");
            }
         } else {
            if (lastPipelineStage.containsKey("$merge")) {
               if (lastPipelineStage.isString("$merge")) {
                  return new MongoNamespace(this.namespace.getDatabaseName(), lastPipelineStage.getString("$merge").getValue());
               }

               if (!lastPipelineStage.isDocument("$merge")) {
                  throw new IllegalStateException("Cannot return a cursor when the value for $merge stage is not a string or a document");
               }

               mergeDocument = lastPipelineStage.getDocument("$merge");
               if (mergeDocument.isDocument("into")) {
                  BsonDocument intoDocument = mergeDocument.getDocument("into");
                  return new MongoNamespace(intoDocument.getString("db", new BsonString(this.namespace.getDatabaseName())).getValue(), intoDocument.getString("coll").getValue());
               }

               if (mergeDocument.isString("into")) {
                  return new MongoNamespace(this.namespace.getDatabaseName(), mergeDocument.getString("into").getValue());
               }
            }

            return null;
         }
      }
   }
}
