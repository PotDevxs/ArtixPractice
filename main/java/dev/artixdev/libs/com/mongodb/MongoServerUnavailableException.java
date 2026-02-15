package dev.artixdev.libs.com.mongodb;

public final class MongoServerUnavailableException extends MongoClientException {
   private static final long serialVersionUID = 5465094535584085700L;

   public MongoServerUnavailableException(String message) {
      super(message);
   }
}
