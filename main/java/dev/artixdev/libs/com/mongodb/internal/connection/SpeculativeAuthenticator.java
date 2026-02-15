package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;

interface SpeculativeAuthenticator {
   @Nullable
   default BsonDocument createSpeculativeAuthenticateCommand(InternalConnection connection) {
      return null;
   }

   @Nullable
   default BsonDocument getSpeculativeAuthenticateResponse() {
      return null;
   }

   default void setSpeculativeAuthenticateResponse(BsonDocument response) {
   }
}
