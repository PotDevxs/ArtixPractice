package dev.artixdev.libs.com.mongodb;

import java.util.Iterator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonValue;

public class WriteConcernException extends MongoServerException {
   private static final long serialVersionUID = -1100801000476719450L;
   private final WriteConcernResult writeConcernResult;
   private final BsonDocument response;

   public WriteConcernException(BsonDocument response, ServerAddress address, WriteConcernResult writeConcernResult) {
      super(extractErrorCode(response), String.format("Write failed with error code %d and error message '%s'", extractErrorCode(response), extractErrorMessage(response)), address);
      this.response = response;
      this.writeConcernResult = writeConcernResult;
   }

   public static int extractErrorCode(BsonDocument response) {
      String errorMessage = extractErrorMessage(response);
      if (errorMessage != null) {
         if (response.containsKey("err") && errorMessage.contains("E11000 duplicate key error")) {
            return 11000;
         }

         if (!response.containsKey("code") && response.containsKey("errObjects")) {
            Iterator var2 = response.getArray("errObjects").iterator();

            while(var2.hasNext()) {
               BsonValue curErrorDocument = (BsonValue)var2.next();
               if (errorMessage.equals(extractErrorMessage(curErrorDocument.asDocument()))) {
                  return curErrorDocument.asDocument().getNumber("code").intValue();
               }
            }
         }
      }

      return response.getNumber("code", new BsonInt32(-1)).intValue();
   }

   @Nullable
   public static String extractErrorMessage(BsonDocument response) {
      if (response.isString("err")) {
         return response.getString("err").getValue();
      } else {
         return response.isString("errmsg") ? response.getString("errmsg").getValue() : null;
      }
   }

   public WriteConcernResult getWriteConcernResult() {
      return this.writeConcernResult;
   }

   public int getErrorCode() {
      return extractErrorCode(this.response);
   }

   @Nullable
   public String getErrorMessage() {
      return extractErrorMessage(this.response);
   }

   public BsonDocument getResponse() {
      return this.response;
   }
}
