package dev.artixdev.libs.com.mongodb.crypt.capi;

/**
 * Stub for MongoDB crypt library exceptions.
 * Full implementation is in mongodb-crypt dependency.
 */
public class MongoCryptException extends RuntimeException {
   public MongoCryptException(String message) {
      super(message);
   }

   public MongoCryptException(String message, Throwable cause) {
      super(message, cause);
   }

   public MongoCryptException(Throwable cause) {
      super(cause);
   }
}
