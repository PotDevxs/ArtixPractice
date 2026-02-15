package dev.artixdev.libs.com.mongodb;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class MongoCredential {
   private final AuthenticationMechanism mechanism;
   private final String userName;
   private final String source;
   private final char[] password;
   private final Map<String, Object> mechanismProperties;
   public static final String GSSAPI_MECHANISM;
   public static final String PLAIN_MECHANISM;
   public static final String MONGODB_X509_MECHANISM;
   public static final String SCRAM_SHA_1_MECHANISM;
   public static final String SCRAM_SHA_256_MECHANISM;
   public static final String SERVICE_NAME_KEY = "SERVICE_NAME";
   public static final String CANONICALIZE_HOST_NAME_KEY = "CANONICALIZE_HOST_NAME";
   public static final String JAVA_SASL_CLIENT_PROPERTIES_KEY = "JAVA_SASL_CLIENT_PROPERTIES";
   public static final String JAVA_SUBJECT_PROVIDER_KEY = "JAVA_SUBJECT_PROVIDER";
   public static final String JAVA_SUBJECT_KEY = "JAVA_SUBJECT";
   public static final String AWS_SESSION_TOKEN_KEY = "AWS_SESSION_TOKEN";
   @Beta({Beta.Reason.CLIENT})
   public static final String AWS_CREDENTIAL_PROVIDER_KEY = "AWS_CREDENTIAL_PROVIDER";

   public static MongoCredential createCredential(String userName, String database, char[] password) {
      return new MongoCredential((AuthenticationMechanism)null, userName, database, password);
   }

   public static MongoCredential createScramSha1Credential(String userName, String source, char[] password) {
      return new MongoCredential(AuthenticationMechanism.SCRAM_SHA_1, userName, source, password);
   }

   public static MongoCredential createScramSha256Credential(String userName, String source, char[] password) {
      return new MongoCredential(AuthenticationMechanism.SCRAM_SHA_256, userName, source, password);
   }

   public static MongoCredential createMongoX509Credential(String userName) {
      return new MongoCredential(AuthenticationMechanism.MONGODB_X509, userName, "$external", (char[])null);
   }

   public static MongoCredential createMongoX509Credential() {
      return new MongoCredential(AuthenticationMechanism.MONGODB_X509, (String)null, "$external", (char[])null);
   }

   public static MongoCredential createPlainCredential(String userName, String source, char[] password) {
      return new MongoCredential(AuthenticationMechanism.PLAIN, userName, source, password);
   }

   public static MongoCredential createGSSAPICredential(String userName) {
      return new MongoCredential(AuthenticationMechanism.GSSAPI, userName, "$external", (char[])null);
   }

   public static MongoCredential createAwsCredential(@Nullable String userName, @Nullable char[] password) {
      return new MongoCredential(AuthenticationMechanism.MONGODB_AWS, userName, "$external", password);
   }

   public <T> MongoCredential withMechanismProperty(String key, T value) {
      return new MongoCredential(this, key, value);
   }

   public MongoCredential withMechanism(AuthenticationMechanism mechanism) {
      if (this.mechanism != null) {
         throw new IllegalArgumentException("Mechanism already set");
      } else {
         return new MongoCredential(mechanism, this.userName, this.source, this.password, this.mechanismProperties);
      }
   }

   MongoCredential(@Nullable AuthenticationMechanism mechanism, @Nullable String userName, String source, @Nullable char[] password) {
      this(mechanism, userName, source, password, Collections.emptyMap());
   }

   MongoCredential(@Nullable AuthenticationMechanism mechanism, @Nullable String userName, String source, @Nullable char[] password, Map<String, Object> mechanismProperties) {
      if (userName == null && !Arrays.asList(AuthenticationMechanism.MONGODB_X509, AuthenticationMechanism.MONGODB_AWS).contains(mechanism)) {
         throw new IllegalArgumentException("username can not be null");
      } else if (mechanism == null && password == null) {
         throw new IllegalArgumentException("Password can not be null when the authentication mechanism is unspecified");
      } else if (this.mechanismRequiresPassword(mechanism) && password == null) {
         throw new IllegalArgumentException("Password can not be null for " + mechanism + " mechanism");
      } else if ((mechanism == AuthenticationMechanism.GSSAPI || mechanism == AuthenticationMechanism.MONGODB_X509) && password != null) {
         throw new IllegalArgumentException("Password must be null for the " + mechanism + " mechanism");
      } else if (mechanism == AuthenticationMechanism.MONGODB_AWS && userName != null && password == null) {
         throw new IllegalArgumentException("Password can not be null when username is provided for " + mechanism + " mechanism");
      } else {
         this.mechanism = mechanism;
         this.userName = userName;
         this.source = (String)Assertions.notNull("source", source);
         this.password = password != null ? (char[])password.clone() : null;
         this.mechanismProperties = new HashMap(mechanismProperties);
      }
   }

   private boolean mechanismRequiresPassword(@Nullable AuthenticationMechanism mechanism) {
      return mechanism == AuthenticationMechanism.PLAIN || mechanism == AuthenticationMechanism.SCRAM_SHA_1 || mechanism == AuthenticationMechanism.SCRAM_SHA_256;
   }

   <T> MongoCredential(MongoCredential from, String mechanismPropertyKey, T mechanismPropertyValue) {
      this(from.mechanism, from.userName, from.source, from.password, mapWith(from.mechanismProperties, ((String)Assertions.notNull("mechanismPropertyKey", mechanismPropertyKey)).toLowerCase(), mechanismPropertyValue));
   }

   private static <T> Map<String, Object> mapWith(Map<String, Object> map, String key, T value) {
      HashMap<String, Object> result = new HashMap(map);
      result.put(key, value);
      return result;
   }

   @Nullable
   public String getMechanism() {
      return this.mechanism == null ? null : this.mechanism.getMechanismName();
   }

   @Nullable
   public AuthenticationMechanism getAuthenticationMechanism() {
      return this.mechanism;
   }

   @Nullable
   public String getUserName() {
      return this.userName;
   }

   public String getSource() {
      return this.source;
   }

   @Nullable
   public char[] getPassword() {
      return this.password == null ? null : (char[])this.password.clone();
   }

   @Nullable
   public <T> T getMechanismProperty(String key, @Nullable T defaultValue) {
      Assertions.notNull("key", key);
      Object raw = this.mechanismProperties.get(key.toLowerCase());
      return raw == null ? defaultValue : (T) raw;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MongoCredential that = (MongoCredential)o;
         if (this.mechanism != that.mechanism) {
            return false;
         } else if (!Arrays.equals(this.password, that.password)) {
            return false;
         } else if (!this.source.equals(that.source)) {
            return false;
         } else if (!Objects.equals(this.userName, that.userName)) {
            return false;
         } else {
            return this.mechanismProperties.equals(that.mechanismProperties);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.mechanism != null ? this.mechanism.hashCode() : 0;
      result = 31 * result + (this.userName != null ? this.userName.hashCode() : 0);
      result = 31 * result + this.source.hashCode();
      result = 31 * result + (this.password != null ? Arrays.hashCode(this.password) : 0);
      result = 31 * result + this.mechanismProperties.hashCode();
      return result;
   }

   public String toString() {
      return "MongoCredential{mechanism=" + this.mechanism + ", userName='" + this.userName + '\'' + ", source='" + this.source + '\'' + ", password=<hidden>, mechanismProperties=<hidden>" + '}';
   }

   static {
      GSSAPI_MECHANISM = AuthenticationMechanism.GSSAPI.getMechanismName();
      PLAIN_MECHANISM = AuthenticationMechanism.PLAIN.getMechanismName();
      MONGODB_X509_MECHANISM = AuthenticationMechanism.MONGODB_X509.getMechanismName();
      SCRAM_SHA_1_MECHANISM = AuthenticationMechanism.SCRAM_SHA_1.getMechanismName();
      SCRAM_SHA_256_MECHANISM = AuthenticationMechanism.SCRAM_SHA_256.getMechanismName();
   }
}
