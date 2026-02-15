package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Beta({Beta.Reason.CLIENT})
public final class AwsCredential {
   private final String accessKeyId;
   private final String secretAccessKey;
   private final String sessionToken;

   public AwsCredential(String accessKeyId, String secretAccessKey, @Nullable String sessionToken) {
      this.accessKeyId = (String)Assertions.notNull("accessKeyId", accessKeyId);
      this.secretAccessKey = (String)Assertions.notNull("secretAccessKey", secretAccessKey);
      this.sessionToken = sessionToken;
   }

   public String getAccessKeyId() {
      return this.accessKeyId;
   }

   public String getSecretAccessKey() {
      return this.secretAccessKey;
   }

   @Nullable
   public String getSessionToken() {
      return this.sessionToken;
   }
}
