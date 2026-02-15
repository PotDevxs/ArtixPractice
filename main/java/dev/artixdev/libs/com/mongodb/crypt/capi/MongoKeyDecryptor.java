package dev.artixdev.libs.com.mongodb.crypt.capi;

import java.nio.ByteBuffer;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

/**
 * Stub for MongoDB crypt key decryptor (KMS decryption).
 * Full implementation is in mongodb-crypt dependency.
 */
public interface MongoKeyDecryptor {
   String getKmsProvider();

   @Nullable
   String getHostName();

   ByteBuffer getMessage();

   int bytesNeeded();

   void feed(ByteBuffer bytes);
}
