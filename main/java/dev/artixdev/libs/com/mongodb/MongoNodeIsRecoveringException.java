package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonDocument;

public class MongoNodeIsRecoveringException extends MongoCommandException {
   private static final long serialVersionUID = 6062524147327071635L;

   public MongoNodeIsRecoveringException(BsonDocument response, ServerAddress serverAddress) {
      super(response, serverAddress);
   }
}
