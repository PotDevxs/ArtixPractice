package dev.artixdev.libs.com.mongodb.crypt.capi;

import java.util.List;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

/**
 * Stub for MongoDB crypt data key options.
 * Full implementation is in mongodb-crypt dependency.
 */
public final class MongoDataKeyOptions {
   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private List<String> keyAltNames;
      private BsonDocument masterKey;
      private byte[] keyMaterial;

      public Builder keyAltNames(@Nullable List<String> keyAltNames) {
         this.keyAltNames = keyAltNames;
         return this;
      }

      public Builder masterKey(@Nullable BsonDocument masterKey) {
         this.masterKey = masterKey;
         return this;
      }

      public Builder keyMaterial(@Nullable byte[] keyMaterial) {
         this.keyMaterial = keyMaterial;
         return this;
      }

      public MongoDataKeyOptions build() {
         return new MongoDataKeyOptions(keyAltNames, masterKey, keyMaterial);
      }
   }

   private final List<String> keyAltNames;
   private final BsonDocument masterKey;
   private final byte[] keyMaterial;

   private MongoDataKeyOptions(List<String> keyAltNames, BsonDocument masterKey, byte[] keyMaterial) {
      this.keyAltNames = keyAltNames;
      this.masterKey = masterKey;
      this.keyMaterial = keyMaterial;
   }
}
