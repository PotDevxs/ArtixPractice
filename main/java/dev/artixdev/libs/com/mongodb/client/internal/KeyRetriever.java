package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.conversions.Bson;

class KeyRetriever {
   private final MongoClient client;
   private final MongoNamespace namespace;

   KeyRetriever(MongoClient client, MongoNamespace namespace) {
      this.client = (MongoClient)Assertions.notNull("client", client);
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
   }

   public List<BsonDocument> find(BsonDocument keyFilter) {
      return (List)this.client.getDatabase(this.namespace.getDatabaseName()).getCollection(this.namespace.getCollectionName(), BsonDocument.class).withReadConcern(ReadConcern.MAJORITY).find((Bson)keyFilter).into(new ArrayList());
   }
}
