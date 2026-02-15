package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.conversions.Bson;

public final class IndexOptionDefaults {
   private Bson storageEngine;

   @Nullable
   public Bson getStorageEngine() {
      return this.storageEngine;
   }

   public IndexOptionDefaults storageEngine(@Nullable Bson storageEngine) {
      this.storageEngine = storageEngine;
      return this;
   }

   public String toString() {
      return "IndexOptionDefaults{storageEngine=" + this.storageEngine + '}';
   }
}
