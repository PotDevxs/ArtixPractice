package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.AutoEncryptionSettings;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.model.BulkWriteOptions;
import dev.artixdev.libs.com.mongodb.client.model.ClusteredIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.Collation;
import dev.artixdev.libs.com.mongodb.client.model.CountOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateViewOptions;
import dev.artixdev.libs.com.mongodb.client.model.DeleteManyModel;
import dev.artixdev.libs.com.mongodb.client.model.DeleteOneModel;
import dev.artixdev.libs.com.mongodb.client.model.DeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.DropIndexOptions;
import dev.artixdev.libs.com.mongodb.client.model.EstimatedDocumentCountOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndDeleteOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.FindOneAndUpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.IndexModel;
import dev.artixdev.libs.com.mongodb.client.model.IndexOptionDefaults;
import dev.artixdev.libs.com.mongodb.client.model.InsertManyOptions;
import dev.artixdev.libs.com.mongodb.client.model.InsertOneModel;
import dev.artixdev.libs.com.mongodb.client.model.InsertOneOptions;
import dev.artixdev.libs.com.mongodb.client.model.MapReduceAction;
import dev.artixdev.libs.com.mongodb.client.model.RenameCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOneModel;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOptions;
import dev.artixdev.libs.com.mongodb.client.model.ReturnDocument;
import dev.artixdev.libs.com.mongodb.client.model.SearchIndexModel;
import dev.artixdev.libs.com.mongodb.client.model.UpdateManyModel;
import dev.artixdev.libs.com.mongodb.client.model.UpdateOneModel;
import dev.artixdev.libs.com.mongodb.client.model.UpdateOptions;
import dev.artixdev.libs.com.mongodb.client.model.ValidationOptions;
import dev.artixdev.libs.com.mongodb.client.model.WriteModel;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocument;
import dev.artixdev.libs.com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import dev.artixdev.libs.com.mongodb.internal.bulk.DeleteRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.IndexRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.InsertRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.UpdateRequest;
import dev.artixdev.libs.com.mongodb.internal.bulk.WriteRequest;
import dev.artixdev.libs.com.mongodb.internal.client.model.AggregationLevel;
import dev.artixdev.libs.com.mongodb.internal.client.model.FindOptions;
import dev.artixdev.libs.com.mongodb.internal.client.model.changestream.ChangeStreamLevel;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.CollectibleCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

final class Operations<TDocument> {
   private final MongoNamespace namespace;
   private final Class<TDocument> documentClass;
   private final ReadPreference readPreference;
   private final CodecRegistry codecRegistry;
   private final ReadConcern readConcern;
   private final WriteConcern writeConcern;
   private final boolean retryWrites;
   private final boolean retryReads;

   Operations(@Nullable MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, ReadConcern readConcern, WriteConcern writeConcern, boolean retryWrites, boolean retryReads) {
      this.namespace = namespace;
      this.documentClass = documentClass;
      this.readPreference = readPreference;
      this.codecRegistry = codecRegistry;
      this.readConcern = readConcern;
      this.writeConcern = writeConcern;
      this.retryWrites = retryWrites;
      this.retryReads = retryReads;
   }

   @Nullable
   MongoNamespace getNamespace() {
      return this.namespace;
   }

   Class<TDocument> getDocumentClass() {
      return this.documentClass;
   }

   ReadPreference getReadPreference() {
      return this.readPreference;
   }

   CodecRegistry getCodecRegistry() {
      return this.codecRegistry;
   }

   ReadConcern getReadConcern() {
      return this.readConcern;
   }

   WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   boolean isRetryWrites() {
      return this.retryWrites;
   }

   boolean isRetryReads() {
      return this.retryReads;
   }

   CountDocumentsOperation countDocuments(Bson filter, CountOptions options) {
      CountDocumentsOperation operation = (new CountDocumentsOperation((MongoNamespace)Assertions.assertNotNull(this.namespace))).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).skip((long)options.getSkip()).limit((long)options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation()).comment(options.getComment());
      if (options.getHint() != null) {
         operation.hint(this.toBsonDocument(options.getHint()));
      } else if (options.getHintString() != null) {
         operation.hint(new BsonString(options.getHintString()));
      }

