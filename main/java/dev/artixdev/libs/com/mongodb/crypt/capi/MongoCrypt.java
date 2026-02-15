package dev.artixdev.libs.com.mongodb.crypt.capi;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.RawBsonDocument;

/**
 * Minimal interface for MongoDB client-side encryption (crypt shared library).
 * Stub for decompiled code when the full mongodb-crypt dependency is not present.
 */
public interface MongoCrypt {
   @Nullable
   String getCryptSharedLibVersionString();

   void close();

   MongoCryptContext createEncryptionContext(String databaseName, RawBsonDocument command);

   MongoCryptContext createDecryptionContext(RawBsonDocument commandResponse);

   MongoCryptContext createDataKeyContext(String kmsProvider, MongoDataKeyOptions options);

   MongoCryptContext createExplicitEncryptionContext(BsonDocument document, MongoExplicitEncryptOptions options);

   MongoCryptContext createEncryptExpressionContext(BsonDocument document, MongoExplicitEncryptOptions options);

   MongoCryptContext createExplicitDecryptionContext(BsonDocument document);

   MongoCryptContext createRewrapManyDatakeyContext(BsonDocument filter, MongoRewrapManyDataKeyOptions options);
}
