package dev.artixdev.libs.com.mongodb.client.model.vault;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class RewrapManyDataKeyOptions {
   private String provider;
   private BsonDocument masterKey;

   public RewrapManyDataKeyOptions provider(String provider) {
      this.provider = provider;
      return this;
   }

   @Nullable
   public String getProvider() {
      return this.provider;
   }

   public RewrapManyDataKeyOptions masterKey(BsonDocument masterKey) {
      this.masterKey = masterKey;
      return this;
   }

   @Nullable
   public BsonDocument getMasterKey() {
      return this.masterKey;
   }
}
