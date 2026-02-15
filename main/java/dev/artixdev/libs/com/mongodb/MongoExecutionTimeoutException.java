package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonDocument;

public class MongoExecutionTimeoutException extends MongoException {
   private static final long serialVersionUID = 5955669123800274594L;

   public MongoExecutionTimeoutException(int code, String message) {
      super(code, message);
   }

   public MongoExecutionTimeoutException(int code, String message, BsonDocument response) {
      super(code, message, response);
   }
}
