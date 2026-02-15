package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoBulkWriteException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.MongoWriteConcernException;
import dev.artixdev.libs.com.mongodb.MongoWriteException;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.WriteConcernResult;
import dev.artixdev.libs.com.mongodb.WriteError;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteInsert;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteUpsert;
import dev.artixdev.libs.com.mongodb.client.AggregateIterable;
import dev.artixdev.libs.com.mongodb.client.ChangeStreamIterable;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.DistinctIterable;
import dev.artixdev.libs.com.mongodb.client.FindIterable;
import dev.artixdev.libs.com.mongodb.client.ListIndexesIterable;
import dev.artixdev.libs.com.mongodb.client.ListSearchIndexesIterable;
import dev.artixdev.libs.com.mongodb.client.MapReduceIterable;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.model.BulkWriteOptions;
import dev.artixdev.libs.com.mongodb.client.model.CountOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.DeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.EstimatedDocumentCountOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndDeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndUpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.IndexModel;
import dev.artixdev.libs.com.mongodb.client.model.IndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.InsertManyOptions;
import dev.artixdev.libs.com.mongodb.client.model.InsertOneOptions;
import dev.artixdev.libs.com.mongodb.client.model.RenameCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.SearchIndexModel;
import dev.artixdev.libs.com.mongodb.client.model.UpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.WriteModel;
import dev.artixdev.libs.com.mongodb.client.result.DeleteResult;
import dev.artixdev.libs.com.mongodb.client.result.InsertManyResult;
import dev.artixdev.libs.com.mongodb.client.result.InsertOneResult;
import dev.artixdev.libs.com.mongodb.client.result.UpdateResult;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.internal.operation.IndexHelper;
import dev.artixdev.libs.com.mongodb.internal.operation.RenameCollectionOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.SyncOperations;
import dev.artixdev.libs.com.mongodb.internal.operation.WriteOperation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

class MongoCollectionImpl<TDocument> implements MongoCollection<TDocument> {
   private final MongoNamespace namespace;
   private final Class<TDocument> documentClass;
   private final ReadPreference readPreference;
   private final CodecRegistry codecRegistry;
   private final WriteConcern writeConcern;
   private final boolean retryWrites;
   private final boolean retryReads;
   private final ReadConcern readConcern;
   private final SyncOperations<TDocument> operations;
   private final UuidRepresentation uuidRepresentation;
   @Nullable
   private final AutoEncryptionSettings autoEncryptionSettings;
   private final OperationExecutor executor;

   MongoCollectionImpl(MongoNamespace namespace, Class<TDocument> documentClass, CodecRegistry codecRegistry, ReadPreference readPreference, WriteConcern writeConcern, boolean retryWrites, boolean retryReads, ReadConcern readConcern, UuidRepresentation uuidRepresentation, @Nullable AutoEncryptionSettings autoEncryptionSettings, OperationExecutor executor) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.documentClass = (Class)Assertions.notNull("documentClass", documentClass);
      this.codecRegistry = (CodecRegistry)Assertions.notNull("codecRegistry", codecRegistry);
      this.readPreference = (ReadPreference)Assertions.notNull("readPreference", readPreference);
      this.writeConcern = (WriteConcern)Assertions.notNull("writeConcern", writeConcern);
      this.retryWrites = retryWrites;
      this.retryReads = retryReads;
      this.readConcern = (ReadConcern)Assertions.notNull("readConcern", readConcern);
      this.executor = (OperationExecutor)Assertions.notNull("executor", executor);
      this.uuidRepresentation = (UuidRepresentation)Assertions.notNull("uuidRepresentation", uuidRepresentation);
      this.autoEncryptionSettings = autoEncryptionSettings;
      this.operations = new SyncOperations(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
   }

   public MongoNamespace getNamespace() {
      return this.namespace;
   }

   public Class<TDocument> getDocumentClass() {
      return this.documentClass;
   }

   public CodecRegistry getCodecRegistry() {
      return this.codecRegistry;
   }

