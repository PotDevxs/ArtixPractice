package dev.artixdev.libs.com.mongodb.internal.connection;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Supplier;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.AwsCredential;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.internal.authentication.AwsCredentialHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonBinaryWriter;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.io.BasicOutputBuffer;

public class AwsAuthenticator extends SaslAuthenticator {
   private static final String MONGODB_AWS_MECHANISM_NAME = "MONGODB-AWS";
   private static final int RANDOM_LENGTH = 32;

   public AwsAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
      if (this.getMongoCredential().getAuthenticationMechanism() != AuthenticationMechanism.MONGODB_AWS) {
         throw new MongoException("Incorrect mechanism: " + this.getMongoCredential().getMechanism());
      }
   }

   public String getMechanismName() {
      return "MONGODB-AWS";
   }

   protected SaslClient createSaslClient(ServerAddress serverAddress) {
      return new AwsAuthenticator.AwsSaslClient(this.getMongoCredential());
   }

   private static byte[] toBson(BsonDocument document) {
      BasicOutputBuffer buffer = new BasicOutputBuffer();
      (new BsonDocumentCodec()).encode(new BsonBinaryWriter(buffer), (BsonDocument)document, EncoderContext.builder().build());
      byte[] bytes = new byte[buffer.size()];
      System.arraycopy(buffer.getInternalBuffer(), 0, bytes, 0, buffer.getSize());
      return bytes;
   }

   private static class AwsSaslClient implements SaslClient {
      private final MongoCredential credential;
      private final byte[] clientNonce = new byte[32];
      private int step = -1;

      AwsSaslClient(MongoCredential credential) {
         this.credential = credential;
      }

      public String getMechanismName() {
         AuthenticationMechanism authMechanism = this.credential.getAuthenticationMechanism();
         if (authMechanism == null) {
            throw new IllegalArgumentException("Authentication mechanism cannot be null");
         } else {
            return authMechanism.getMechanismName();
         }
      }

      public boolean hasInitialResponse() {
         return true;
      }

      public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
         ++this.step;
         if (this.step == 0) {
            return this.computeClientFirstMessage();
         } else if (this.step == 1) {
            return this.computeClientFinalMessage(challenge);
         } else {
            throw new SaslException(String.format("Too many steps involved in the %s negotiation.", this.getMechanismName()));
         }
      }

      public boolean isComplete() {
         return this.step == 1;
      }

      public byte[] unwrap(byte[] bytes, int i, int i1) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public byte[] wrap(byte[] bytes, int i, int i1) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public Object getNegotiatedProperty(String s) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public void dispose() {
      }

      private byte[] computeClientFirstMessage() {
         (new SecureRandom()).nextBytes(this.clientNonce);
         BsonDocument document = (new BsonDocument()).append("r", new BsonBinary(this.clientNonce)).append("p", new BsonInt32(110));
         return AwsAuthenticator.toBson(document);
      }

      private byte[] computeClientFinalMessage(byte[] serverFirst) throws SaslException {
         BsonDocument document = new RawBsonDocument(serverFirst);
         String host = document.getString("h").getValue();
         byte[] serverNonce = document.getBinary("s").getData();
         if (serverNonce.length != 64) {
            throw new SaslException(String.format("Server nonce must be %d bytes", 64));
         } else if (!Arrays.equals(Arrays.copyOf(serverNonce, 32), this.clientNonce)) {
            throw new SaslException(String.format("The first %d bytes of the server nonce must be the client nonce", 32));
         } else {
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.of("UTC")).format(Instant.now());
            AwsCredential awsCredential = this.createAwsCredential();
            String sessionToken = awsCredential.getSessionToken();
            AuthorizationHeader authorizationHeader = AuthorizationHeader.builder().setAccessKeyID(awsCredential.getAccessKeyId()).setSecretKey(awsCredential.getSecretAccessKey()).setSessionToken(sessionToken).setHost(host).setNonce(serverNonce).setTimestamp(timestamp).build();
            BsonDocument ret = (new BsonDocument()).append("a", new BsonString(authorizationHeader.toString())).append("d", new BsonString(authorizationHeader.getTimestamp()));
            if (sessionToken != null) {
               ret.append("t", new BsonString(sessionToken));
            }

            return AwsAuthenticator.toBson(ret);
         }
      }

      private AwsCredential createAwsCredential() {
         AwsCredential awsCredential;
         if (this.credential.getUserName() != null) {
            if (this.credential.getPassword() == null) {
               throw new MongoClientException("secretAccessKey is required for AWS credential");
            }

            awsCredential = new AwsCredential((String)Assertions.assertNotNull(this.credential.getUserName()), new String((char[])Assertions.assertNotNull(this.credential.getPassword())), (String)this.credential.getMechanismProperty("AWS_SESSION_TOKEN", (Object)null));
         } else if (this.credential.getMechanismProperty("AWS_CREDENTIAL_PROVIDER", (Object)null) != null) {
            Supplier<AwsCredential> awsCredentialSupplier = (Supplier)Assertions.assertNotNull((Supplier)this.credential.getMechanismProperty("AWS_CREDENTIAL_PROVIDER", (Object)null));
            awsCredential = (AwsCredential)awsCredentialSupplier.get();
            if (awsCredential == null) {
               throw new MongoClientException("AWS_CREDENTIAL_PROVIDER_KEY must return an AwsCredential instance");
            }
         } else {
            awsCredential = AwsCredentialHelper.obtainFromEnvironment();
            if (awsCredential == null) {
               throw new MongoClientException("Unable to obtain AWS credential from the environment");
            }
         }

         return awsCredential;
      }
   }
}
