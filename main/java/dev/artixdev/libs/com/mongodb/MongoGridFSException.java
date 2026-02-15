package dev.artixdev.libs.com.mongodb;

public class MongoGridFSException extends MongoException {
   private static final long serialVersionUID = -3894346172927543978L;

   public MongoGridFSException(String message) {
      super(message);
   }

   public MongoGridFSException(String message, Throwable t) {
      super(message, t);
   }
}
