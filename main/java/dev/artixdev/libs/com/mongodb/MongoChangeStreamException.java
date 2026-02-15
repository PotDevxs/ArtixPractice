package dev.artixdev.libs.com.mongodb;

public class MongoChangeStreamException extends MongoException {
   private static final long serialVersionUID = 3621370414132219001L;

   public MongoChangeStreamException(String message) {
      super(message);
   }
}
