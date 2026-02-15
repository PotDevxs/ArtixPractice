package dev.artixdev.libs.com.mongodb.client.model.vault;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinary;

public class EncryptOptions {
   private BsonBinary keyId;
   private String keyAltName;
   private final String algorithm;
   private Long contentionFactor;
   private String queryType;
   private RangeOptions rangeOptions;

   public EncryptOptions(String algorithm) {
      this.algorithm = algorithm;
   }

   public String getAlgorithm() {
      return this.algorithm;
   }

   @Nullable
   public BsonBinary getKeyId() {
      return this.keyId;
   }

   @Nullable
   public String getKeyAltName() {
      return this.keyAltName;
   }

   public EncryptOptions keyId(BsonBinary keyId) {
      this.keyId = keyId;
      return this;
   }

   public EncryptOptions keyAltName(String keyAltName) {
      this.keyAltName = keyAltName;
      return this;
   }

   public EncryptOptions contentionFactor(@Nullable Long contentionFactor) {
      this.contentionFactor = contentionFactor;
      return this;
   }

   @Nullable
   public Long getContentionFactor() {
      return this.contentionFactor;
   }

   public EncryptOptions queryType(@Nullable String queryType) {
      this.queryType = queryType;
      return this;
   }

   @Nullable
   public String getQueryType() {
      return this.queryType;
   }

   @Beta({Beta.Reason.SERVER})
   public EncryptOptions rangeOptions(@Nullable RangeOptions rangeOptions) {
      this.rangeOptions = rangeOptions;
      return this;
   }

   @Nullable
   @Beta({Beta.Reason.SERVER})
   public RangeOptions getRangeOptions() {
      return this.rangeOptions;
   }

   public String toString() {
      return "EncryptOptions{keyId=" + this.keyId + ", keyAltName='" + this.keyAltName + '\'' + ", algorithm='" + this.algorithm + '\'' + ", contentionFactor=" + this.contentionFactor + ", queryType='" + this.queryType + '\'' + ", rangeOptions=" + this.rangeOptions + '}';
   }
}
