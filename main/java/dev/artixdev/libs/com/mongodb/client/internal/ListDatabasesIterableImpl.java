package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.ListDatabasesIterable;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class ListDatabasesIterableImpl<TResult> extends MongoIterableImpl<TResult> implements ListDatabasesIterable<TResult> {
   private final SyncOperations<BsonDocument> operations;
   private final Class<TResult> resultClass;
   private long maxTimeMS;
   private Bson filter;
   private Boolean nameOnly;
   private Boolean authorizedDatabasesOnly;
   private BsonValue comment;

   ListDatabasesIterableImpl(@Nullable ClientSession clientSession, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor) {
      this(clientSession, resultClass, codecRegistry, readPreference, executor, true);
   }

   public ListDatabasesIterableImpl(@Nullable ClientSession clientSession, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
      super(clientSession, executor, ReadConcern.DEFAULT, readPreference, retryReads);
      this.operations = new SyncOperations(BsonDocument.class, readPreference, codecRegistry, retryReads);
      this.resultClass = (Class)Assertions.notNull("clazz", resultClass);
   }

   public ListDatabasesIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public ListDatabasesIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public ListDatabasesIterable<TResult> filter(@Nullable Bson filter) {
      this.filter = filter;
      return this;
   }

   public ListDatabasesIterable<TResult> nameOnly(@Nullable Boolean nameOnly) {
      this.nameOnly = nameOnly;
      return this;
   }

   public ListDatabasesIterable<TResult> authorizedDatabasesOnly(@Nullable Boolean authorizedDatabasesOnly) {
      this.authorizedDatabasesOnly = authorizedDatabasesOnly;
      return this;
   }

   public ListDatabasesIterable<TResult> comment(@Nullable String comment) {
      this.comment = comment != null ? new BsonString(comment) : null;
      return this;
   }

   public ListDatabasesIterable<TResult> comment(@Nullable BsonValue comment) {
      this.comment = comment;
      return this;
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      return this.operations.listDatabases(this.resultClass, this.filter, this.nameOnly, this.maxTimeMS, this.authorizedDatabasesOnly, this.comment);
   }
}
