package dev.artixdev.libs.com.mongodb.client;

import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
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
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

@ThreadSafe
public interface MongoCollection<TDocument> {
   MongoNamespace getNamespace();

   Class<TDocument> getDocumentClass();

   CodecRegistry getCodecRegistry();

   ReadPreference getReadPreference();

   WriteConcern getWriteConcern();

   ReadConcern getReadConcern();

   <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(Class<NewTDocument> var1);

   MongoCollection<TDocument> withCodecRegistry(CodecRegistry var1);

   MongoCollection<TDocument> withReadPreference(ReadPreference var1);

   MongoCollection<TDocument> withWriteConcern(WriteConcern var1);

   MongoCollection<TDocument> withReadConcern(ReadConcern var1);

   long countDocuments();

   long countDocuments(Bson var1);

   long countDocuments(Bson var1, CountOptions var2);

   long countDocuments(ClientSession var1);

   long countDocuments(ClientSession var1, Bson var2);

   long countDocuments(ClientSession var1, Bson var2, CountOptions var3);

   long estimatedDocumentCount();

   long estimatedDocumentCount(EstimatedDocumentCountOptions var1);

   <TResult> DistinctIterable<TResult> distinct(String var1, Class<TResult> var2);

   <TResult> DistinctIterable<TResult> distinct(String var1, Bson var2, Class<TResult> var3);

   <TResult> DistinctIterable<TResult> distinct(ClientSession var1, String var2, Class<TResult> var3);

   <TResult> DistinctIterable<TResult> distinct(ClientSession var1, String var2, Bson var3, Class<TResult> var4);

   FindIterable<TDocument> find();

   <TResult> FindIterable<TResult> find(Class<TResult> var1);

   FindIterable<TDocument> find(Bson var1);

   <TResult> FindIterable<TResult> find(Bson var1, Class<TResult> var2);

   FindIterable<TDocument> find(ClientSession var1);

   <TResult> FindIterable<TResult> find(ClientSession var1, Class<TResult> var2);

   FindIterable<TDocument> find(ClientSession var1, Bson var2);

   <TResult> FindIterable<TResult> find(ClientSession var1, Bson var2, Class<TResult> var3);

   AggregateIterable<TDocument> aggregate(List<? extends Bson> var1);

