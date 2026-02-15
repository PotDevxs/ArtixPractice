package dev.artixdev.libs.com.mongodb;

public class MongoInternalException extends MongoException {
   private static final long serialVersionUID = -4415279469780082174L;

   public MongoInternalException(String msg) {
      super(msg);
   }

   public MongoInternalException(String msg, Throwable t) {
      super(msg, t);
   }
}
