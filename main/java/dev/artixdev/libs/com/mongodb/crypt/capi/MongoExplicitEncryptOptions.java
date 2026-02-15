package dev.artixdev.libs.com.mongodb.crypt.capi;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDocument;

/**
 * Stub for MongoDB crypt explicit encrypt options.
 * Full implementation is in mongodb-crypt dependency.
 */
public final class MongoExplicitEncryptOptions {
   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String algorithm;
      private BsonBinary keyId;
      private String keyAltName;
      private Long contentionFactor;
      private String queryType;
      private BsonDocument rangeOptions;

      public Builder algorithm(String algorithm) {
         this.algorithm = algorithm;
         return this;
      }

      public Builder keyId(@Nullable BsonBinary keyId) {
         this.keyId = keyId;
         return this;
      }

      public Builder keyAltName(@Nullable String keyAltName) {
         this.keyAltName = keyAltName;
         return this;
      }

      public Builder contentionFactor(@Nullable Long contentionFactor) {
         this.contentionFactor = contentionFactor;
         return this;
      }

      public Builder queryType(@Nullable String queryType) {
         this.queryType = queryType;
         return this;
      }

      public Builder rangeOptions(@Nullable BsonDocument rangeOptions) {
         this.rangeOptions = rangeOptions;
         return this;
      }

      public MongoExplicitEncryptOptions build() {
         return new MongoExplicitEncryptOptions(algorithm, keyId, keyAltName, contentionFactor, queryType, rangeOptions);
      }
   }

   private final String algorithm;
   private final BsonBinary keyId;
   private final String keyAltName;
   private final Long contentionFactor;
   private final String queryType;
   private final BsonDocument rangeOptions;

   private MongoExplicitEncryptOptions(String algorithm, BsonBinary keyId, String keyAltName, Long contentionFactor, String queryType, BsonDocument rangeOptions) {
      this.algorithm = algorithm;
      this.keyId = keyId;
      this.keyAltName = keyAltName;
      this.contentionFactor = contentionFactor;
      this.queryType = queryType;
      this.rangeOptions = rangeOptions;
   }
}