   public ReadPreference getReadPreference() {
      return this.readPreference;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public ReadConcern getReadConcern() {
      return this.readConcern;
   }

   public <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(Class<NewTDocument> clazz) {
      return new MongoCollectionImpl(this.namespace, clazz, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoCollection<TDocument> withCodecRegistry(CodecRegistry codecRegistry) {
      return new MongoCollectionImpl(this.namespace, this.documentClass, CodecRegistries.withUuidRepresentation(codecRegistry, this.uuidRepresentation), this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoCollection<TDocument> withReadPreference(ReadPreference readPreference) {
      return new MongoCollectionImpl(this.namespace, this.documentClass, this.codecRegistry, readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoCollection<TDocument> withWriteConcern(WriteConcern writeConcern) {
      return new MongoCollectionImpl(this.namespace, this.documentClass, this.codecRegistry, this.readPreference, writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public MongoCollection<TDocument> withReadConcern(ReadConcern readConcern) {
      return new MongoCollectionImpl(this.namespace, this.documentClass, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, readConcern, this.uuidRepresentation, this.autoEncryptionSettings, this.executor);
   }

   public long countDocuments() {
      return this.countDocuments((Bson)(new BsonDocument()));
   }

   public long countDocuments(Bson filter) {
      return this.countDocuments(filter, new CountOptions());
   }

   public long countDocuments(Bson filter, CountOptions options) {
      return this.executeCount((ClientSession)null, filter, options);
   }

   public long countDocuments(ClientSession clientSession) {
      return this.countDocuments((ClientSession)clientSession, (Bson)(new BsonDocument()));
   }

   public long countDocuments(ClientSession clientSession, Bson filter) {
      return this.countDocuments(clientSession, filter, new CountOptions());
   }

   public long countDocuments(ClientSession clientSession, Bson filter, CountOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeCount(clientSession, filter, options);
   }

   public long estimatedDocumentCount() {
      return this.estimatedDocumentCount(new EstimatedDocumentCountOptions());
   }

   public long estimatedDocumentCount(EstimatedDocumentCountOptions options) {
      return (Long)this.executor.execute(this.operations.estimatedDocumentCount(options), this.readPreference, this.readConcern, (ClientSession)null);
   }

   private long executeCount(@Nullable ClientSession clientSession, Bson filter, CountOptions options) {
      return (Long)this.executor.execute(this.operations.countDocuments(filter, options), this.readPreference, this.readConcern, clientSession);
   }

   public <TResult> DistinctIterable<TResult> distinct(String fieldName, Class<TResult> resultClass) {
      return this.distinct((String)fieldName, (Bson)(new BsonDocument()), resultClass);
   }

   public <TResult> DistinctIterable<TResult> distinct(String fieldName, Bson filter, Class<TResult> resultClass) {
      return this.createDistinctIterable((ClientSession)null, fieldName, filter, resultClass);
   }

   public <TResult> DistinctIterable<TResult> distinct(ClientSession clientSession, String fieldName, Class<TResult> resultClass) {
      return this.distinct(clientSession, fieldName, new BsonDocument(), resultClass);
   }

   public <TResult> DistinctIterable<TResult> distinct(ClientSession clientSession, String fieldName, Bson filter, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createDistinctIterable(clientSession, fieldName, filter, resultClass);
   }

   private <TResult> DistinctIterable<TResult> createDistinctIterable(@Nullable ClientSession clientSession, String fieldName, Bson filter, Class<TResult> resultClass) {
      return new DistinctIterableImpl(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.executor, fieldName, filter, this.retryReads);
   }

   public FindIterable<TDocument> find() {
      return this.find((Bson)(new BsonDocument()), (Class)this.documentClass);
   }

   public <TResult> FindIterable<TResult> find(Class<TResult> resultClass) {
      return this.find((Bson)(new BsonDocument()), (Class)resultClass);
   }

   public FindIterable<TDocument> find(Bson filter) {
      return this.find(filter, this.documentClass);
   }

   public <TResult> FindIterable<TResult> find(Bson filter, Class<TResult> resultClass) {
      return this.createFindIterable((ClientSession)null, filter, resultClass);
   }

   public FindIterable<TDocument> find(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      return this.find(clientSession, new BsonDocument(), this.documentClass);
   }

   public <TResult> FindIterable<TResult> find(ClientSession clientSession, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.find(clientSession, new BsonDocument(), resultClass);
   }

   public FindIterable<TDocument> find(ClientSession clientSession, Bson filter) {
      Assertions.notNull("clientSession", clientSession);
      return this.find(clientSession, filter, this.documentClass);
   }

   public <TResult> FindIterable<TResult> find(ClientSession clientSession, Bson filter, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createFindIterable(clientSession, filter, resultClass);
   }

   private <TResult> FindIterable<TResult> createFindIterable(@Nullable ClientSession clientSession, Bson filter, Class<TResult> resultClass) {
      return new FindIterableImpl(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.executor, filter, this.retryReads);
   }

   public AggregateIterable<TDocument> aggregate(List<? extends Bson> pipeline) {
      return this.aggregate(pipeline, this.documentClass);
   }

   public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return this.createAggregateIterable((ClientSession)null, pipeline, resultClass);
   }

   public AggregateIterable<TDocument> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
      return this.aggregate(clientSession, pipeline, this.documentClass);
   }

   public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createAggregateIterable(clientSession, pipeline, resultClass);
   }

   private <TResult> AggregateIterable<TResult> createAggregateIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return new AggregateIterableImpl(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, pipeline, AggregationLevel.COLLECTION, this.retryReads);
   }

   public ChangeStreamIterable<TDocument> watch() {
      return this.watch(Collections.emptyList());
   }

   public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
      return this.watch(Collections.emptyList(), resultClass);
   }

   public ChangeStreamIterable<TDocument> watch(List<? extends Bson> pipeline) {
      return this.watch(pipeline, this.documentClass);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return this.createChangeStreamIterable((ClientSession)null, pipeline, resultClass);
   }

   public ChangeStreamIterable<TDocument> watch(ClientSession clientSession) {
      return this.watch(clientSession, Collections.emptyList(), this.documentClass);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
      return this.watch(clientSession, Collections.emptyList(), resultClass);
   }

   public ChangeStreamIterable<TDocument> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
      return this.watch(clientSession, pipeline, this.documentClass);
   }

   public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
   }

   private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
      return new ChangeStreamIterableImpl(clientSession, this.namespace, this.codecRegistry, this.readPreference, this.readConcern, this.executor, pipeline, resultClass, ChangeStreamLevel.COLLECTION, this.retryReads);
   }

   public MapReduceIterable<TDocument> mapReduce(String mapFunction, String reduceFunction) {
      return this.mapReduce(mapFunction, reduceFunction, this.documentClass);
   }

   public <TResult> MapReduceIterable<TResult> mapReduce(String mapFunction, String reduceFunction, Class<TResult> resultClass) {
      return this.createMapReduceIterable((ClientSession)null, mapFunction, reduceFunction, resultClass);
   }

   public MapReduceIterable<TDocument> mapReduce(ClientSession clientSession, String mapFunction, String reduceFunction) {
      return this.mapReduce(clientSession, mapFunction, reduceFunction, this.documentClass);
   }

   public <TResult> MapReduceIterable<TResult> mapReduce(ClientSession clientSession, String mapFunction, String reduceFunction, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createMapReduceIterable(clientSession, mapFunction, reduceFunction, resultClass);
   }

   private <TResult> MapReduceIterable<TResult> createMapReduceIterable(@Nullable ClientSession clientSession, String mapFunction, String reduceFunction, Class<TResult> resultClass) {
      return new MapReduceIterableImpl(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, mapFunction, reduceFunction);
   }

   public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> requests) {
      return this.bulkWrite(requests, new BulkWriteOptions());
   }

