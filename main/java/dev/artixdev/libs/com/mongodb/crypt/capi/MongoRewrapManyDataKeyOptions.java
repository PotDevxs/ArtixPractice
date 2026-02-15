package dev.artixdev.libs.com.mongodb.crypt.capi;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

/**
 * Stub for MongoDB crypt rewrap many data key options.
 * Full implementation is in mongodb-crypt dependency.
 */
public final class MongoRewrapManyDataKeyOptions {
   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String provider;
      private BsonDocument masterKey;

      public Builder provider(@Nullable String provider) {
         this.provider = provider;
         return this;
      }

      public Builder masterKey(@Nullable BsonDocument masterKey) {
         this.masterKey = masterKey;
         return this;
      }

      public MongoRewrapManyDataKeyOptions build() {
         return new MongoRewrapManyDataKeyOptions(provider, masterKey);
      }
   }

   private final String provider;
   private final BsonDocument masterKey;

   private MongoRewrapManyDataKeyOptions(String provider, BsonDocument masterKey) {
      this.provider = provider;
      this.masterKey = masterKey;
   }
}