      return operation;
   }

   EstimatedDocumentCountOperation estimatedDocumentCount(EstimatedDocumentCountOptions options) {
      return (new EstimatedDocumentCountOperation((MongoNamespace)Assertions.assertNotNull(this.namespace))).retryReads(this.retryReads).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).comment(options.getComment());
   }

   <TResult> FindOperation<TResult> findFirst(Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.createFindOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), filter, resultClass, options).batchSize(0).limit(-1);
   }

   <TResult> FindOperation<TResult> find(Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.createFindOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), filter, resultClass, options);
   }

   <TResult> FindOperation<TResult> find(MongoNamespace findNamespace, @Nullable Bson filter, Class<TResult> resultClass, FindOptions options) {
      return this.createFindOperation(findNamespace, filter, resultClass, options);
   }

   private <TResult> FindOperation<TResult> createFindOperation(MongoNamespace findNamespace, @Nullable Bson filter, Class<TResult> resultClass, FindOptions options) {
      FindOperation<TResult> operation = (new FindOperation(findNamespace, this.codecRegistry.get(resultClass))).retryReads(this.retryReads).filter(filter == null ? new BsonDocument() : filter.toBsonDocument(this.documentClass, this.codecRegistry)).batchSize(options.getBatchSize()).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).maxAwaitTime(options.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).cursorType(options.getCursorType()).noCursorTimeout(options.isNoCursorTimeout()).oplogReplay(options.isOplogReplay()).partial(options.isPartial()).collation(options.getCollation()).comment(options.getComment()).let(this.toBsonDocument(options.getLet())).min(this.toBsonDocument(options.getMin())).max(this.toBsonDocument(options.getMax())).returnKey(options.isReturnKey()).showRecordId(options.isShowRecordId()).allowDiskUse(options.isAllowDiskUse());
      if (options.getHint() != null) {
         operation.hint(this.toBsonDocument(options.getHint()));
      } else if (options.getHintString() != null) {
         operation.hint(new BsonString(options.getHintString()));
      }

      return operation;
   }

   <TResult> DistinctOperation<TResult> distinct(String fieldName, @Nullable Bson filter, Class<TResult> resultClass, long maxTimeMS, Collation collation, BsonValue comment) {
      return (new DistinctOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), fieldName, this.codecRegistry.get(resultClass))).retryReads(this.retryReads).filter(filter == null ? null : filter.toBsonDocument(this.documentClass, this.codecRegistry)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).collation(collation).comment(comment);
   }

   <TResult> AggregateOperation<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass, long maxTimeMS, long maxAwaitTimeMS, @Nullable Integer batchSize, Collation collation, @Nullable Bson hint, @Nullable String hintString, BsonValue comment, Bson variables, Boolean allowDiskUse, AggregationLevel aggregationLevel) {
      return (new AggregateOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), (List)Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.codecRegistry.get(resultClass), aggregationLevel)).retryReads(this.retryReads).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).maxAwaitTime(maxAwaitTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).batchSize(batchSize).collation(collation).hint((BsonValue)(hint != null ? this.toBsonDocument(hint) : (hintString != null ? new BsonString(hintString) : null))).comment(comment).let(this.toBsonDocument(variables));
   }

   AggregateToCollectionOperation aggregateToCollection(List<? extends Bson> pipeline, long maxTimeMS, Boolean allowDiskUse, Boolean bypassDocumentValidation, Collation collation, @Nullable Bson hint, @Nullable String hintString, BsonValue comment, Bson variables, AggregationLevel aggregationLevel) {
      return (new AggregateToCollectionOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), (List)Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.readConcern, this.writeConcern, aggregationLevel)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).bypassDocumentValidation(bypassDocumentValidation).collation(collation).hint((BsonValue)(hint != null ? this.toBsonDocument(hint) : (hintString != null ? new BsonString(hintString) : null))).comment(comment).let(this.toBsonDocument(variables));
   }

   MapReduceToCollectionOperation mapReduceToCollection(String databaseName, String collectionName, String mapFunction, String reduceFunction, @Nullable String finalizeFunction, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, MapReduceAction action, boolean nonAtomic, boolean sharded, Boolean bypassDocumentValidation, Collation collation) {
      MapReduceToCollectionOperation operation = (new MapReduceToCollectionOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), collectionName, this.writeConcern)).filter(this.toBsonDocument(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocument(scope)).sort(this.toBsonDocument(sort)).verbose(verbose).action(action.getValue()).nonAtomic(nonAtomic).sharded(sharded).databaseName(databaseName).bypassDocumentValidation(bypassDocumentValidation).collation(collation);
      if (finalizeFunction != null) {
         operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
      }

      return operation;
   }

   <TResult> MapReduceWithInlineResultsOperation<TResult> mapReduce(String mapFunction, String reduceFunction, @Nullable String finalizeFunction, Class<TResult> resultClass, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, Collation collation) {
      MapReduceWithInlineResultsOperation<TResult> operation = (new MapReduceWithInlineResultsOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), this.codecRegistry.get(resultClass))).filter(this.toBsonDocument(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocument(scope)).sort(this.toBsonDocument(sort)).verbose(verbose).collation(collation);
      if (finalizeFunction != null) {
         operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
      }

      return operation;
   }

   FindAndDeleteOperation<TDocument> findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
      return (new FindAndDeleteOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec())).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation()).hint(options.getHint()).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
   }

   FindAndReplaceOperation<TDocument> findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
      return (new FindAndReplaceOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), this.documentToBsonDocument(replacement))).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).hint(options.getHint()).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
   }

   FindAndUpdateOperation<TDocument> findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
      return (new FindAndUpdateOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), (BsonDocument)Assertions.assertNotNull(this.toBsonDocument(update)))).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters())).hint(options.getHint()).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
   }

   FindAndUpdateOperation<TDocument> findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
      return (new FindAndUpdateOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.writeConcern, this.retryWrites, this.getCodec(), (List)Assertions.assertNotNull(this.toBsonDocumentList(update)))).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters())).hint(options.getHint()).hintString(options.getHintString()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
   }

   MixedBulkWriteOperation insertOne(TDocument document, InsertOneOptions options) {
      return this.bulkWrite(Collections.singletonList(new InsertOneModel<TDocument>(document)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()));
   }

   MixedBulkWriteOperation replaceOne(Bson filter, TDocument replacement, ReplaceOptions options) {
      return this.bulkWrite(Collections.singletonList(new ReplaceOneModel<TDocument>(filter, replacement, options)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation deleteOne(Bson filter, DeleteOptions options) {
      return this.bulkWrite(Collections.singletonList(new DeleteOneModel<TDocument>(filter, options)), (new BulkWriteOptions()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation deleteMany(Bson filter, DeleteOptions options) {
      return this.bulkWrite(Collections.singletonList(new DeleteManyModel<TDocument>(filter, options)), (new BulkWriteOptions()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation updateOne(Bson filter, Bson update, UpdateOptions options) {
      return this.bulkWrite(Collections.singletonList(new UpdateOneModel<TDocument>(filter, update, options)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation updateOne(Bson filter, List<? extends Bson> update, UpdateOptions options) {
      return this.bulkWrite(Collections.singletonList(new UpdateOneModel<TDocument>(filter, update, options)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation updateMany(Bson filter, Bson update, UpdateOptions options) {
      return this.bulkWrite(Collections.singletonList(new UpdateManyModel<TDocument>(filter, update, options)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation updateMany(Bson filter, List<? extends Bson> update, UpdateOptions options) {
      return this.bulkWrite(Collections.singletonList(new UpdateManyModel<TDocument>(filter, update, options)), (new BulkWriteOptions()).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(options.getLet()));
   }

   MixedBulkWriteOperation insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
      Assertions.notNull("documents", documents);
      List<InsertRequest> requests = new ArrayList(documents.size());

      for (TDocument document : documents) {
         if (document == null) {
            throw new IllegalArgumentException("documents can not contain a null value");
         }
         if (this.getCodec() instanceof CollectibleCodec) {
            document = ((CollectibleCodec<TDocument>)this.getCodec()).generateIdIfAbsentFromDocument(document);
         }
         requests.add(new InsertRequest(this.documentToBsonDocument(document)));
      }

      return (new MixedBulkWriteOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), requests, options.isOrdered(), this.writeConcern, this.retryWrites)).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment());
   }

   MixedBulkWriteOperation bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
      Assertions.notNull("requests", requests);
      List<WriteRequest> writeRequests = new ArrayList(requests.size());

      WriteRequest writeRequest;
      for (Iterator<? extends WriteModel<? extends TDocument>> var4 = requests.iterator(); var4.hasNext(); writeRequests.add(writeRequest)) {
         WriteModel<? extends TDocument> writeModel = var4.next();
         if (writeModel == null) {
            throw new IllegalArgumentException("requests can not contain a null value");
         }

         if (writeModel instanceof InsertOneModel) {
            TDocument document = ((InsertOneModel<TDocument>)writeModel).getDocument();
            if (this.getCodec() instanceof CollectibleCodec) {
               document = ((CollectibleCodec<TDocument>)this.getCodec()).generateIdIfAbsentFromDocument(document);
            }

            writeRequest = new InsertRequest(this.documentToBsonDocument(document));
         } else if (writeModel instanceof ReplaceOneModel) {
            ReplaceOneModel<TDocument> replaceOneModel = (ReplaceOneModel)writeModel;
            writeRequest = (new UpdateRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(replaceOneModel.getFilter())), this.documentToBsonDocument(replaceOneModel.getReplacement()), WriteRequest.Type.REPLACE)).upsert(replaceOneModel.getReplaceOptions().isUpsert()).collation(replaceOneModel.getReplaceOptions().getCollation()).hint(replaceOneModel.getReplaceOptions().getHint()).hintString(replaceOneModel.getReplaceOptions().getHintString());
         } else {
            Object update;
            if (writeModel instanceof UpdateOneModel) {
               UpdateOneModel<TDocument> updateOneModel = (UpdateOneModel)writeModel;
               update = updateOneModel.getUpdate() != null ? this.toBsonDocument(updateOneModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateOneModel.getUpdatePipeline()));
               writeRequest = (new UpdateRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(updateOneModel.getFilter())), (BsonValue)update, WriteRequest.Type.UPDATE)).multi(false).upsert(updateOneModel.getOptions().isUpsert()).collation(updateOneModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateOneModel.getOptions().getArrayFilters())).hint(updateOneModel.getOptions().getHint()).hintString(updateOneModel.getOptions().getHintString());
            } else if (writeModel instanceof UpdateManyModel) {
               UpdateManyModel<TDocument> updateManyModel = (UpdateManyModel)writeModel;
               update = updateManyModel.getUpdate() != null ? this.toBsonDocument(updateManyModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateManyModel.getUpdatePipeline()));
               writeRequest = (new UpdateRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(updateManyModel.getFilter())), (BsonValue)update, WriteRequest.Type.UPDATE)).multi(true).upsert(updateManyModel.getOptions().isUpsert()).collation(updateManyModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateManyModel.getOptions().getArrayFilters())).hint(updateManyModel.getOptions().getHint()).hintString(updateManyModel.getOptions().getHintString());
            } else if (writeModel instanceof DeleteOneModel) {
               DeleteOneModel<TDocument> deleteOneModel = (DeleteOneModel)writeModel;
               writeRequest = (new DeleteRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(deleteOneModel.getFilter())))).multi(false).collation(deleteOneModel.getOptions().getCollation()).hint(deleteOneModel.getOptions().getHint()).hintString(deleteOneModel.getOptions().getHintString());
            } else {
               if (!(writeModel instanceof DeleteManyModel)) {
                  throw new UnsupportedOperationException(String.format("WriteModel of type %s is not supported", writeModel.getClass()));
               }

               DeleteManyModel<TDocument> deleteManyModel = (DeleteManyModel)writeModel;
               writeRequest = (new DeleteRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(deleteManyModel.getFilter())))).multi(true).collation(deleteManyModel.getOptions().getCollation()).hint(deleteManyModel.getOptions().getHint()).hintString(deleteManyModel.getOptions().getHintString());
            }
         }
      }

      return (new MixedBulkWriteOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), writeRequests, options.isOrdered(), this.writeConcern, this.retryWrites)).bypassDocumentValidation(options.getBypassDocumentValidation()).comment(options.getComment()).let(this.toBsonDocument(options.getLet()));
   }

   <TResult> CommandReadOperation<TResult> commandRead(Bson command, Class<TResult> resultClass) {
      Assertions.notNull("command", command);
      Assertions.notNull("resultClass", resultClass);
      return new CommandReadOperation(((MongoNamespace)Assertions.assertNotNull(this.namespace)).getDatabaseName(), (BsonDocument)Assertions.assertNotNull(this.toBsonDocument(command)), this.codecRegistry.get(resultClass));
   }

   DropDatabaseOperation dropDatabase() {
      return new DropDatabaseOperation(((MongoNamespace)Assertions.assertNotNull(this.namespace)).getDatabaseName(), this.getWriteConcern());
   }

   CreateCollectionOperation createCollection(String collectionName, CreateCollectionOptions createCollectionOptions, @Nullable AutoEncryptionSettings autoEncryptionSettings) {
      CreateCollectionOperation operation = (new CreateCollectionOperation(((MongoNamespace)Assertions.assertNotNull(this.namespace)).getDatabaseName(), collectionName, this.writeConcern)).collation(createCollectionOptions.getCollation()).capped(createCollectionOptions.isCapped()).sizeInBytes(createCollectionOptions.getSizeInBytes()).maxDocuments(createCollectionOptions.getMaxDocuments()).storageEngineOptions(this.toBsonDocument(createCollectionOptions.getStorageEngineOptions())).expireAfter(createCollectionOptions.getExpireAfter(TimeUnit.SECONDS)).timeSeriesOptions(createCollectionOptions.getTimeSeriesOptions()).changeStreamPreAndPostImagesOptions(createCollectionOptions.getChangeStreamPreAndPostImagesOptions());
      ClusteredIndexOptions clusteredIndexOptions = createCollectionOptions.getClusteredIndexOptions();
      if (clusteredIndexOptions != null) {
         operation.clusteredIndexKey(this.toBsonDocument(clusteredIndexOptions.getKey()));
         operation.clusteredIndexUnique(clusteredIndexOptions.isUnique());
         operation.clusteredIndexName(clusteredIndexOptions.getName());
      }

      Bson encryptedFields = createCollectionOptions.getEncryptedFields();
      operation.encryptedFields(this.toBsonDocument(encryptedFields));
      if (encryptedFields == null && autoEncryptionSettings != null) {
         Map<String, BsonDocument> encryptedFieldsMap = autoEncryptionSettings.getEncryptedFieldsMap();
         if (encryptedFieldsMap != null) {
            operation.encryptedFields((BsonDocument)encryptedFieldsMap.getOrDefault(this.namespace.getDatabaseName() + "." + collectionName, (BsonDocument)null));
         }
      }

      IndexOptionDefaults indexOptionDefaults = createCollectionOptions.getIndexOptionDefaults();
      Bson storageEngine = indexOptionDefaults.getStorageEngine();
      if (storageEngine != null) {
         operation.indexOptionDefaults(new BsonDocument("storageEngine", this.toBsonDocument(storageEngine)));
      }

      ValidationOptions validationOptions = createCollectionOptions.getValidationOptions();
      Bson validator = validationOptions.getValidator();
      operation.validator(this.toBsonDocument(validator));
      operation.validationLevel(validationOptions.getValidationLevel());
      operation.validationAction(validationOptions.getValidationAction());
      return operation;
   }

   DropCollectionOperation dropCollection(DropCollectionOptions dropCollectionOptions, @Nullable AutoEncryptionSettings autoEncryptionSettings) {
      DropCollectionOperation operation = new DropCollectionOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.writeConcern);
      Bson encryptedFields = dropCollectionOptions.getEncryptedFields();
      if (encryptedFields != null) {
         operation.encryptedFields((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(encryptedFields)));
      } else if (autoEncryptionSettings != null) {
         Map<String, BsonDocument> encryptedFieldsMap = autoEncryptionSettings.getEncryptedFieldsMap();
         if (encryptedFieldsMap != null) {
            operation.encryptedFields((BsonDocument)encryptedFieldsMap.getOrDefault(this.namespace.getFullName(), (BsonDocument)null));
            operation.autoEncryptedFields(true);
         }
      }

      return operation;
   }

   RenameCollectionOperation renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
      return (new RenameCollectionOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), newCollectionNamespace, this.writeConcern)).dropTarget(renameCollectionOptions.isDropTarget());
   }

   CreateViewOperation createView(String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
      Assertions.notNull("options", createViewOptions);
      Assertions.notNull("pipeline", pipeline);
      return (new CreateViewOperation(((MongoNamespace)Assertions.assertNotNull(this.namespace)).getDatabaseName(), viewName, viewOn, (List)Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), this.writeConcern)).collation(createViewOptions.getCollation());
   }

   CreateIndexesOperation createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
      Assertions.notNull("indexes", indexes);
      Assertions.notNull("createIndexOptions", createIndexOptions);
      List<IndexRequest> indexRequests = new ArrayList(indexes.size());
      Iterator var4 = indexes.iterator();

      while(var4.hasNext()) {
         IndexModel model = (IndexModel)var4.next();
         if (model == null) {
            throw new IllegalArgumentException("indexes can not contain a null value");
         }

         indexRequests.add((new IndexRequest((BsonDocument)Assertions.assertNotNull(this.toBsonDocument(model.getKeys())))).name(model.getOptions().getName()).background(model.getOptions().isBackground()).unique(model.getOptions().isUnique()).sparse(model.getOptions().isSparse()).expireAfter(model.getOptions().getExpireAfter(TimeUnit.SECONDS), TimeUnit.SECONDS).version(model.getOptions().getVersion()).weights(this.toBsonDocument(model.getOptions().getWeights())).defaultLanguage(model.getOptions().getDefaultLanguage()).languageOverride(model.getOptions().getLanguageOverride()).textVersion(model.getOptions().getTextVersion()).sphereVersion(model.getOptions().getSphereVersion()).bits(model.getOptions().getBits()).min(model.getOptions().getMin()).max(model.getOptions().getMax()).bucketSize(model.getOptions().getBucketSize()).storageEngine(this.toBsonDocument(model.getOptions().getStorageEngine())).partialFilterExpression(this.toBsonDocument(model.getOptions().getPartialFilterExpression())).collation(model.getOptions().getCollation()).wildcardProjection(this.toBsonDocument(model.getOptions().getWildcardProjection())).hidden(model.getOptions().isHidden()));
      }

      return (new CreateIndexesOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), indexRequests, this.writeConcern)).maxTime(createIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).commitQuorum(createIndexOptions.getCommitQuorum());
   }

   CreateSearchIndexesOperation createSearchIndexes(List<SearchIndexModel> indexes) {
      List<SearchIndexRequest> indexRequests = (List)indexes.stream().map(this::createSearchIndexRequest).collect(Collectors.toList());
      return new CreateSearchIndexesOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), indexRequests, this.writeConcern);
   }

   UpdateSearchIndexesOperation updateSearchIndex(String indexName, Bson definition) {
      BsonDocument definitionDocument = (BsonDocument)Assertions.assertNotNull(this.toBsonDocument(definition));
      SearchIndexRequest searchIndexRequest = new SearchIndexRequest(definitionDocument, indexName);
      return new UpdateSearchIndexesOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), searchIndexRequest, this.writeConcern);
   }

   DropSearchIndexOperation dropSearchIndex(String indexName) {
      return new DropSearchIndexOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), indexName, this.writeConcern);
   }

   <TResult> ListSearchIndexesOperation<TResult> listSearchIndexes(Class<TResult> resultClass, long maxTimeMS, @Nullable String indexName, @Nullable Integer batchSize, @Nullable Collation collation, @Nullable BsonValue comment, @Nullable Boolean allowDiskUse) {
      return new ListSearchIndexesOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.codecRegistry.get(resultClass), maxTimeMS, indexName, batchSize, collation, comment, allowDiskUse, this.retryReads);
   }

   DropIndexOperation dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
      return (new DropIndexOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), indexName, this.writeConcern)).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
   }

   DropIndexOperation dropIndex(Bson keys, DropIndexOptions dropIndexOptions) {
      return (new DropIndexOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), keys.toBsonDocument(BsonDocument.class, this.codecRegistry), this.writeConcern)).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
   }

   <TResult> ListCollectionsOperation<TResult> listCollections(String databaseName, Class<TResult> resultClass, Bson filter, boolean collectionNamesOnly, @Nullable Integer batchSize, long maxTimeMS, BsonValue comment) {
      return (new ListCollectionsOperation(databaseName, this.codecRegistry.get(resultClass))).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).nameOnly(collectionNamesOnly).batchSize(batchSize == null ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).comment(comment);
   }

   <TResult> ListDatabasesOperation<TResult> listDatabases(Class<TResult> resultClass, Bson filter, Boolean nameOnly, long maxTimeMS, Boolean authorizedDatabasesOnly, BsonValue comment) {
      return (new ListDatabasesOperation(this.codecRegistry.get(resultClass))).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).nameOnly(nameOnly).authorizedDatabasesOnly(authorizedDatabasesOnly).comment(comment);
   }

   <TResult> ListIndexesOperation<TResult> listIndexes(Class<TResult> resultClass, @Nullable Integer batchSize, long maxTimeMS, BsonValue comment) {
      return (new ListIndexesOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), this.codecRegistry.get(resultClass))).retryReads(this.retryReads).batchSize(batchSize == null ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).comment(comment);
   }

   <TResult> ChangeStreamOperation<TResult> changeStream(FullDocument fullDocument, FullDocumentBeforeChange fullDocumentBeforeChange, List<? extends Bson> pipeline, Decoder<TResult> decoder, ChangeStreamLevel changeStreamLevel, @Nullable Integer batchSize, Collation collation, BsonValue comment, long maxAwaitTimeMS, BsonDocument resumeToken, BsonTimestamp startAtOperationTime, BsonDocument startAfter, boolean showExpandedEvents) {
      return (new ChangeStreamOperation((MongoNamespace)Assertions.assertNotNull(this.namespace), fullDocument, fullDocumentBeforeChange, (List)Assertions.assertNotNull(this.toBsonDocumentList(pipeline)), decoder, changeStreamLevel)).batchSize(batchSize).collation(collation).comment(comment).maxAwaitTime(maxAwaitTimeMS, TimeUnit.MILLISECONDS).resumeAfter(resumeToken).startAtOperationTime(startAtOperationTime).startAfter(startAfter).showExpandedEvents(showExpandedEvents).retryReads(this.retryReads);
   }

   private Codec<TDocument> getCodec() {
      return this.codecRegistry.get(this.documentClass);
   }

   private BsonDocument documentToBsonDocument(TDocument document) {
      return BsonDocumentWrapper.asBsonDocument(document, this.codecRegistry);
   }

   @Nullable
   private BsonDocument toBsonDocument(@Nullable Bson bson) {
      return bson == null ? null : bson.toBsonDocument(this.documentClass, this.codecRegistry);
   }

   @Nullable
   private List<BsonDocument> toBsonDocumentList(@Nullable List<? extends Bson> bsonList) {
      if (bsonList == null) {
         return null;
      } else {
         List<BsonDocument> bsonDocumentList = new ArrayList(bsonList.size());
         Iterator var3 = bsonList.iterator();

         while(var3.hasNext()) {
            Bson cur = (Bson)var3.next();
            if (cur == null) {
               throw new IllegalArgumentException("All documents in the list must be non-null");
            }

            bsonDocumentList.add(this.toBsonDocument(cur));
         }

         return bsonDocumentList;
      }
   }

   private SearchIndexRequest createSearchIndexRequest(SearchIndexModel model) {
      BsonDocument definition = (BsonDocument)Assertions.assertNotNull(this.toBsonDocument(model.getDefinition()));
      String indexName = model.getName();
      SearchIndexRequest indexRequest = new SearchIndexRequest(definition, indexName);
      return indexRequest;
   }
}
