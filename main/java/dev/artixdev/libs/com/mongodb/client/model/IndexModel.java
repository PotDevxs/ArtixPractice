package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class IndexModel {
   private final Bson keys;
   private final IndexOptions options;

   public IndexModel(Bson keys) {
      this(keys, new IndexOptions());
   }

   public IndexModel(Bson keys, IndexOptions options) {
      this.keys = (Bson)Assertions.notNull("keys", keys);
      this.options = (IndexOptions)Assertions.notNull("options", options);
   }

   public Bson getKeys() {
      return this.keys;
   }

   public IndexOptions getOptions() {
      return this.options;
   }

   public String toString() {
      return "IndexModel{keys=" + this.keys + ", options=" + this.options + '}';
   }
}
