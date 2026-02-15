package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;

public abstract class IndexMap {
   public static IndexMap create() {
      return new IndexMap.RangeBased();
   }

   public static IndexMap create(int startIndex, int count) {
      return new IndexMap.RangeBased(startIndex, count);
   }

   public abstract IndexMap add(int var1, int var2);

   public abstract int map(int var1);

   private static class RangeBased extends IndexMap {
      private int startIndex;
      private int count;

      RangeBased() {
      }

      RangeBased(int startIndex, int count) {
         Assertions.isTrueArgument("startIndex", startIndex >= 0);
         Assertions.isTrueArgument("count", count > 0);
         this.startIndex = startIndex;
         this.count = count;
      }

      public IndexMap add(int index, int originalIndex) {
         if (this.count == 0) {
            this.startIndex = originalIndex;
            this.count = 1;
            return this;
         } else if (originalIndex == this.startIndex + this.count) {
            ++this.count;
            return this;
         } else {
            IndexMap hashBasedMap = new IndexMap.HashBased(this.startIndex, this.count);
            hashBasedMap.add(index, originalIndex);
            return hashBasedMap;
         }
      }

      public int map(int index) {
         if (index < 0) {
            throw new MongoInternalException("no mapping found for index " + index);
         } else if (index >= this.count) {
            throw new MongoInternalException("index should not be greater than or equal to count");
         } else {
            return this.startIndex + index;
         }
      }
   }

   private static class HashBased extends IndexMap {
      private final Map<Integer, Integer> indexMap = new HashMap();

      HashBased(int startIndex, int count) {
         for(int i = startIndex; i < startIndex + count; ++i) {
            this.indexMap.put(i - startIndex, i);
         }

      }

      public IndexMap add(int index, int originalIndex) {
         this.indexMap.put(index, originalIndex);
         return this;
      }

      public int map(int index) {
         Integer originalIndex = (Integer)this.indexMap.get(index);
         if (originalIndex == null) {
            throw new MongoInternalException("no mapping found for index " + index);
         } else {
            return originalIndex;
         }
      }
   }
}
