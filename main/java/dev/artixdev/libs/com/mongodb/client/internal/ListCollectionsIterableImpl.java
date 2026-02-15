package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListCollectionsIterable;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class ListCollectionsIterableImpl<TResult> extends MongoIterableImpl<TResult> implements ListCollectionsIterable<TResult> {
   private final SyncOperations<BsonDocument> operations;
   private final String databaseName;
   private final Class<TResult> resultClass;
   private Bson filter;
   private final boolean collectionNamesOnly;
   private long maxTimeMS;
   private BsonValue comment;

   ListCollectionsIterableImpl(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor) {
      this(clientSession, databaseName, collectionNamesOnly, resultClass, codecRegistry, readPreference, executor, true);
   }

   ListCollectionsIterableImpl(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
      super(clientSession, executor, ReadConcern.DEFAULT, readPreference, retryReads);
      this.collectionNamesOnly = collectionNamesOnly;
      this.operations = new SyncOperations(BsonDocument.class, readPreference, codecRegistry, retryReads);
      this.databaseName = (String)Assertions.notNull("databaseName", databaseName);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
   }

   public ListCollectionsIterable<TResult> filter(@Nullable Bson filter) {
      this.filter = filter;
      return this;
   }

   public ListCollectionsIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListCollectionsIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public ListCollectionsIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public ListCollectionsIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.operations.listCollections(this.databaseName, this.resultClass, this.filter, this.collectionNamesOnly, this.getBatchSize(), this.maxTimeMS, this.comment);
   }
}
