package dev.artixdev.libs.com.mongodb.crypt.capi;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.RawBsonDocument;

/**
 * Stub factory for creating MongoCrypt instances.
 * Full implementation is in mongodb-crypt dependency.
 * The returned stub throws UnsupportedOperationException when encryption is used.
 */
public final class MongoCrypts {
   public static MongoCrypt create(MongoCryptOptions options) {
      return new StubMongoCrypt();
   }

   private MongoCrypts() {
   }

   private static final class StubMongoCrypt implements MongoCrypt {
      @Override
      @Nullable
      public String getCryptSharedLibVersionString() {
         return null;
      }

      @Override
      public void close() {
      }

      @Override
      public MongoCryptContext createEncryptionContext(String databaseName, RawBsonDocument command) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createDecryptionContext(RawBsonDocument commandResponse) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createDataKeyContext(String kmsProvider, MongoDataKeyOptions options) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createExplicitEncryptionContext(BsonDocument document, MongoExplicitEncryptOptions options) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createEncryptExpressionContext(BsonDocument document, MongoExplicitEncryptOptions options) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createExplicitDecryptionContext(BsonDocument document) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }

      @Override
      public MongoCryptContext createRewrapManyDatakeyContext(BsonDocument filter, MongoRewrapManyDataKeyOptions options) {
         throw new UnsupportedOperationException("mongodb-crypt not available; add mongodb-crypt dependency for client-side encryption");
      }
   }
}
