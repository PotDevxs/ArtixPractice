package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public abstract class Authenticator {
   private final MongoCredentialWithCache credential;
   private final ClusterConnectionMode clusterConnectionMode;
   private final ServerApi serverApi;

   Authenticator(@NonNull MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      this.credential = credential;
      this.clusterConnectionMode = (ClusterConnectionMode)Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
      this.serverApi = serverApi;
   }

   @NonNull
   MongoCredentialWithCache getMongoCredentialWithCache() {
      return this.credential;
   }

   @NonNull
   MongoCredential getMongoCredential() {
      return this.credential.getCredential();
   }

   ClusterConnectionMode getClusterConnectionMode() {
      return this.clusterConnectionMode;
   }

   @Nullable
   ServerApi getServerApi() {
      return this.serverApi;
   }

   @NonNull
   String getUserNameNonNull() {
      String userName = this.credential.getCredential().getUserName();
      if (userName == null) {
         throw new MongoInternalException("User name can not be null");
      } else {
         return userName;
      }
   }

   @NonNull
   char[] getPasswordNonNull() {
      char[] password = this.credential.getCredential().getPassword();
      if (password == null) {
         throw new MongoInternalException("Password can not be null");
      } else {
         return password;
      }
   }

   @NonNull
   <T> T getNonNullMechanismProperty(String key, @Nullable T defaultValue) {
      T mechanismProperty = this.credential.getCredential().getMechanismProperty(key, defaultValue);
      if (mechanismProperty == null) {
         throw new MongoInternalException("Mechanism property can not be null");
      } else {
         return mechanismProperty;
      }
   }

   abstract void authenticate(InternalConnection var1, ConnectionDescription var2);

   abstract void authenticateAsync(InternalConnection var1, ConnectionDescription var2, SingleResultCallback<Void> var3);
}
