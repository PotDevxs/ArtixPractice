package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class GcpCredentialHelper {
   public static BsonDocument obtainFromEnvironment() {
      String endpoint = "http://metadata.google.internal/computeMetadata/v1/instance/service-accounts/default/token";
      Map<String, String> header = new HashMap();
      header.put("Metadata-Flavor", "Google");
      String response = HttpHelper.getHttpContents("GET", endpoint, header);
      BsonDocument responseDocument = BsonDocument.parse(response);
      if (responseDocument.containsKey("access_token")) {
         return new BsonDocument("accessToken", responseDocument.get("access_token"));
      } else {
         throw new MongoClientException("access_token is missing from GCE metadata response.  Full response is ''" + response);
      }
   }

   private GcpCredentialHelper() {
   }
}
