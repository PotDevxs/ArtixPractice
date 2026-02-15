package dev.artixdev.api.practice.storage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.mongodb.client.MongoCollection;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.client.model.Filters;
import dev.artixdev.libs.com.mongodb.client.model.ReplaceOptions;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.conversions.Bson;
import dev.artixdev.practice.models.PlayerProfile;

public class MongoStorage<V> {
   private static final ReplaceOptions REPLACE_OPTIONS = (new ReplaceOptions()).upsert(true);
   private final MongoCollection<Document> collection;
   private final Gson gson;

   public MongoStorage(MongoCollection<Document> collection, Gson gson) {
      this.collection = collection;
      this.gson = gson;
   }

   public CompletableFuture<List<V>> fetchAllEntries() {
      return CompletableFuture.supplyAsync(() -> {
         List<V> found = new ArrayList();
         MongoCursor var2 = this.collection.find().iterator();

         while(var2.hasNext()) {
            Document document = (Document)var2.next();
            if (document != null) {
               Type typeToken = (new TypeToken<V>() {
               }).getType();
               found.add(this.gson.fromJson(document.toJson(), typeToken));
            }
         }

         return found;
      });
   }

   public CompletableFuture<List<Document>> fetchAllRawEntries() {
      return CompletableFuture.supplyAsync(() -> {
         List<Document> found = new ArrayList();
         MongoCursor var2 = this.collection.find().iterator();

         while(var2.hasNext()) {
            Document document = (Document)var2.next();
            found.add(document);
         }

         return found;
      });
   }

   public void saveData(UUID key, V value, Type type) {
      CompletableFuture.runAsync(() -> {
         this.saveDataSync(key, value, type);
      });
   }

   public void saveDataSync(UUID key, V value, Type type) {
      Bson query = Filters.eq("_id", key.toString());
      Document parsed = Document.parse(this.gson.toJson(value, type));
      this.collection.replaceOne(query, parsed, REPLACE_OPTIONS);
   }

   public void saveRawData(UUID key, Document document) {
      CompletableFuture.runAsync(() -> {
         this.saveRawDataSync(key, document);
      });
   }

   public void saveRawDataSync(UUID key, Document document) {
      Bson query = Filters.eq("_id", key.toString());
      this.collection.replaceOne(query, document, REPLACE_OPTIONS);
   }

   public V loadData(UUID key, Type type) {
      Bson query = Filters.eq("_id", key.toString());
      Document document = (Document)this.collection.find(query).first();
      return document == null ? null : this.gson.fromJson(document.toJson(), type);
   }

   public CompletableFuture<V> loadDataAsync(UUID key, Type type) {
      return CompletableFuture.supplyAsync(() -> {
         return this.loadData(key, type);
      });
   }

   public Document loadRawData(UUID key) {
      Bson query = Filters.eq("_id", key.toString());
      return (Document)this.collection.find(query).first();
   }

   public CompletableFuture<Document> loadRawDataAsync(UUID key) {
      return CompletableFuture.supplyAsync(() -> {
         return this.loadRawData(key);
      });
   }

   public void deleteData(UUID key) {
      CompletableFuture.runAsync(() -> {
         Bson query = Filters.eq("_id", key.toString());
         this.collection.deleteOne(query);
      });
   }

   public CompletableFuture<Integer> deleteKeyInAll(String key) {
      return CompletableFuture.supplyAsync(() -> {
         int deleteCount = 0;
         MongoCursor var3 = this.collection.find().iterator();

         while(var3.hasNext()) {
            Document document = (Document)var3.next();
            if (document != null) {
               document.remove(key);
               ++deleteCount;
            }
         }

         return deleteCount;
      });
   }

   public PlayerProfile load(UUID playerId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'load'");
   }

   public boolean save(PlayerProfile profile) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
   }

   public boolean delete(UUID playerId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
   }
}
