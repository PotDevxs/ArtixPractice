package dev.artixdev.libs.com.mongodb;

import java.io.StringWriter;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.json.JsonWriter;

public class MongoCommandException extends MongoServerException {
   private static final long serialVersionUID = 8160676451944215078L;
   private final BsonDocument response;

   public MongoCommandException(BsonDocument response, ServerAddress address) {
      super(extractErrorCode(response), extractErrorCodeName(response), String.format("Command failed with error %s: '%s' on server %s. The full response is %s", extractErrorCodeAndName(response), extractErrorMessage(response), address, getResponseAsJson(response)), address);
      this.response = response;
      this.addLabels(response.getArray("errorLabels", new BsonArray()));
   }

   public int getErrorCode() {
      return this.getCode();
   }

   public String getErrorCodeName() {
      return super.getErrorCodeName();
   }

   public String getErrorMessage() {
      return extractErrorMessage(this.response);
   }

   public BsonDocument getResponse() {
      return this.response;
   }

   private static String getResponseAsJson(BsonDocument commandResponse) {
      StringWriter writer = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(writer);
      (new BsonDocumentCodec()).encode(jsonWriter, (BsonDocument)commandResponse, EncoderContext.builder().build());
      return writer.toString();
   }

   private static String extractErrorCodeAndName(BsonDocument response) {
      int errorCode = extractErrorCode(response);
      String errorCodeName = extractErrorCodeName(response);
      return errorCodeName.isEmpty() ? Integer.toString(errorCode) : String.format("%d (%s)", errorCode, errorCodeName);
   }

   private static int extractErrorCode(BsonDocument response) {
      return response.getNumber("code", new BsonInt32(-1)).intValue();
   }

   private static String extractErrorCodeName(BsonDocument response) {
      return response.getString("codeName", new BsonString("")).getValue();
   }

   private static String extractErrorMessage(BsonDocument response) {
      String errorMessage = response.getString("errmsg", new BsonString("")).getValue();
      if (errorMessage == null) {
         throw new MongoInternalException("This value should not be null");
      } else {
         return errorMessage;
      }
   }
}
