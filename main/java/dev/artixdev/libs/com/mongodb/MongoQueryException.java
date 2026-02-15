package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;

public class MongoQueryException extends MongoCommandException {
   private static final long serialVersionUID = -5113350133297015801L;

   public MongoQueryException(BsonDocument response, ServerAddress serverAddress) {
      super(response, serverAddress);
   }

   /** @deprecated */
   @Deprecated
   public MongoQueryException(ServerAddress address, int errorCode, String errorMessage) {
      this(manufactureResponse(errorCode, (String)null, errorMessage), address);
   }

   /** @deprecated */
   @Deprecated
   public MongoQueryException(ServerAddress address, int errorCode, @Nullable String errorCodeName, String errorMessage) {
      this(manufactureResponse(errorCode, errorCodeName, errorMessage), address);
   }

   /** @deprecated */
   @Deprecated
   public MongoQueryException(MongoCommandException commandException) {
      this(commandException.getResponse(), commandException.getServerAddress());
   }

   private static BsonDocument manufactureResponse(int errorCode, @Nullable String errorCodeName, String errorMessage) {
      BsonDocument response = (new BsonDocument("ok", new BsonInt32(1))).append("code", new BsonInt32(errorCode)).append("errmsg", new BsonString(errorMessage));
      if (errorCodeName != null) {
         response.append("codeName", new BsonString(errorCodeName));
      }

      return response;
   }
}
