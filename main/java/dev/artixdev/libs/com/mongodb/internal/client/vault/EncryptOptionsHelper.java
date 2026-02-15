package dev.artixdev.libs.com.mongodb.internal.client.vault;

import dev.artixdev.libs.com.mongodb.client.model.vault.EncryptOptions;
import dev.artixdev.libs.com.mongodb.client.model.vault.RangeOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoExplicitEncryptOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoExplicitEncryptOptions.Builder;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonValue;

public final class EncryptOptionsHelper {
   public static MongoExplicitEncryptOptions asMongoExplicitEncryptOptions(EncryptOptions options) {
      Builder encryptOptionsBuilder = MongoExplicitEncryptOptions.builder().algorithm(options.getAlgorithm());
      if (options.getKeyId() != null) {
         encryptOptionsBuilder.keyId(options.getKeyId());
      }

      if (options.getKeyAltName() != null) {
         encryptOptionsBuilder.keyAltName(options.getKeyAltName());
      }

      if (options.getContentionFactor() != null) {
         encryptOptionsBuilder.contentionFactor(options.getContentionFactor());
      }

      if (options.getQueryType() != null) {
         encryptOptionsBuilder.queryType(options.getQueryType());
      }

      RangeOptions rangeOptions = options.getRangeOptions();
      if (rangeOptions != null) {
         BsonDocument rangeOptionsBsonDocument = new BsonDocument();
         BsonValue min = rangeOptions.getMin();
         if (min != null) {
            rangeOptionsBsonDocument.put("min", min);
         }

         BsonValue max = rangeOptions.getMax();
         if (max != null) {
            rangeOptionsBsonDocument.put("max", max);
         }

         Long sparsity = rangeOptions.getSparsity();
         if (sparsity != null) {
            rangeOptionsBsonDocument.put((String)"sparsity", (BsonValue)(new BsonInt64(sparsity)));
         }

         Integer precision = rangeOptions.getPrecision();
         if (precision != null) {
            rangeOptionsBsonDocument.put((String)"precision", (BsonValue)(new BsonInt32(precision)));
         }

         encryptOptionsBuilder.rangeOptions(rangeOptionsBsonDocument);
      }

      return encryptOptionsBuilder.build();
   }

   private EncryptOptionsHelper() {
   }
}
