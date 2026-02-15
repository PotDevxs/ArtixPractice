package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoClientException extends MongoException {
   private static final long serialVersionUID = -5127414714432646066L;

   public MongoClientException(String message) {
      super(message);
   }

   public MongoClientException(String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
