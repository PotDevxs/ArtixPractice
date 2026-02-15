package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListIndexesIterable;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class ListIndexesIterableImpl<TResult> extends MongoIterableImpl<TResult> implements ListIndexesIterable<TResult> {
   private final SyncOperations<BsonDocument> operations;
   private final Class<TResult> resultClass;
   private long maxTimeMS;
   private BsonValue comment;

   ListIndexesIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor) {
      this(clientSession, namespace, resultClass, codecRegistry, readPreference, executor, true);
   }

   ListIndexesIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
      super(clientSession, executor, ReadConcern.DEFAULT, readPreference, retryReads);
      this.operations = new SyncOperations(namespace, BsonDocument.class, readPreference, codecRegistry, retryReads);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
   }

   public ListIndexesIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListIndexesIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public ListIndexesIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public ListIndexesIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.operations.listIndexes(this.resultClass, this.getBatchSize(), this.maxTimeMS, this.comment);
   }
}
