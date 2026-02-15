package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonDocument;

public class DuplicateKeyException extends WriteConcernException {
   private static final long serialVersionUID = -4415279469780082174L;

   public DuplicateKeyException(BsonDocument response, ServerAddress address, WriteConcernResult writeConcernResult) {
      super(response, address, writeConcernResult);
   }
}
