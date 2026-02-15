package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonDocument;

public class MongoNotPrimaryException extends MongoCommandException {
   private static final long serialVersionUID = 694876345217027108L;

   public MongoNotPrimaryException(BsonDocument response, ServerAddress serverAddress) {
      super(response, serverAddress);
   }
}
