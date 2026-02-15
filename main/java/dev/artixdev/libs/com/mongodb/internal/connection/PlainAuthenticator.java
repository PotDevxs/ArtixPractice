package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

class PlainAuthenticator extends SaslAuthenticator {
   private static final String DEFAULT_PROTOCOL = "mongodb";

   PlainAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
   }

   public String getMechanismName() {
      return AuthenticationMechanism.PLAIN.getMechanismName();
   }

   protected SaslClient createSaslClient(ServerAddress serverAddress) {
      MongoCredential credential = this.getMongoCredential();
      Assertions.isTrue("mechanism is PLAIN", credential.getAuthenticationMechanism() == AuthenticationMechanism.PLAIN);

      try {
         return Sasl.createSaslClient(new String[]{AuthenticationMechanism.PLAIN.getMechanismName()}, credential.getUserName(), "mongodb", serverAddress.getHost(), (Map)null, (callbacks) -> {
            Callback[] var2 = callbacks;
            int var3 = callbacks.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Callback callback = var2[var4];
               if (callback instanceof PasswordCallback) {
                  ((PasswordCallback)callback).setPassword(credential.getPassword());
               } else if (callback instanceof NameCallback) {
                  ((NameCallback)callback).setName(credential.getUserName());
               }
            }

         });
      } catch (SaslException e) {
         throw new MongoSecurityException(credential, "Exception initializing SASL client", e);
      }
   }
}
