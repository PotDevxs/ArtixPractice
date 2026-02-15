package dev.artixdev.libs.com.mongodb.client.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class Sorts {
   private Sorts() {
   }

   public static Bson ascending(String... fieldNames) {
      return ascending(Arrays.asList(fieldNames));
   }

   public static Bson ascending(List<String> fieldNames) {
      Assertions.notNull("fieldNames", fieldNames);
      return orderBy(fieldNames, new BsonInt32(1));
   }

   public static Bson descending(String... fieldNames) {
      return descending(Arrays.asList(fieldNames));
   }

   public static Bson descending(List<String> fieldNames) {
      Assertions.notNull("fieldNames", fieldNames);
      return orderBy(fieldNames, new BsonInt32(-1));
   }

   public static Bson metaTextScore(String fieldName) {
      return new BsonDocument(fieldName, new BsonDocument("$meta", new BsonString("textScore")));
   }

   public static Bson orderBy(Bson... sorts) {
      return orderBy(Arrays.asList(sorts));
   }

   public static Bson orderBy(List<? extends Bson> sorts) {
      Assertions.notNull("sorts", sorts);
      return new Sorts.CompoundSort(sorts);
   }

   private static Bson orderBy(List<String> fieldNames, BsonValue value) {
      BsonDocument document = new BsonDocument();
      Iterator var3 = fieldNames.iterator();

      while(var3.hasNext()) {
         String fieldName = (String)var3.next();
         document.append(fieldName, value);
      }

      return document;
   }

   private static final class CompoundSort implements Bson {
      private final List<? extends Bson> sorts;

      private CompoundSort(List<? extends Bson> sorts) {
         this.sorts = sorts;
      }

      public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
         BsonDocument combinedDocument = new BsonDocument();
         Iterator var4 = this.sorts.iterator();

         while(var4.hasNext()) {
            Bson sort = (Bson)var4.next();
            BsonDocument sortDocument = sort.toBsonDocument(documentClass, codecRegistry);
            Iterator var7 = sortDocument.keySet().iterator();

            while(var7.hasNext()) {
               String key = (String)var7.next();
               combinedDocument.append(key, sortDocument.get(key));
            }
         }

         return combinedDocument;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            Sorts.CompoundSort that = (Sorts.CompoundSort)o;
            return Objects.equals(this.sorts, that.sorts);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.sorts != null ? this.sorts.hashCode() : 0;
      }

      public String toString() {
         return "Compound Sort{sorts=" + this.sorts + '}';
      }

      // $FF: synthetic method
      CompoundSort(List x0, Object x1) {
         this(x0);
      }
   }
}
