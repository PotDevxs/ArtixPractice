package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListSearchIndexesIterable;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
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

final class ListSearchIndexesIterableImpl<TResult> extends MongoIterableImpl<TResult> implements ListSearchIndexesIterable<TResult> {
   private final SyncOperations<BsonDocument> operations;
   private final Class<TResult> resultClass;
   @Nullable
   private Boolean allowDiskUse;
   @Nullable
   private long maxTimeMS;
   @Nullable
   private Collation collation;
   @Nullable
   private BsonValue comment;
   @Nullable
   private String indexName;
   private final CodecRegistry codecRegistry;

   ListSearchIndexesIterableImpl(MongoNamespace namespace, OperationExecutor executor, ReadConcern readConcern, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, boolean retryReads) {
      super((ClientSession)null, executor, readConcern, readPreference, retryReads);
      this.resultClass = resultClass;
      this.operations = new SyncOperations(namespace, BsonDocument.class, readPreference, codecRegistry, retryReads);
      this.codecRegistry = codecRegistry;
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.asAggregateOperation();
   }

   public ListSearchIndexesIterable<TResult> allowDiskUse(@Nullable Boolean allowDiskUse) {
      this.allowDiskUse = allowDiskUse;
      return this;
   }

   public ListSearchIndexesIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public ListSearchIndexesIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListSearchIndexesIterable<TResult> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public ListSearchIndexesIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public ListSearchIndexesIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ListSearchIndexesIterable<TResult> name(String indexName) {
      this.indexName = (String)Assertions.notNull("indexName", indexName);
      return this;
   }

   public Document explain() {
      return (Document)this.executeExplain(Document.class, (ExplainVerbosity)null);
   }

   public Document explain(ExplainVerbosity verbosity) {
      Assertions.notNull("verbosity", verbosity);
      return (Document)this.executeExplain(Document.class, verbosity);
   }

   public <E> E explain(Class<E> explainResultClass) {
      Assertions.notNull("explainResultClass", explainResultClass);
      return this.executeExplain(explainResultClass, (ExplainVerbosity)null);
   }

   public <E> E explain(Class<E> explainResultClass, ExplainVerbosity verbosity) {
      Assertions.notNull("explainResultClass", explainResultClass);
      Assertions.notNull("verbosity", verbosity);
      return this.executeExplain(explainResultClass, verbosity);
   }

   private <E> E executeExplain(Class<E> explainResultClass, @Nullable ExplainVerbosity verbosity) {
      return this.getExecutor().execute(this.asAggregateOperation().asExplainableOperation(verbosity, this.codecRegistry.get(explainResultClass)), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
   }

   private ExplainableReadOperation<BatchCursor<TResult>> asAggregateOperation() {
      return this.operations.listSearchIndexes(this.resultClass, this.maxTimeMS, this.indexName, this.getBatchSize(), this.collation, this.comment, this.allowDiskUse);
   }
}
