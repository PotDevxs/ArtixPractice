package dev.artixdev.libs.com.mongodb.crypt.capi;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.RawBsonDocument;

/**
 * Stub for MongoDB crypt context (encryption/decryption state machine).
 * Full implementation is in mongodb-crypt dependency.
 */
public interface MongoCryptContext {
   enum State {
      NEED_MONGO_COLLINFO,
      NEED_MONGO_MARKINGS,
      NEED_KMS_CREDENTIALS,
      NEED_MONGO_KEYS,
      NEED_KMS,
      READY,
      DONE
   }

   State getState();

   RawBsonDocument finish();

   void provideKmsProviderCredentials(BsonDocument credentials);

   RawBsonDocument getMongoOperation();

   void addMongoOperationResult(BsonDocument result);

   void completeMongoOperation();

   @Nullable
   MongoKeyDecryptor nextKeyDecryptor();

   void completeKeyDecryptors();

   void close();
}
