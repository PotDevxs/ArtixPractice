package dev.artixdev.libs.com.mongodb.client;

import java.util.List;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.client.model.CreateCollectionOptions;
import dev.artixdev.libs.com.mongodb.client.model.CreateViewOptions;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

@ThreadSafe
public interface MongoDatabase {
   String getName();

   CodecRegistry getCodecRegistry();

   ReadPreference getReadPreference();

   WriteConcern getWriteConcern();

   ReadConcern getReadConcern();

   MongoDatabase withCodecRegistry(CodecRegistry var1);

   MongoDatabase withReadPreference(ReadPreference var1);

   MongoDatabase withWriteConcern(WriteConcern var1);

   MongoDatabase withReadConcern(ReadConcern var1);

   MongoCollection<Document> getCollection(String var1);

   <TDocument> MongoCollection<TDocument> getCollection(String var1, Class<TDocument> var2);

   Document runCommand(Bson var1);

   Document runCommand(Bson var1, ReadPreference var2);

   <TResult> TResult runCommand(Bson var1, Class<TResult> var2);

   <TResult> TResult runCommand(Bson var1, ReadPreference var2, Class<TResult> var3);

   Document runCommand(ClientSession var1, Bson var2);

   Document runCommand(ClientSession var1, Bson var2, ReadPreference var3);

   <TResult> TResult runCommand(ClientSession var1, Bson var2, Class<TResult> var3);

   <TResult> TResult runCommand(ClientSession var1, Bson var2, ReadPreference var3, Class<TResult> var4);

   void drop();

   void drop(ClientSession var1);

   MongoIterable<String> listCollectionNames();

   ListCollectionsIterable<Document> listCollections();

   <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> var1);

   MongoIterable<String> listCollectionNames(ClientSession var1);

   ListCollectionsIterable<Document> listCollections(ClientSession var1);

   <TResult> ListCollectionsIterable<TResult> listCollections(ClientSession var1, Class<TResult> var2);

   void createCollection(String var1);

   void createCollection(String var1, CreateCollectionOptions var2);

   void createCollection(ClientSession var1, String var2);

   void createCollection(ClientSession var1, String var2, CreateCollectionOptions var3);

   void createView(String var1, String var2, List<? extends Bson> var3);

   void createView(String var1, String var2, List<? extends Bson> var3, CreateViewOptions var4);

   void createView(ClientSession var1, String var2, String var3, List<? extends Bson> var4);

   void createView(ClientSession var1, String var2, String var3, List<? extends Bson> var4, CreateViewOptions var5);

   ChangeStreamIterable<Document> watch();

   <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> var1);

   ChangeStreamIterable<Document> watch(List<? extends Bson> var1);

   <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> var1, Class<TResult> var2);

   ChangeStreamIterable<Document> watch(ClientSession var1);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, Class<TResult> var2);

   ChangeStreamIterable<Document> watch(ClientSession var1, List<? extends Bson> var2);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

   AggregateIterable<Document> aggregate(List<? extends Bson> var1);

   <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> var1, Class<TResult> var2);

   AggregateIterable<Document> aggregate(ClientSession var1, List<? extends Bson> var2);

   <TResult> AggregateIterable<TResult> aggregate(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);
}
