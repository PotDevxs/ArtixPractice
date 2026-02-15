package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.DistinctIterable;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class DistinctIterableImpl<TDocument, TResult> extends MongoIterableImpl<TResult> implements DistinctIterable<TResult> {
   private final SyncOperations<TDocument> operations;
   private final Class<TResult> resultClass;
   private final String fieldName;
   private Bson filter;
   private long maxTimeMS;
   private Collation collation;
   private BsonValue comment;

   DistinctIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter) {
      this(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, true);
   }

   DistinctIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
      super(clientSession, executor, readConcern, readPreference, retryReads);
      this.operations = new SyncOperations(namespace, documentClass, readPreference, codecRegistry, retryReads);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
      this.fieldName = (String)Assertions.notNull("mapFunction", fieldName);
      this.filter = filter;
   }

   public DistinctIterable<TResult> filter(@Nullable Bson filter) {
      this.filter = filter;
      return this;
   }

   public DistinctIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public DistinctIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public DistinctIterable<TResult> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   public DistinctIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment == null ? null : new BsonString(comment);
      return this;
   }

   public DistinctIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.operations.distinct(this.fieldName, this.filter, this.resultClass, this.maxTimeMS, this.collation, this.comment);
   }
}
