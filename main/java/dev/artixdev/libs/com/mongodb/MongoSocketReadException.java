package dev.artixdev.libs.com.mongodb;

public class MongoSocketReadException extends MongoSocketException {
   private static final long serialVersionUID = -1142547119966956531L;

   public MongoSocketReadException(String message, ServerAddress address) {
      super(message, address);
   }

   public MongoSocketReadException(String message, ServerAddress address, Throwable cause) {
      super(message, address, cause);
   }
}