   public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
      return this.executeBulkWrite((ClientSession)null, requests, options);
   }

   public BulkWriteResult bulkWrite(ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests) {
      return this.bulkWrite(clientSession, requests, new BulkWriteOptions());
   }

   public BulkWriteResult bulkWrite(ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeBulkWrite(clientSession, requests, options);
   }

   private BulkWriteResult executeBulkWrite(@Nullable ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
      Assertions.notNull("requests", requests);
      return (BulkWriteResult)this.executor.execute(this.operations.bulkWrite(requests, options), this.readConcern, clientSession);
   }

   public InsertOneResult insertOne(TDocument document) {
      return this.insertOne(document, new InsertOneOptions());
   }

   public InsertOneResult insertOne(TDocument document, InsertOneOptions options) {
      Assertions.notNull("document", document);
      return this.executeInsertOne((ClientSession)null, document, options);
   }

   public InsertOneResult insertOne(ClientSession clientSession, TDocument document) {
      return this.insertOne(clientSession, document, new InsertOneOptions());
   }

   public InsertOneResult insertOne(ClientSession clientSession, TDocument document, InsertOneOptions options) {
      Assertions.notNull("clientSession", clientSession);
      Assertions.notNull("document", document);
      return this.executeInsertOne(clientSession, document, options);
   }

   private InsertOneResult executeInsertOne(@Nullable ClientSession clientSession, TDocument document, InsertOneOptions options) {
      return this.toInsertOneResult(this.executeSingleWriteRequest(clientSession, this.operations.insertOne(document, options), WriteRequest.Type.INSERT));
   }

   public InsertManyResult insertMany(List<? extends TDocument> documents) {
      return this.insertMany(documents, new InsertManyOptions());
   }

   public InsertManyResult insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
      return this.executeInsertMany((ClientSession)null, documents, options);
   }

   public InsertManyResult insertMany(ClientSession clientSession, List<? extends TDocument> documents) {
      return this.insertMany(clientSession, documents, new InsertManyOptions());
   }

   public InsertManyResult insertMany(ClientSession clientSession, List<? extends TDocument> documents, InsertManyOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeInsertMany(clientSession, documents, options);
   }

   private InsertManyResult executeInsertMany(@Nullable ClientSession clientSession, List<? extends TDocument> documents, InsertManyOptions options) {
      return this.toInsertManyResult((BulkWriteResult)this.executor.execute(this.operations.insertMany(documents, options), this.readConcern, clientSession));
   }

   public DeleteResult deleteOne(Bson filter) {
      return this.deleteOne(filter, new DeleteOptions());
   }

   public DeleteResult deleteOne(Bson filter, DeleteOptions options) {
      return this.executeDelete((ClientSession)null, filter, options, false);
   }

   public DeleteResult deleteOne(ClientSession clientSession, Bson filter) {
      return this.deleteOne(clientSession, filter, new DeleteOptions());
   }

   public DeleteResult deleteOne(ClientSession clientSession, Bson filter, DeleteOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeDelete(clientSession, filter, options, false);
   }

   public DeleteResult deleteMany(Bson filter) {
      return this.deleteMany(filter, new DeleteOptions());
   }

   public DeleteResult deleteMany(Bson filter, DeleteOptions options) {
      return this.executeDelete((ClientSession)null, filter, options, true);
   }

   public DeleteResult deleteMany(ClientSession clientSession, Bson filter) {
      return this.deleteMany(clientSession, filter, new DeleteOptions());
   }

   public DeleteResult deleteMany(ClientSession clientSession, Bson filter, DeleteOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeDelete(clientSession, filter, options, true);
   }

   public UpdateResult replaceOne(Bson filter, TDocument replacement) {
      return this.replaceOne(filter, replacement, new ReplaceOptions());
   }

   public UpdateResult replaceOne(Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
      return this.executeReplaceOne((ClientSession)null, filter, replacement, replaceOptions);
   }

   public UpdateResult replaceOne(ClientSession clientSession, Bson filter, TDocument replacement) {
      return this.replaceOne(clientSession, filter, replacement, new ReplaceOptions());
   }

   public UpdateResult replaceOne(ClientSession clientSession, Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeReplaceOne(clientSession, filter, replacement, replaceOptions);
   }

   private UpdateResult executeReplaceOne(@Nullable ClientSession clientSession, Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
      return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, this.operations.replaceOne(filter, replacement, replaceOptions), WriteRequest.Type.REPLACE));
   }

   public UpdateResult updateOne(Bson filter, Bson update) {
      return this.updateOne(filter, update, new UpdateOptions());
   }

   public UpdateResult updateOne(Bson filter, Bson update, UpdateOptions updateOptions) {
      return this.executeUpdate((ClientSession)null, filter, (Bson)update, updateOptions, false);
   }

   public UpdateResult updateOne(ClientSession clientSession, Bson filter, Bson update) {
      return this.updateOne(clientSession, filter, update, new UpdateOptions());
   }

   public UpdateResult updateOne(ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeUpdate(clientSession, filter, update, updateOptions, false);
   }

   public UpdateResult updateOne(Bson filter, List<? extends Bson> update) {
      return this.updateOne(filter, update, new UpdateOptions());
   }

   public UpdateResult updateOne(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      return this.executeUpdate((ClientSession)null, filter, (List)update, updateOptions, false);
   }

   public UpdateResult updateOne(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
      return this.updateOne(clientSession, filter, update, new UpdateOptions());
   }

   public UpdateResult updateOne(ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeUpdate(clientSession, filter, update, updateOptions, false);
   }

   public UpdateResult updateMany(Bson filter, Bson update) {
      return this.updateMany(filter, update, new UpdateOptions());
   }

   public UpdateResult updateMany(Bson filter, Bson update, UpdateOptions updateOptions) {
      return this.executeUpdate((ClientSession)null, filter, (Bson)update, updateOptions, true);
   }

   public UpdateResult updateMany(ClientSession clientSession, Bson filter, Bson update) {
      return this.updateMany(clientSession, filter, update, new UpdateOptions());
   }

   public UpdateResult updateMany(ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeUpdate(clientSession, filter, update, updateOptions, true);
   }

   public UpdateResult updateMany(Bson filter, List<? extends Bson> update) {
      return this.updateMany(filter, update, new UpdateOptions());
   }

   public UpdateResult updateMany(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      return this.executeUpdate((ClientSession)null, filter, (List)update, updateOptions, true);
   }

   public UpdateResult updateMany(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
      return this.updateMany(clientSession, filter, update, new UpdateOptions());
   }

   public UpdateResult updateMany(ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeUpdate(clientSession, filter, update, updateOptions, true);
   }

   @Nullable
   public TDocument findOneAndDelete(Bson filter) {
      return this.findOneAndDelete(filter, new FindOneAndDeleteOptions());
   }

   @Nullable
   public TDocument findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
      return this.executeFindOneAndDelete((ClientSession)null, filter, options);
   }

   @Nullable
   public TDocument findOneAndDelete(ClientSession clientSession, Bson filter) {
      return this.findOneAndDelete(clientSession, filter, new FindOneAndDeleteOptions());
   }

   @Nullable
   public TDocument findOneAndDelete(ClientSession clientSession, Bson filter, FindOneAndDeleteOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeFindOneAndDelete(clientSession, filter, options);
   }

   @Nullable
   private TDocument executeFindOneAndDelete(@Nullable ClientSession clientSession, Bson filter, FindOneAndDeleteOptions options) {
      return this.executor.execute(this.operations.findOneAndDelete(filter, options), this.readConcern, clientSession);
   }

   @Nullable
   public TDocument findOneAndReplace(Bson filter, TDocument replacement) {
      return this.findOneAndReplace(filter, replacement, new FindOneAndReplaceOptions());
   }

   @Nullable
   public TDocument findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
      return this.executeFindOneAndReplace((ClientSession)null, filter, replacement, options);
   }

   @Nullable
   public TDocument findOneAndReplace(ClientSession clientSession, Bson filter, TDocument replacement) {
      return this.findOneAndReplace(clientSession, filter, replacement, new FindOneAndReplaceOptions());
   }

   @Nullable
   public TDocument findOneAndReplace(ClientSession clientSession, Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeFindOneAndReplace(clientSession, filter, replacement, options);
   }

   @Nullable
   private TDocument executeFindOneAndReplace(@Nullable ClientSession clientSession, Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
      return this.executor.execute(this.operations.findOneAndReplace(filter, replacement, options), this.readConcern, clientSession);
   }

   @Nullable
   public TDocument findOneAndUpdate(Bson filter, Bson update) {
      return this.findOneAndUpdate(filter, update, new FindOneAndUpdateOptions());
   }

   @Nullable
   public TDocument findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
      return this.executeFindOneAndUpdate((ClientSession)null, filter, (Bson)update, options);
   }

   @Nullable
   public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, Bson update) {
      return this.findOneAndUpdate(clientSession, filter, update, new FindOneAndUpdateOptions());
   }

   @Nullable
   public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, Bson update, FindOneAndUpdateOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeFindOneAndUpdate(clientSession, filter, update, options);
   }

   @Nullable
   private TDocument executeFindOneAndUpdate(@Nullable ClientSession clientSession, Bson filter, Bson update, FindOneAndUpdateOptions options) {
      return this.executor.execute(this.operations.findOneAndUpdate(filter, update, options), this.readConcern, clientSession);
   }

   @Nullable
   public TDocument findOneAndUpdate(Bson filter, List<? extends Bson> update) {
      return this.findOneAndUpdate(filter, update, new FindOneAndUpdateOptions());
   }

   @Nullable
   public TDocument findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
      return this.executeFindOneAndUpdate((ClientSession)null, filter, update, options);
   }

   @Nullable
   public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
      return this.findOneAndUpdate(clientSession, filter, update, new FindOneAndUpdateOptions());
   }

   @Nullable
   public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeFindOneAndUpdate(clientSession, filter, update, options);
   }

   @Nullable
   private TDocument executeFindOneAndUpdate(@Nullable ClientSession clientSession, Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
      return this.executor.execute(this.operations.findOneAndUpdate(filter, update, options), this.readConcern, clientSession);
   }

   public void drop() {
      this.executeDrop((ClientSession)null, new DropCollectionOptions());
   }

   public void drop(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      this.executeDrop(clientSession, new DropCollectionOptions());
   }

   public void drop(DropCollectionOptions dropCollectionOptions) {
      this.executeDrop((ClientSession)null, dropCollectionOptions);
   }

   public void drop(ClientSession clientSession, DropCollectionOptions dropCollectionOptions) {
      this.executeDrop(clientSession, dropCollectionOptions);
   }

   public String createSearchIndex(String indexName, Bson definition) {
      Assertions.notNull("indexName", indexName);
      Assertions.notNull("definition", definition);
      return (String)this.executeCreateSearchIndexes(Collections.singletonList(new SearchIndexModel(indexName, definition))).get(0);
   }

   public String createSearchIndex(Bson definition) {
      Assertions.notNull("definition", definition);
      return (String)this.executeCreateSearchIndexes(Collections.singletonList(new SearchIndexModel(definition))).get(0);
   }

   public List<String> createSearchIndexes(List<SearchIndexModel> searchIndexModels) {
      Assertions.notNullElements("searchIndexModels", searchIndexModels);
      return this.executeCreateSearchIndexes(searchIndexModels);
   }

   public void updateSearchIndex(String indexName, Bson definition) {
      Assertions.notNull("indexName", indexName);
      Assertions.notNull("definition", definition);
      this.executor.execute((WriteOperation)this.operations.updateSearchIndex(indexName, definition), (ReadConcern)this.readConcern, (ClientSession)null);
   }

   public void dropSearchIndex(String indexName) {
      Assertions.notNull("indexName", indexName);
      this.executor.execute((WriteOperation)this.operations.dropSearchIndex(indexName), (ReadConcern)this.readConcern, (ClientSession)null);
   }

   public ListSearchIndexesIterable<Document> listSearchIndexes() {
      return this.createListSearchIndexesIterable(Document.class);
   }

   public <TResult> ListSearchIndexesIterable<TResult> listSearchIndexes(Class<TResult> resultClass) {
      Assertions.notNull("resultClass", resultClass);
      return this.createListSearchIndexesIterable(resultClass);
   }

   private void executeDrop(@Nullable ClientSession clientSession, DropCollectionOptions dropCollectionOptions) {
      this.executor.execute(this.operations.dropCollection(dropCollectionOptions, this.autoEncryptionSettings), this.readConcern, clientSession);
   }

   public String createIndex(Bson keys) {
      return this.createIndex(keys, new IndexOptions());
   }

   public String createIndex(Bson keys, IndexOptions indexOptions) {
      return (String)this.createIndexes(Collections.singletonList(new IndexModel(keys, indexOptions))).get(0);
   }

   public String createIndex(ClientSession clientSession, Bson keys) {
      return this.createIndex(clientSession, keys, new IndexOptions());
   }

   public String createIndex(ClientSession clientSession, Bson keys, IndexOptions indexOptions) {
      return (String)this.createIndexes(clientSession, Collections.singletonList(new IndexModel(keys, indexOptions))).get(0);
   }

   public List<String> createIndexes(List<IndexModel> indexes) {
      return this.createIndexes(indexes, new CreateIndexOptions());
   }

   public List<String> createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
      return this.executeCreateIndexes((ClientSession)null, indexes, createIndexOptions);
   }

   public List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes) {
      return this.createIndexes(clientSession, indexes, new CreateIndexOptions());
   }

   public List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
      Assertions.notNull("clientSession", clientSession);
      return this.executeCreateIndexes(clientSession, indexes, createIndexOptions);
   }

   private List<String> executeCreateIndexes(@Nullable ClientSession clientSession, List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
      this.executor.execute(this.operations.createIndexes(indexes, createIndexOptions), this.readConcern, clientSession);
      return IndexHelper.getIndexNames(indexes, this.codecRegistry);
   }

   private List<String> executeCreateSearchIndexes(List<SearchIndexModel> searchIndexModels) {
      this.executor.execute((WriteOperation)this.operations.createSearchIndexes(searchIndexModels), (ReadConcern)this.readConcern, (ClientSession)null);
      return IndexHelper.getSearchIndexNames(searchIndexModels);
   }

   public ListIndexesIterable<Document> listIndexes() {
      return this.listIndexes(Document.class);
   }

   public <TResult> ListIndexesIterable<TResult> listIndexes(Class<TResult> resultClass) {
      return this.createListIndexesIterable((ClientSession)null, resultClass);
   }

   public ListIndexesIterable<Document> listIndexes(ClientSession clientSession) {
      return this.listIndexes(clientSession, Document.class);
   }

   public <TResult> ListIndexesIterable<TResult> listIndexes(ClientSession clientSession, Class<TResult> resultClass) {
      Assertions.notNull("clientSession", clientSession);
      return this.createListIndexesIterable(clientSession, resultClass);
   }

   private <TResult> ListIndexesIterable<TResult> createListIndexesIterable(@Nullable ClientSession clientSession, Class<TResult> resultClass) {
      return new ListIndexesIterableImpl(clientSession, this.getNamespace(), resultClass, this.codecRegistry, ReadPreference.primary(), this.executor, this.retryReads);
   }

   private <TResult> ListSearchIndexesIterable<TResult> createListSearchIndexesIterable(Class<TResult> resultClass) {
      return new ListSearchIndexesIterableImpl(this.getNamespace(), this.executor, this.readConcern, resultClass, this.codecRegistry, this.readPreference, this.retryReads);
   }

   public void dropIndex(String indexName) {
      this.dropIndex(indexName, new DropIndexOptions());
   }

   public void dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
      this.executeDropIndex((ClientSession)null, (String)indexName, dropIndexOptions);
   }

   public void dropIndex(Bson keys) {
      this.dropIndex(keys, new DropIndexOptions());
   }

   public void dropIndex(Bson keys, DropIndexOptions dropIndexOptions) {
      this.executeDropIndex((ClientSession)null, (Bson)keys, dropIndexOptions);
   }

   public void dropIndex(ClientSession clientSession, String indexName) {
      this.dropIndex(clientSession, indexName, new DropIndexOptions());
   }

   public void dropIndex(ClientSession clientSession, Bson keys) {
      this.dropIndex(clientSession, keys, new DropIndexOptions());
   }

   public void dropIndex(ClientSession clientSession, String indexName, DropIndexOptions dropIndexOptions) {
      Assertions.notNull("clientSession", clientSession);
      this.executeDropIndex(clientSession, indexName, dropIndexOptions);
   }

   public void dropIndex(ClientSession clientSession, Bson keys, DropIndexOptions dropIndexOptions) {
      Assertions.notNull("clientSession", clientSession);
      this.executeDropIndex(clientSession, keys, dropIndexOptions);
   }

   public void dropIndexes() {
      this.dropIndex("*");
   }

   public void dropIndexes(ClientSession clientSession) {
      Assertions.notNull("clientSession", clientSession);
      this.executeDropIndex(clientSession, "*", new DropIndexOptions());
   }

   public void dropIndexes(DropIndexOptions dropIndexOptions) {
      this.dropIndex("*", dropIndexOptions);
   }

   public void dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions) {
      this.dropIndex(clientSession, "*", dropIndexOptions);
   }

   private void executeDropIndex(@Nullable ClientSession clientSession, String indexName, DropIndexOptions dropIndexOptions) {
      Assertions.notNull("dropIndexOptions", dropIndexOptions);
      this.executor.execute(this.operations.dropIndex(indexName, dropIndexOptions), this.readConcern, clientSession);
   }

   private void executeDropIndex(@Nullable ClientSession clientSession, Bson keys, DropIndexOptions dropIndexOptions) {
      this.executor.execute(this.operations.dropIndex(keys, dropIndexOptions), this.readConcern, clientSession);
   }

   public void renameCollection(MongoNamespace newCollectionNamespace) {
      this.renameCollection(newCollectionNamespace, new RenameCollectionOptions());
   }

   public void renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
      this.executeRenameCollection((ClientSession)null, newCollectionNamespace, renameCollectionOptions);
   }

   public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace) {
      this.renameCollection(clientSession, newCollectionNamespace, new RenameCollectionOptions());
   }

   public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
      Assertions.notNull("clientSession", clientSession);
      this.executeRenameCollection(clientSession, newCollectionNamespace, renameCollectionOptions);
   }

   private void executeRenameCollection(@Nullable ClientSession clientSession, MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
      this.executor.execute((WriteOperation)(new RenameCollectionOperation(this.getNamespace(), newCollectionNamespace, this.writeConcern)).dropTarget(renameCollectionOptions.isDropTarget()), (ReadConcern)this.readConcern, (ClientSession)clientSession);
   }

   private DeleteResult executeDelete(@Nullable ClientSession clientSession, Bson filter, DeleteOptions deleteOptions, boolean multi) {
      BulkWriteResult result = this.executeSingleWriteRequest(clientSession, multi ? this.operations.deleteMany(filter, deleteOptions) : this.operations.deleteOne(filter, deleteOptions), WriteRequest.Type.DELETE);
      return result.wasAcknowledged() ? DeleteResult.acknowledged((long)result.getDeletedCount()) : DeleteResult.unacknowledged();
   }

   private UpdateResult executeUpdate(@Nullable ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions, boolean multi) {
      return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, multi ? this.operations.updateMany(filter, update, updateOptions) : this.operations.updateOne(filter, update, updateOptions), WriteRequest.Type.UPDATE));
   }

   private UpdateResult executeUpdate(@Nullable ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions, boolean multi) {
      return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, multi ? this.operations.updateMany(filter, update, updateOptions) : this.operations.updateOne(filter, update, updateOptions), WriteRequest.Type.UPDATE));
   }

   private BulkWriteResult executeSingleWriteRequest(@Nullable ClientSession clientSession, WriteOperation<BulkWriteResult> writeOperation, WriteRequest.Type type) {
      try {
         return (BulkWriteResult)this.executor.execute(writeOperation, this.readConcern, clientSession);
      } catch (MongoBulkWriteException e) {
         MongoException exception;
         if (e.getWriteErrors().isEmpty()) {
            exception = new MongoWriteConcernException(e.getWriteConcernError(), this.translateBulkWriteResult(type, e.getWriteResult()), e.getServerAddress());
         } else {
            exception = new MongoWriteException(new WriteError((WriteError)e.getWriteErrors().get(0)), e.getServerAddress());
         }

         for (String errorLabel : e.getErrorLabels()) {
            exception.addLabel(errorLabel);
         }

         throw exception;
      }
   }

   private WriteConcernResult translateBulkWriteResult(WriteRequest.Type type, BulkWriteResult writeResult) {
      switch(type) {
      case INSERT:
         return WriteConcernResult.acknowledged(writeResult.getInsertedCount(), false, (BsonValue)null);
      case DELETE:
         return WriteConcernResult.acknowledged(writeResult.getDeletedCount(), false, (BsonValue)null);
      case UPDATE:
      case REPLACE:
         return WriteConcernResult.acknowledged(writeResult.getMatchedCount() + writeResult.getUpserts().size(), writeResult.getMatchedCount() > 0, writeResult.getUpserts().isEmpty() ? null : ((BulkWriteUpsert)writeResult.getUpserts().get(0)).getId());
      default:
         throw new MongoInternalException("Unhandled write request type: " + type);
      }
   }

   private InsertOneResult toInsertOneResult(BulkWriteResult result) {
      if (result.wasAcknowledged()) {
         BsonValue insertedId = result.getInserts().isEmpty() ? null : ((BulkWriteInsert)result.getInserts().get(0)).getId();
         return InsertOneResult.acknowledged(insertedId);
      } else {
         return InsertOneResult.unacknowledged();
      }
   }

   private InsertManyResult toInsertManyResult(BulkWriteResult result) {
      return result.wasAcknowledged() ? InsertManyResult.acknowledged((Map)result.getInserts().stream().collect(HashMap::new, (m, v) -> {
         m.put(v.getIndex(), v.getId());
      }, HashMap::putAll)) : InsertManyResult.unacknowledged();
   }

   private UpdateResult toUpdateResult(BulkWriteResult result) {
      if (result.wasAcknowledged()) {
         BsonValue upsertedId = result.getUpserts().isEmpty() ? null : ((BulkWriteUpsert)result.getUpserts().get(0)).getId();
         return UpdateResult.acknowledged((long)result.getMatchedCount(), (long)result.getModifiedCount(), upsertedId);
      } else {
         return UpdateResult.unacknowledged();
      }
   }
}
