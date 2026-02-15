package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.internal.ExpirableValue;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.json.JsonParseException;

public final class AzureCredentialHelper {
   private static final String ACCESS_TOKEN_FIELD = "access_token";
   private static final String EXPIRES_IN_FIELD = "expires_in";
   private static final Lock CACHED_ACCESS_TOKEN_LOCK = new ReentrantLock();
   private static volatile ExpirableValue<String> cachedAccessToken = ExpirableValue.expired();

   public static BsonDocument obtainFromEnvironment() {
      Optional<String> cachedValue = cachedAccessToken.getValue();
      String accessToken;
      if (cachedValue.isPresent()) {
         accessToken = (String)cachedValue.get();
      } else {
         Locks.lockInterruptibly(CACHED_ACCESS_TOKEN_LOCK);

         try {
            cachedValue = cachedAccessToken.getValue();
            if (cachedValue.isPresent()) {
               accessToken = (String)cachedValue.get();
            } else {
               String endpoint = "http://169.254.169.254:80/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https://vault.azure.net";
               Map<String, String> headers = new HashMap();
               headers.put("Metadata", "true");
               headers.put("Accept", "application/json");
               long startNanoTime = System.nanoTime();

               BsonDocument responseDocument;
               try {
                  responseDocument = BsonDocument.parse(HttpHelper.getHttpContents("GET", endpoint, headers));
               } catch (JsonParseException e) {
                  throw new MongoClientException("Exception parsing JSON from Azure IMDS metadata response.", e);
               }

               if (!responseDocument.isString("access_token")) {
                  throw new MongoClientException(String.format("The %s field from Azure IMDS metadata response is missing or is not a string", "access_token"));
               }

               if (!responseDocument.isString("expires_in")) {
                  throw new MongoClientException(String.format("The %s field from Azure IMDS metadata response is missing or is not a string", "expires_in"));
               }

               accessToken = responseDocument.getString("access_token").getValue();
               int expiresInSeconds = Integer.parseInt(responseDocument.getString("expires_in").getValue());
               cachedAccessToken = ExpirableValue.expirable(accessToken, Duration.ofSeconds((long)expiresInSeconds).minus(Duration.ofMinutes(1L)), startNanoTime);
            }
         } finally {
            CACHED_ACCESS_TOKEN_LOCK.unlock();
         }
      }

      return new BsonDocument("accessToken", new BsonString(accessToken));
   }

   private AzureCredentialHelper() {
   }
}
