package dev.artixdev.libs.com.mongodb.client.internal;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

class CollectionInfoRetriever {
   private final MongoClient client;

   CollectionInfoRetriever(MongoClient client) {
      this.client = (MongoClient)Assertions.notNull("client", client);
   }

   @Nullable
   public BsonDocument filter(String databaseName, BsonDocument filter) {
      return (BsonDocument)this.client.getDatabase(databaseName).listCollections(BsonDocument.class).filter(filter).first();
   }
}