   <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> var1, Class<TResult> var2);

   AggregateIterable<TDocument> aggregate(ClientSession var1, List<? extends Bson> var2);

   <TResult> AggregateIterable<TResult> aggregate(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

   ChangeStreamIterable<TDocument> watch();

   <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> var1);

   ChangeStreamIterable<TDocument> watch(List<? extends Bson> var1);

   <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> var1, Class<TResult> var2);

   ChangeStreamIterable<TDocument> watch(ClientSession var1);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, Class<TResult> var2);

   ChangeStreamIterable<TDocument> watch(ClientSession var1, List<? extends Bson> var2);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

   /** @deprecated */
   @Deprecated
   MapReduceIterable<TDocument> mapReduce(String var1, String var2);

   /** @deprecated */
   @Deprecated
   <TResult> MapReduceIterable<TResult> mapReduce(String var1, String var2, Class<TResult> var3);

   /** @deprecated */
   @Deprecated
   MapReduceIterable<TDocument> mapReduce(ClientSession var1, String var2, String var3);

   /** @deprecated */
   @Deprecated
   <TResult> MapReduceIterable<TResult> mapReduce(ClientSession var1, String var2, String var3, Class<TResult> var4);

   BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> var1);

   BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> var1, BulkWriteOptions var2);

   BulkWriteResult bulkWrite(ClientSession var1, List<? extends WriteModel<? extends TDocument>> var2);

   BulkWriteResult bulkWrite(ClientSession var1, List<? extends WriteModel<? extends TDocument>> var2, BulkWriteOptions var3);

   InsertOneResult insertOne(TDocument var1);

   InsertOneResult insertOne(TDocument var1, InsertOneOptions var2);

   InsertOneResult insertOne(ClientSession var1, TDocument var2);

   InsertOneResult insertOne(ClientSession var1, TDocument var2, InsertOneOptions var3);

   InsertManyResult insertMany(List<? extends TDocument> var1);

   InsertManyResult insertMany(List<? extends TDocument> var1, InsertManyOptions var2);

   InsertManyResult insertMany(ClientSession var1, List<? extends TDocument> var2);

   InsertManyResult insertMany(ClientSession var1, List<? extends TDocument> var2, InsertManyOptions var3);

   DeleteResult deleteOne(Bson var1);

   DeleteResult deleteOne(Bson var1, DeleteOptions var2);

   DeleteResult deleteOne(ClientSession var1, Bson var2);

   DeleteResult deleteOne(ClientSession var1, Bson var2, DeleteOptions var3);

   DeleteResult deleteMany(Bson var1);

   DeleteResult deleteMany(Bson var1, DeleteOptions var2);

   DeleteResult deleteMany(ClientSession var1, Bson var2);

   DeleteResult deleteMany(ClientSession var1, Bson var2, DeleteOptions var3);

   UpdateResult replaceOne(Bson var1, TDocument var2);

   UpdateResult replaceOne(Bson var1, TDocument var2, ReplaceOptions var3);

   UpdateResult replaceOne(ClientSession var1, Bson var2, TDocument var3);

   UpdateResult replaceOne(ClientSession var1, Bson var2, TDocument var3, ReplaceOptions var4);

   UpdateResult updateOne(Bson var1, Bson var2);

   UpdateResult updateOne(Bson var1, Bson var2, UpdateOptions var3);

   UpdateResult updateOne(ClientSession var1, Bson var2, Bson var3);

   UpdateResult updateOne(ClientSession var1, Bson var2, Bson var3, UpdateOptions var4);

   UpdateResult updateOne(Bson var1, List<? extends Bson> var2);

   UpdateResult updateOne(Bson var1, List<? extends Bson> var2, UpdateOptions var3);

   UpdateResult updateOne(ClientSession var1, Bson var2, List<? extends Bson> var3);

   UpdateResult updateOne(ClientSession var1, Bson var2, List<? extends Bson> var3, UpdateOptions var4);

   UpdateResult updateMany(Bson var1, Bson var2);

   UpdateResult updateMany(Bson var1, Bson var2, UpdateOptions var3);

   UpdateResult updateMany(ClientSession var1, Bson var2, Bson var3);

   UpdateResult updateMany(ClientSession var1, Bson var2, Bson var3, UpdateOptions var4);

   UpdateResult updateMany(Bson var1, List<? extends Bson> var2);

   UpdateResult updateMany(Bson var1, List<? extends Bson> var2, UpdateOptions var3);

   UpdateResult updateMany(ClientSession var1, Bson var2, List<? extends Bson> var3);

   UpdateResult updateMany(ClientSession var1, Bson var2, List<? extends Bson> var3, UpdateOptions var4);

   @Nullable
   TDocument findOneAndDelete(Bson var1);

   @Nullable
   TDocument findOneAndDelete(Bson var1, FindOneAndDeleteOptions var2);

   @Nullable
   TDocument findOneAndDelete(ClientSession var1, Bson var2);

   @Nullable
   TDocument findOneAndDelete(ClientSession var1, Bson var2, FindOneAndDeleteOptions var3);

   @Nullable
   TDocument findOneAndReplace(Bson var1, TDocument var2);

   @Nullable
   TDocument findOneAndReplace(Bson var1, TDocument var2, FindOneAndReplaceOptions var3);

   @Nullable
   TDocument findOneAndReplace(ClientSession var1, Bson var2, TDocument var3);

   @Nullable
   TDocument findOneAndReplace(ClientSession var1, Bson var2, TDocument var3, FindOneAndReplaceOptions var4);

   @Nullable
   TDocument findOneAndUpdate(Bson var1, Bson var2);

   @Nullable
   TDocument findOneAndUpdate(Bson var1, Bson var2, FindOneAndUpdateOptions var3);

   @Nullable
   TDocument findOneAndUpdate(ClientSession var1, Bson var2, Bson var3);

   @Nullable
   TDocument findOneAndUpdate(ClientSession var1, Bson var2, Bson var3, FindOneAndUpdateOptions var4);

   @Nullable
   TDocument findOneAndUpdate(Bson var1, List<? extends Bson> var2);

   @Nullable
   TDocument findOneAndUpdate(Bson var1, List<? extends Bson> var2, FindOneAndUpdateOptions var3);

   @Nullable
   TDocument findOneAndUpdate(ClientSession var1, Bson var2, List<? extends Bson> var3);

   @Nullable
   TDocument findOneAndUpdate(ClientSession var1, Bson var2, List<? extends Bson> var3, FindOneAndUpdateOptions var4);

   void drop();

   void drop(ClientSession var1);

   void drop(DropCollectionOptions var1);

   void drop(ClientSession var1, DropCollectionOptions var2);

   String createSearchIndex(String var1, Bson var2);

   String createSearchIndex(Bson var1);

   List<String> createSearchIndexes(List<SearchIndexModel> var1);

   void updateSearchIndex(String var1, Bson var2);

   void dropSearchIndex(String var1);

   ListSearchIndexesIterable<Document> listSearchIndexes();

   <TResult> ListSearchIndexesIterable<TResult> listSearchIndexes(Class<TResult> var1);

   String createIndex(Bson var1);

   String createIndex(Bson var1, IndexOptions var2);

   String createIndex(ClientSession var1, Bson var2);

   String createIndex(ClientSession var1, Bson var2, IndexOptions var3);

   List<String> createIndexes(List<IndexModel> var1);

   List<String> createIndexes(List<IndexModel> var1, CreateIndexOptions var2);

   List<String> createIndexes(ClientSession var1, List<IndexModel> var2);

   List<String> createIndexes(ClientSession var1, List<IndexModel> var2, CreateIndexOptions var3);

   ListIndexesIterable<Document> listIndexes();

   <TResult> ListIndexesIterable<TResult> listIndexes(Class<TResult> var1);

   ListIndexesIterable<Document> listIndexes(ClientSession var1);

   <TResult> ListIndexesIterable<TResult> listIndexes(ClientSession var1, Class<TResult> var2);

   void dropIndex(String var1);

   void dropIndex(String var1, DropIndexOptions var2);

   void dropIndex(Bson var1);

   void dropIndex(Bson var1, DropIndexOptions var2);

   void dropIndex(ClientSession var1, String var2);

   void dropIndex(ClientSession var1, Bson var2);

   void dropIndex(ClientSession var1, String var2, DropIndexOptions var3);

   void dropIndex(ClientSession var1, Bson var2, DropIndexOptions var3);

   void dropIndexes();

   void dropIndexes(ClientSession var1);

   void dropIndexes(DropIndexOptions var1);

   void dropIndexes(ClientSession var1, DropIndexOptions var2);

   void renameCollection(MongoNamespace var1);

   void renameCollection(MongoNamespace var1, RenameCollectionOptions var2);

   void renameCollection(ClientSession var1, MongoNamespace var2);

   void renameCollection(ClientSession var1, MongoNamespace var2, RenameCollectionOptions var3);
}
