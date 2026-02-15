package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.List;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.client.model.BulkWriteOptions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.CountOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateViewOptions;
import dev.artixdev.libs.com.mongodb.client.model.DeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.EstimatedDocumentCountOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndDeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndUpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.IndexModel;
import dev.artixdev.libs.com.mongodb.client.model.InsertManyOptions;
import dev.artixdev.libs.com.mongodb.client.model.InsertOneOptions;
import dev.artixdev.libs.com.mongodb.client.model.MapReduceAction;
import dev.artixdev.libs.com.mongodb.client.model.RenameCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.SearchIndexModel;
import dev.artixdev.libs.com.mongodb.client.model.UpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.WriteModel;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.client.model.FindOptions;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class SyncOperations<TDocument> {
   private final Operations<TDocument> operations;

   public SyncOperations(Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, boolean retryReads) {
      this((MongoNamespace)null, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, true, retryReads);
   }

   public SyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, boolean retryReads) {
      this(namespace, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, true, retryReads);
   }

   public SyncOperations(@Nullable MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, ReadConcern readConcern, WriteConcern writeConcern, boolean retryWrites, boolean retryReads) {
      this.operations = new Operations(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
   }

   public ReadOperation<Long> countDocuments(Bson filter, CountOptions options) {
      return this.operations.countDocuments(filter, options);
   }

   public ReadOperation<Long> estimatedDocumentCount(EstimatedDocumentCountOptions options) {
      return this.operations.estimatedDocumentCount(options);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> findFirst(Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.operations.findFirst(filter, resultClass, options);
   }

   public <TResult> ExplainableReadOperation<BatchCursor<TResult>> find(Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.operations.find(filter, resultClass, options);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> find(MongoNamespace findNamespace, Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.operations.find(findNamespace, filter, resultClass, options);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> distinct(String fieldName, Bson filter, Class<TResult> resultClass, long maxTimeMS, Collation collation, BsonValue comment) {
      return this.operations.distinct(fieldName, filter, resultClass, maxTimeMS, collation, comment);
   }

   public <TResult> ExplainableReadOperation<BatchCursor<TResult>> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass, long maxTimeMS, long maxAwaitTimeMS, @Nullable Integer batchSize, Collation collation, Bson hint, String hintString, BsonValue comment, Bson variables, Boolean allowDiskUse, AggregationLevel aggregationLevel) {
      return this.operations.aggregate(pipeline, resultClass, maxTimeMS, maxAwaitTimeMS, batchSize, collation, hint, hintString, comment, variables, allowDiskUse, aggregationLevel);
   }

   public ReadOperation<Void> aggregateToCollection(List<? extends Bson> pipeline, long maxTimeMS, Boolean allowDiskUse, Boolean bypassDocumentValidation, Collation collation, Bson hint, String hintString, BsonValue comment, Bson variables, AggregationLevel aggregationLevel) {
      return this.operations.aggregateToCollection(pipeline, maxTimeMS, allowDiskUse, bypassDocumentValidation, collation, hint, hintString, comment, variables, aggregationLevel);
   }

   public WriteOperation<MapReduceStatistics> mapReduceToCollection(String databaseName, String collectionName, String mapFunction, String reduceFunction, String finalizeFunction, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, MapReduceAction action, boolean nonAtomic, boolean sharded, Boolean bypassDocumentValidation, Collation collation) {
      return this.operations.mapReduceToCollection(databaseName, collectionName, mapFunction, reduceFunction, finalizeFunction, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, action, nonAtomic, sharded, bypassDocumentValidation, collation);
   }

   public <TResult> ReadOperation<MapReduceBatchCursor<TResult>> mapReduce(String mapFunction, String reduceFunction, String finalizeFunction, Class<TResult> resultClass, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, Collation collation) {
      return this.operations.mapReduce(mapFunction, reduceFunction, finalizeFunction, resultClass, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, collation);
   }

   public WriteOperation<TDocument> findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
      return this.operations.findOneAndDelete(filter, options);
   }

   public WriteOperation<TDocument> findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
      return this.operations.findOneAndReplace(filter, replacement, options);
   }

   public WriteOperation<TDocument> findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
      return this.operations.findOneAndUpdate(filter, update, options);
   }

   public WriteOperation<TDocument> findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
      return this.operations.findOneAndUpdate(filter, update, options);
   }

   public WriteOperation<BulkWriteResult> insertOne(TDocument document, InsertOneOptions options) {
      return this.operations.insertOne(document, options);
   }

   public WriteOperation<BulkWriteResult> replaceOne(Bson filter, TDocument replacement, ReplaceOptions options) {
      return this.operations.replaceOne(filter, replacement, options);
   }

   public WriteOperation<BulkWriteResult> deleteOne(Bson filter, DeleteOptions options) {
      return this.operations.deleteOne(filter, options);
   }

   public WriteOperation<BulkWriteResult> deleteMany(Bson filter, DeleteOptions options) {
      return this.operations.deleteMany(filter, options);
   }

   public WriteOperation<BulkWriteResult> updateOne(Bson filter, Bson update, UpdateOptions updateOptions) {
      return this.operations.updateOne(filter, update, updateOptions);
   }

   public WriteOperation<BulkWriteResult> updateOne(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      return this.operations.updateOne(filter, update, updateOptions);
   }

   public WriteOperation<BulkWriteResult> updateMany(Bson filter, Bson update, UpdateOptions updateOptions) {
      return this.operations.updateMany(filter, update, updateOptions);
   }

   public WriteOperation<BulkWriteResult> updateMany(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      return this.operations.updateMany(filter, update, updateOptions);
   }

   public WriteOperation<BulkWriteResult> insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
      return this.operations.insertMany(documents, options);
   }

   public WriteOperation<BulkWriteResult> bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
      return this.operations.bulkWrite(requests, options);
   }

   public <TResult> ReadOperation<TResult> commandRead(Bson command, Class<TResult> resultClass) {
      return this.operations.commandRead(command, resultClass);
   }

   public WriteOperation<Void> dropDatabase() {
      return this.operations.dropDatabase();
   }

   public WriteOperation<Void> createCollection(String collectionName, CreateCollectionOptions createCollectionOptions, @Nullable AutoEncryptionSettings autoEncryptionSettings) {
      return this.operations.createCollection(collectionName, createCollectionOptions, autoEncryptionSettings);
   }

   public WriteOperation<Void> dropCollection(DropCollectionOptions dropCollectionOptions, @Nullable AutoEncryptionSettings autoEncryptionSettings) {
      return this.operations.dropCollection(dropCollectionOptions, autoEncryptionSettings);
   }

   public WriteOperation<Void> renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
      return this.operations.renameCollection(newCollectionNamespace, options);
   }

   public WriteOperation<Void> createView(String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
      return this.operations.createView(viewName, viewOn, pipeline, createViewOptions);
   }

   public WriteOperation<Void> createIndexes(List<IndexModel> indexes, CreateIndexOptions options) {
      return this.operations.createIndexes(indexes, options);
   }

   public WriteOperation<Void> createSearchIndexes(List<SearchIndexModel> indexes) {
      return this.operations.createSearchIndexes(indexes);
   }

   public WriteOperation<Void> updateSearchIndex(String indexName, Bson definition) {
      return this.operations.updateSearchIndex(indexName, definition);
   }

   public WriteOperation<Void> dropSearchIndex(String indexName) {
      return this.operations.dropSearchIndex(indexName);
   }

   public <TResult> ExplainableReadOperation<BatchCursor<TResult>> listSearchIndexes(Class<TResult> resultClass, long maxTimeMS, @Nullable String indexName, @Nullable Integer batchSize, @Nullable Collation collation, @Nullable BsonValue comment, @Nullable Boolean allowDiskUse) {
      return this.operations.listSearchIndexes(resultClass, maxTimeMS, indexName, batchSize, collation, comment, allowDiskUse);
   }

   public WriteOperation<Void> dropIndex(String indexName, DropIndexOptions options) {
      return this.operations.dropIndex(indexName, options);
   }

   public WriteOperation<Void> dropIndex(Bson keys, DropIndexOptions options) {
      return this.operations.dropIndex(keys, options);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> listCollections(String databaseName, Class<TResult> resultClass, Bson filter, boolean collectionNamesOnly, @Nullable Integer batchSize, long maxTimeMS, BsonValue comment) {
      return this.operations.listCollections(databaseName, resultClass, filter, collectionNamesOnly, batchSize, maxTimeMS, comment);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> listDatabases(Class<TResult> resultClass, Bson filter, Boolean nameOnly, long maxTimeMS, Boolean authorizedDatabases, BsonValue comment) {
      return this.operations.listDatabases(resultClass, filter, nameOnly, maxTimeMS, authorizedDatabases, comment);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> listIndexes(Class<TResult> resultClass, @Nullable Integer batchSize, long maxTimeMS, BsonValue comment) {
      return this.operations.listIndexes(resultClass, batchSize, maxTimeMS, comment);
   }

   public <TResult> ReadOperation<BatchCursor<TResult>> changeStream(FullDocument fullDocument, FullDocumentBeforeChange fullDocumentBeforeChange, List<? extends Bson> pipeline, Decoder<TResult> decoder, ChangeStreamLevel changeStreamLevel, @Nullable Integer batchSize, Collation collation, BsonValue comment, long maxAwaitTimeMS, BsonDocument resumeToken, BsonTimestamp startAtOperationTime, BsonDocument startAfter, boolean showExpandedEvents) {
      return this.operations.changeStream(fullDocument, fullDocumentBeforeChange, pipeline, decoder, changeStreamLevel, batchSize, collation, comment, maxAwaitTimeMS, resumeToken, startAtOperationTime, startAfter, showExpandedEvents);
   }
}
