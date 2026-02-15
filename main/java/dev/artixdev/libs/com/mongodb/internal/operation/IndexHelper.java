package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import dev.artixdev.libs.com.mongodb.client.model.IndexModel;
import dev.artixdev.libs.com.mongodb.client.model.SearchIndexModel;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonNumber;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class IndexHelper {
   public static List<String> getIndexNames(List<IndexModel> indexes, CodecRegistry codecRegistry) {
      List<String> indexNames = new ArrayList(indexes.size());
      Iterator var3 = indexes.iterator();

      while(var3.hasNext()) {
         IndexModel index = (IndexModel)var3.next();
         String name = index.getOptions().getName();
         if (name != null) {
            indexNames.add(name);
         } else {
            indexNames.add(generateIndexName(index.getKeys().toBsonDocument(BsonDocument.class, codecRegistry)));
         }
      }

      return indexNames;
   }

   public static List<String> getSearchIndexNames(List<SearchIndexModel> indexes) {
      return (List)indexes.stream().map(IndexHelper::getSearchIndexName).collect(Collectors.toList());
   }

   private static String getSearchIndexName(SearchIndexModel model) {
      String name = model.getName();
      return name != null ? name : "default";
   }

   public static String generateIndexName(BsonDocument index) {
      StringBuilder indexName = new StringBuilder();
      Iterator var2 = index.keySet().iterator();

      while(var2.hasNext()) {
         String keyNames = (String)var2.next();
         if (indexName.length() != 0) {
            indexName.append('_');
         }

         indexName.append(keyNames).append('_');
         BsonValue ascOrDescValue = index.get(keyNames);
         if (ascOrDescValue instanceof BsonNumber) {
            indexName.append(((BsonNumber)ascOrDescValue).intValue());
         } else if (ascOrDescValue instanceof BsonString) {
            indexName.append(((BsonString)ascOrDescValue).getValue().replace(' ', '_'));
         }
      }

      return indexName.toString();
   }

   private IndexHelper() {
   }
}
