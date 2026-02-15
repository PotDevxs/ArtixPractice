package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.MapReduceIterable;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.MapReduceAction;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.client.model.FindOptions;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.MapReduceBatchCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.MapReduceStatistics;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.internal.operation.WriteOperation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class MapReduceIterableImpl<TDocument, TResult> extends MongoIterableImpl<TResult> implements MapReduceIterable<TResult> {
   private final SyncOperations<TDocument> operations;
   private final MongoNamespace namespace;
   private final Class<TResult> resultClass;
   private final String mapFunction;
   private final String reduceFunction;
   private boolean inline = true;
   private String collectionName;
   private String finalizeFunction;
   private Bson scope;
   private Bson filter;
   private Bson sort;
   private int limit;
   private boolean jsMode;
   private boolean verbose = true;
   private long maxTimeMS;
   private MapReduceAction action;
   private String databaseName;
   private boolean sharded;
   private boolean nonAtomic;
   private Boolean bypassDocumentValidation;
   private Collation collation;

   MapReduceIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
      super(clientSession, executor, readConcern, readPreference, false);
      this.action = MapReduceAction.REPLACE;
      this.operations = new SyncOperations(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, false, false);
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.resultClass = (Class)Assertions.notNull("resultClass", resultClass);
      this.mapFunction = (String)Assertions.notNull("mapFunction", mapFunction);
      this.reduceFunction = (String)Assertions.notNull("reduceFunction", reduceFunction);
   }

   public void toCollection() {
      if (this.inline) {
         throw new IllegalStateException("The options must specify a non-inline result");
      } else {
         this.getExecutor().execute(this.createMapReduceToCollectionOperation(), this.getReadConcern(), this.getClientSession());
      }
   }

   public MapReduceIterable<TResult> collectionName(String collectionName) {
      this.collectionName = (String)Assertions.notNull("collectionName", collectionName);
      this.inline = false;
      return this;
   }

   public MapReduceIterable<TResult> finalizeFunction(@Nullable String finalizeFunction) {
      this.finalizeFunction = finalizeFunction;
      return this;
   }

   public MapReduceIterable<TResult> scope(@Nullable Bson scope) {
      this.scope = scope;
      return this;
   }

   public MapReduceIterable<TResult> sort(@Nullable Bson sort) {
      this.sort = sort;
      return this;
   }

   public MapReduceIterable<TResult> filter(@Nullable Bson filter) {
      this.filter = filter;
      return this;
   }

   public MapReduceIterable<TResult> limit(int limit) {
      this.limit = limit;
      return this;
   }

   public MapReduceIterable<TResult> jsMode(boolean jsMode) {
      this.jsMode = jsMode;
      return this;
   }

   public MapReduceIterable<TResult> verbose(boolean verbose) {
      this.verbose = verbose;
      return this;
   }

   public MapReduceIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
      return this;
   }

   public MapReduceIterable<TResult> action(MapReduceAction action) {
      this.action = action;
      return this;
   }

   public MapReduceIterable<TResult> databaseName(@Nullable String databaseName) {
      this.databaseName = databaseName;
      return this;
   }

   public MapReduceIterable<TResult> sharded(boolean sharded) {
      this.sharded = sharded;
      return this;
   }

   public MapReduceIterable<TResult> nonAtomic(boolean nonAtomic) {
      this.nonAtomic = nonAtomic;
      return this;
   }

   public MapReduceIterable<TResult> batchSize(int batchSize) {
      super.batchSize(batchSize);
      return this;
   }

   public MapReduceIterable<TResult> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
      this.bypassDocumentValidation = bypassDocumentValidation;
      return this;
   }

   public MapReduceIterable<TResult> collation(@Nullable Collation collation) {
      this.collation = collation;
      return this;
   }

   ReadPreference getReadPreference() {
      return this.inline ? super.getReadPreference() : ReadPreference.primary();
   }

   public ReadOperation<BatchCursor<TResult>> asReadOperation() {
      if (this.inline) {
         ReadOperation<MapReduceBatchCursor<TResult>> operation = this.operations.mapReduce(this.mapFunction, this.reduceFunction, this.finalizeFunction, this.resultClass, this.filter, this.limit, this.maxTimeMS, this.jsMode, this.scope, this.sort, this.verbose, this.collation);
         return new MapReduceIterableImpl.WrappedMapReduceReadOperation(operation);
      } else {
         this.getExecutor().execute(this.createMapReduceToCollectionOperation(), this.getReadConcern(), this.getClientSession());
         String dbName = this.databaseName != null ? this.databaseName : this.namespace.getDatabaseName();
         FindOptions findOptions = (new FindOptions()).collation(this.collation);
         Integer batchSize = this.getBatchSize();
         if (batchSize != null) {
            findOptions.batchSize(batchSize);
         }

         return this.operations.find(new MongoNamespace(dbName, this.collectionName), new BsonDocument(), this.resultClass, findOptions);
      }
   }

   private WriteOperation<MapReduceStatistics> createMapReduceToCollectionOperation() {
      return this.operations.mapReduceToCollection(this.databaseName, this.collectionName, this.mapFunction, this.reduceFunction, this.finalizeFunction, this.filter, this.limit, this.maxTimeMS, this.jsMode, this.scope, this.sort, this.verbose, this.action, this.nonAtomic, this.sharded, this.bypassDocumentValidation, this.collation);
   }

   static class WrappedMapReduceReadOperation<TResult> implements ReadOperation<BatchCursor<TResult>> {
      private final ReadOperation<MapReduceBatchCursor<TResult>> operation;

      ReadOperation<MapReduceBatchCursor<TResult>> getOperation() {
         return this.operation;
      }

      WrappedMapReduceReadOperation(ReadOperation<MapReduceBatchCursor<TResult>> operation) {
         this.operation = operation;
      }

      public BatchCursor<TResult> execute(ReadBinding binding) {
         return (BatchCursor)this.operation.execute(binding);
      }
   }
}
