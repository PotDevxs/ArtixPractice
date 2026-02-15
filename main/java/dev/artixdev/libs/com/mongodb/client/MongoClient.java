package dev.artixdev.libs.com.mongodb.client;

import java.io.Closeable;
import java.util.List;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.conversions.Bson;

@Immutable
public interface MongoClient extends Closeable {
   MongoDatabase getDatabase(String var1);

   ClientSession startSession();

   ClientSession startSession(ClientSessionOptions var1);

   void close();

   MongoIterable<String> listDatabaseNames();

   MongoIterable<String> listDatabaseNames(ClientSession var1);

   ListDatabasesIterable<Document> listDatabases();

   ListDatabasesIterable<Document> listDatabases(ClientSession var1);

   <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> var1);

   <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession var1, Class<TResult> var2);

   ChangeStreamIterable<Document> watch();

   <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> var1);

   ChangeStreamIterable<Document> watch(List<? extends Bson> var1);

   <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> var1, Class<TResult> var2);

   ChangeStreamIterable<Document> watch(ClientSession var1);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, Class<TResult> var2);

   ChangeStreamIterable<Document> watch(ClientSession var1, List<? extends Bson> var2);

   <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

   ClusterDescription getClusterDescription();
}
