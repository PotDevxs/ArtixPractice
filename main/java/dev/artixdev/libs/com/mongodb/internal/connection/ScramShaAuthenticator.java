package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.internal.authentication.NativeAuthenticationHelper;
import dev.artixdev.libs.com.mongodb.internal.authentication.SaslPrep;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

class ScramShaAuthenticator extends SaslAuthenticator {
   private final ScramShaAuthenticator.RandomStringGenerator randomStringGenerator;
   private final ScramShaAuthenticator.AuthenticationHashGenerator authenticationHashGenerator;
   private SaslClient speculativeSaslClient;
   private BsonDocument speculativeAuthenticateResponse;
   private static final int MINIMUM_ITERATION_COUNT = 4096;
   private static final String GS2_HEADER = "n,,";
   private static final int RANDOM_LENGTH = 24;
   private static final byte[] INT_1 = new byte[]{0, 0, 0, 1};
   private static final ScramShaAuthenticator.AuthenticationHashGenerator DEFAULT_AUTHENTICATION_HASH_GENERATOR = (credential) -> {
      char[] password = credential.getPassword();
      if (password == null) {
         throw new IllegalArgumentException("Password must not be null");
      } else {
         return new String(password);
      }
   };
   private static final ScramShaAuthenticator.AuthenticationHashGenerator LEGACY_AUTHENTICATION_HASH_GENERATOR = (credential) -> {
      String username = credential.getUserName();
      char[] password = credential.getPassword();
      if (username != null && password != null) {
         return NativeAuthenticationHelper.createAuthenticationHash(username, password);
      } else {
         throw new IllegalArgumentException("Username and password must not be null");
      }
   };

   ScramShaAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      this(credential, new ScramShaAuthenticator.DefaultRandomStringGenerator(), getAuthenicationHashGenerator((AuthenticationMechanism)Assertions.assertNotNull(credential.getAuthenticationMechanism())), clusterConnectionMode, serverApi);
   }

   ScramShaAuthenticator(MongoCredentialWithCache credential, ScramShaAuthenticator.RandomStringGenerator randomStringGenerator, ScramShaAuthenticator.AuthenticationHashGenerator authenticationHashGenerator, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
      this.randomStringGenerator = randomStringGenerator;
      this.authenticationHashGenerator = authenticationHashGenerator;
   }

   public String getMechanismName() {
      AuthenticationMechanism authMechanism = this.getMongoCredential().getAuthenticationMechanism();
      if (authMechanism == null) {
         throw new IllegalArgumentException("Authentication mechanism cannot be null");
      } else {
         return authMechanism.getMechanismName();
      }
   }

   protected void appendSaslStartOptions(BsonDocument saslStartCommand) {
      saslStartCommand.append("options", new BsonDocument("skipEmptyExchange", new BsonBoolean(true)));
   }

   protected SaslClient createSaslClient(ServerAddress serverAddress) {
      return (SaslClient)(this.speculativeSaslClient != null ? this.speculativeSaslClient : new ScramShaAuthenticator.ScramShaSaslClient(this.getMongoCredentialWithCache(), this.randomStringGenerator, this.authenticationHashGenerator));
   }

   public BsonDocument createSpeculativeAuthenticateCommand(InternalConnection connection) {
      try {
         this.speculativeSaslClient = this.createSaslClient(connection.getDescription().getServerAddress());
         BsonDocument startDocument = this.createSaslStartCommandDocument(this.speculativeSaslClient.evaluateChallenge(new byte[0])).append("db", new BsonString(this.getMongoCredential().getSource()));
         this.appendSaslStartOptions(startDocument);
         return startDocument;
      } catch (Exception e) {
         throw this.wrapException(e);
      }
   }

   public BsonDocument getSpeculativeAuthenticateResponse() {
      return this.speculativeAuthenticateResponse;
   }

   public void setSpeculativeAuthenticateResponse(@Nullable BsonDocument response) {
      if (response == null) {
         this.speculativeSaslClient = null;
      } else {
         this.speculativeAuthenticateResponse = response;
      }

   }

   private static ScramShaAuthenticator.AuthenticationHashGenerator getAuthenicationHashGenerator(AuthenticationMechanism authenticationMechanism) {
      return authenticationMechanism == AuthenticationMechanism.SCRAM_SHA_1 ? LEGACY_AUTHENTICATION_HASH_GENERATOR : DEFAULT_AUTHENTICATION_HASH_GENERATOR;
   }

   private static class DefaultRandomStringGenerator implements ScramShaAuthenticator.RandomStringGenerator {
      private DefaultRandomStringGenerator() {
      }

      public String generate(int length) {
         Random random = new SecureRandom();
         int comma = 44;
         int low = 33;
         int high = 126;
         int range = high - low;
         char[] text = new char[length];

         for(int i = 0; i < length; ++i) {
            int next;
            for(next = random.nextInt(range) + low; next == comma; next = random.nextInt(range) + low) {
            }

            text[i] = (char)next;
         }

         return new String(text);
      }

      // $FF: synthetic method
      DefaultRandomStringGenerator(Object x0) {
         this();
      }
   }

   public interface AuthenticationHashGenerator {
      String generate(MongoCredential var1);
   }

   public interface RandomStringGenerator {
      String generate(int var1);
   }

   class ScramShaSaslClient implements SaslClient {
      private final MongoCredentialWithCache credential;
      private final ScramShaAuthenticator.RandomStringGenerator randomStringGenerator;
      private final ScramShaAuthenticator.AuthenticationHashGenerator authenticationHashGenerator;
      private final String hAlgorithm;
      private final String hmacAlgorithm;
      private String clientFirstMessageBare;
      private String clientNonce;
      private byte[] serverSignature;
      private int step = -1;

      ScramShaSaslClient(MongoCredentialWithCache credential, ScramShaAuthenticator.RandomStringGenerator randomStringGenerator, ScramShaAuthenticator.AuthenticationHashGenerator authenticationHashGenerator) {
         this.credential = credential;
         this.randomStringGenerator = randomStringGenerator;
         this.authenticationHashGenerator = authenticationHashGenerator;
         if (((AuthenticationMechanism)Assertions.assertNotNull(credential.getAuthenticationMechanism())).equals(AuthenticationMechanism.SCRAM_SHA_1)) {
            this.hAlgorithm = "SHA-1";
            this.hmacAlgorithm = "HmacSHA1";
         } else {
            this.hAlgorithm = "SHA-256";
            this.hmacAlgorithm = "HmacSHA256";
         }

      }

      public String getMechanismName() {
         return ((AuthenticationMechanism)Assertions.assertNotNull(this.credential.getAuthenticationMechanism())).getMechanismName();
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
         } else if (this.step == 2) {
            return this.validateServerSignature(challenge);
         } else {
            throw new SaslException(String.format("Too many steps involved in the %s negotiation.", this.getMechanismName()));
         }
      }

      private byte[] validateServerSignature(byte[] challenge) throws SaslException {
         String serverResponse = new String(challenge, StandardCharsets.UTF_8);
         HashMap<String, String> map = this.parseServerResponse(serverResponse);
         if (!MessageDigest.isEqual(Base64.getDecoder().decode((String)map.get("v")), this.serverSignature)) {
            throw new SaslException("Server signature was invalid.");
         } else {
            return new byte[0];
         }
      }

      public boolean isComplete() {
         return this.step == 2;
      }

      public byte[] unwrap(byte[] incoming, int offset, int len) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public byte[] wrap(byte[] outgoing, int offset, int len) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public Object getNegotiatedProperty(String propName) {
         throw new UnsupportedOperationException("Not implemented yet!");
      }

      public void dispose() {
      }

      private byte[] computeClientFirstMessage() {
         this.clientNonce = this.randomStringGenerator.generate(24);
         String clientFirstMessage = "n=" + this.getUserName() + ",r=" + this.clientNonce;
         this.clientFirstMessageBare = clientFirstMessage;
         return ("n,," + clientFirstMessage).getBytes(StandardCharsets.UTF_8);
      }

      private byte[] computeClientFinalMessage(byte[] challenge) throws SaslException {
         String serverFirstMessage = new String(challenge, StandardCharsets.UTF_8);
         HashMap<String, String> map = this.parseServerResponse(serverFirstMessage);
         String serverNonce = (String)map.get("r");
         if (!serverNonce.startsWith(this.clientNonce)) {
            throw new SaslException("Server sent an invalid nonce.");
         } else {
            String salt = (String)map.get("s");
            int iterationCount = Integer.parseInt((String)map.get("i"));
            if (iterationCount < 4096) {
               throw new SaslException("Invalid iteration count.");
            } else {
               String clientFinalMessageWithoutProof = "c=" + Base64.getEncoder().encodeToString("n,,".getBytes(StandardCharsets.UTF_8)) + ",r=" + serverNonce;
               String authMessage = this.clientFirstMessageBare + "," + serverFirstMessage + "," + clientFinalMessageWithoutProof;
               String clientFinalMessage = clientFinalMessageWithoutProof + ",p=" + this.getClientProof(this.getAuthenicationHash(), salt, iterationCount, authMessage);
               return clientFinalMessage.getBytes(StandardCharsets.UTF_8);
            }
         }
      }

      String getClientProof(String password, String salt, int iterationCount, String authMessage) throws SaslException {
         String hashedPasswordAndSalt = new String(this.h((password + salt).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
         ScramShaAuthenticator.CacheKey cacheKey = new ScramShaAuthenticator.CacheKey(hashedPasswordAndSalt, salt, iterationCount);
         ScramShaAuthenticator.CacheValue cachedKeys = (ScramShaAuthenticator.CacheValue)ScramShaAuthenticator.this.getMongoCredentialWithCache().getFromCache(cacheKey, ScramShaAuthenticator.CacheValue.class);
         byte[] storedKey;
         byte[] clientSignature;
         byte[] clientProof;
         if (cachedKeys == null) {
            storedKey = this.hi(password.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(salt), iterationCount);
            clientSignature = this.hmac(storedKey, "Client Key");
            clientProof = this.hmac(storedKey, "Server Key");
            cachedKeys = new ScramShaAuthenticator.CacheValue(clientSignature, clientProof);
            ScramShaAuthenticator.this.getMongoCredentialWithCache().putInCache(cacheKey, new ScramShaAuthenticator.CacheValue(clientSignature, clientProof));
         }

         this.serverSignature = this.hmac(cachedKeys.serverKey, authMessage);
         storedKey = this.h(cachedKeys.clientKey);
         clientSignature = this.hmac(storedKey, authMessage);
         clientProof = this.xor(cachedKeys.clientKey, clientSignature);
         return Base64.getEncoder().encodeToString(clientProof);
      }

      private byte[] h(byte[] data) throws SaslException {
         try {
            return MessageDigest.getInstance(this.hAlgorithm).digest(data);
         } catch (NoSuchAlgorithmException e) {
            throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hAlgorithm), e);
         }
      }

      private byte[] hi(byte[] password, byte[] salt, int iterations) throws SaslException {
         try {
            SecretKeySpec key = new SecretKeySpec(password, this.hmacAlgorithm);
            Mac mac = Mac.getInstance(this.hmacAlgorithm);
            mac.init(key);
            mac.update(salt);
            mac.update(ScramShaAuthenticator.INT_1);
            byte[] result = mac.doFinal();
            byte[] previous = null;

            for(int i = 1; i < iterations; ++i) {
               mac.update(previous != null ? previous : result);
               previous = mac.doFinal();
               this.xorInPlace(result, previous);
            }

            return result;
         } catch (NoSuchAlgorithmException e) {
            throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hmacAlgorithm), e);
         } catch (InvalidKeyException e) {
            throw new SaslException(String.format("Invalid key for %s", this.hmacAlgorithm), e);
         }
      }

      private byte[] hmac(byte[] bytes, String key) throws SaslException {
         try {
            Mac mac = Mac.getInstance(this.hmacAlgorithm);
            mac.init(new SecretKeySpec(bytes, this.hmacAlgorithm));
            return mac.doFinal(key.getBytes(StandardCharsets.UTF_8));
         } catch (NoSuchAlgorithmException e) {
            throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hmacAlgorithm), e);
         } catch (InvalidKeyException e) {
            throw new SaslException("Could not initialize mac.", e);
         }
      }

      private HashMap<String, String> parseServerResponse(String response) {
         HashMap<String, String> map = new HashMap();
         String[] pairs = response.split(",");
         String[] var4 = pairs;
         int var5 = pairs.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String pair = var4[var6];
            String[] parts = pair.split("=", 2);
            map.put(parts[0], parts[1]);
         }

         return map;
      }

      private String getUserName() {
         String userName = this.credential.getCredential().getUserName();
         if (userName == null) {
            throw new IllegalArgumentException("Username can not be null");
         } else {
            return userName.replace("=", "=3D").replace(",", "=2C");
         }
      }

      private String getAuthenicationHash() {
         String password = this.authenticationHashGenerator.generate(this.credential.getCredential());
         if (this.credential.getAuthenticationMechanism() == AuthenticationMechanism.SCRAM_SHA_256) {
            password = SaslPrep.saslPrepStored(password);
         }

         return password;
      }

      private byte[] xorInPlace(byte[] a, byte[] b) {
         for(int i = 0; i < a.length; ++i) {
            a[i] ^= b[i];
         }

         return a;
      }

      private byte[] xor(byte[] a, byte[] b) {
         byte[] result = new byte[a.length];
         System.arraycopy(a, 0, result, 0, a.length);
         return this.xorInPlace(result, b);
      }
   }

   private static class CacheValue {
      private final byte[] clientKey;
      private final byte[] serverKey;

      CacheValue(byte[] clientKey, byte[] serverKey) {
         this.clientKey = clientKey;
         this.serverKey = serverKey;
      }
   }

   private static class CacheKey {
      private final String hashedPasswordAndSalt;
      private final String salt;
      private final int iterationCount;

      CacheKey(String hashedPasswordAndSalt, String salt, int iterationCount) {
         this.hashedPasswordAndSalt = hashedPasswordAndSalt;
         this.salt = salt;
         this.iterationCount = iterationCount;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            ScramShaAuthenticator.CacheKey that = (ScramShaAuthenticator.CacheKey)o;
            if (this.iterationCount != that.iterationCount) {
               return false;
            } else {
               return !this.hashedPasswordAndSalt.equals(that.hashedPasswordAndSalt) ? false : this.salt.equals(that.salt);
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.hashedPasswordAndSalt.hashCode();
         result = 31 * result + this.salt.hashCode();
         result = 31 * result + this.iterationCount;
         return result;
      }
   }
}
