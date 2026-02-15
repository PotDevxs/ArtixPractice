package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class ClusteredIndexOptions {
   private final Bson key;
   private final boolean unique;
   private String name;

   public ClusteredIndexOptions(Bson key, boolean unique) {
      this.key = (Bson)Assertions.notNull("key", key);
      this.unique = unique;
   }

   public Bson getKey() {
      return this.key;
   }

   public boolean isUnique() {
      return this.unique;
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   public ClusteredIndexOptions name(String name) {
      this.name = name;
      return this;
   }

   public String toString() {
      return "ClusteredIndexOptions{key=" + this.key + ", unique=" + this.unique + ", name='" + this.name + '\'' + '}';
   }
}
