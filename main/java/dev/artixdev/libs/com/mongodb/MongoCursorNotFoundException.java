package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonDocument;

public class MongoCursorNotFoundException extends MongoQueryException {
   private static final long serialVersionUID = -4415279469780082174L;
   private final long cursorId;

   public MongoCursorNotFoundException(long cursorId, BsonDocument response, ServerAddress serverAddress) {
      super(response, serverAddress);
      this.cursorId = cursorId;
   }

   /** @deprecated */
   @Deprecated
   public MongoCursorNotFoundException(long cursorId, ServerAddress serverAddress) {
      super(serverAddress, -5, "Cursor " + cursorId + " not found on server " + serverAddress);
      this.cursorId = cursorId;
   }

   public long getCursorId() {
      return this.cursorId;
   }
}
