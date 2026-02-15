package dev.artixdev.libs.com.mongodb.internal.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.KerberosSubjectProvider;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.SubjectProvider;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

class GSSAPIAuthenticator extends SaslAuthenticator {
   private static final String GSSAPI_MECHANISM_NAME = "GSSAPI";
   private static final String GSSAPI_OID = "1.2.840.113554.1.2.2";
   private static final String SERVICE_NAME_DEFAULT_VALUE = "mongodb";
   private static final Boolean CANONICALIZE_HOST_NAME_DEFAULT_VALUE = false;

   GSSAPIAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
      if (this.getMongoCredential().getAuthenticationMechanism() != AuthenticationMechanism.GSSAPI) {
         throw new MongoException("Incorrect mechanism: " + this.getMongoCredential().getMechanism());
      }
   }

   public String getMechanismName() {
      return "GSSAPI";
   }

   protected SaslClient createSaslClient(ServerAddress serverAddress) {
      MongoCredential credential = this.getMongoCredential();

      try {
         Map<String, Object> saslClientProperties = (Map)credential.getMechanismProperty("JAVA_SASL_CLIENT_PROPERTIES", (Object)null);
         if (saslClientProperties == null) {
            saslClientProperties = new HashMap();
            ((Map)saslClientProperties).put("javax.security.sasl.maxbuffer", "0");
            ((Map)saslClientProperties).put("javax.security.sasl.credentials", this.getGSSCredential((String)Assertions.assertNotNull(credential.getUserName())));
         }

         SaslClient saslClient = Sasl.createSaslClient(new String[]{AuthenticationMechanism.GSSAPI.getMechanismName()}, credential.getUserName(), (String)credential.getMechanismProperty("SERVICE_NAME", "mongodb"), this.getHostName(serverAddress), (Map)saslClientProperties, (CallbackHandler)null);
         if (saslClient == null) {
            throw new MongoSecurityException(credential, String.format("No platform support for %s mechanism", AuthenticationMechanism.GSSAPI));
         } else {
            return saslClient;
         }
      } catch (SaslException e) {
         throw new MongoSecurityException(credential, "Exception initializing SASL client", e);
      } catch (GSSException e) {
         throw new MongoSecurityException(credential, "Exception initializing GSSAPI credentials", e);
      } catch (UnknownHostException e) {
         throw new MongoSecurityException(credential, "Unable to canonicalize host name + " + serverAddress);
      }
   }

   private GSSCredential getGSSCredential(String userName) throws GSSException {
      Oid krb5Mechanism = new Oid("1.2.840.113554.1.2.2");
      GSSManager manager = GSSManager.getInstance();
      GSSName name = manager.createName(userName, GSSName.NT_USER_NAME);
      return manager.createCredential(name, Integer.MAX_VALUE, krb5Mechanism, 1);
   }

   private String getHostName(ServerAddress serverAddress) throws UnknownHostException {
      return (Boolean)this.getNonNullMechanismProperty("CANONICALIZE_HOST_NAME", CANONICALIZE_HOST_NAME_DEFAULT_VALUE) ? InetAddress.getByName(serverAddress.getHost()).getCanonicalHostName() : serverAddress.getHost();
   }

   @NonNull
   protected SubjectProvider getDefaultSubjectProvider() {
      return new KerberosSubjectProvider();
   }
}
