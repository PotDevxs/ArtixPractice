package dev.artixdev.libs.com.mongodb;

public class MongoTimeoutException extends MongoClientException {
   private static final long serialVersionUID = -3016560214331826577L;

   public MongoTimeoutException(String message) {
      super(message);
   }
}
