package dev.artixdev.libs.com.mongodb;

import java.util.HashMap;
import java.util.Map;

public enum AuthenticationMechanism {
   GSSAPI("GSSAPI"),
   MONGODB_AWS("MONGODB-AWS"),
   MONGODB_X509("MONGODB-X509"),
   PLAIN("PLAIN"),
   SCRAM_SHA_1("SCRAM-SHA-1"),
   SCRAM_SHA_256("SCRAM-SHA-256");

   private static final Map<String, AuthenticationMechanism> AUTH_MAP = new HashMap();
   private final String mechanismName;

   private AuthenticationMechanism(String mechanismName) {
      this.mechanismName = mechanismName;
   }

   public String getMechanismName() {
      return this.mechanismName;
   }

   public String toString() {
      return this.mechanismName;
   }

   public static AuthenticationMechanism fromMechanismName(String mechanismName) {
      AuthenticationMechanism mechanism = (AuthenticationMechanism)AUTH_MAP.get(mechanismName);
      if (mechanism == null) {
         throw new IllegalArgumentException("Unsupported authMechanism: " + mechanismName);
      } else {
         return mechanism;
      }
   }

   // $FF: synthetic method
   private static AuthenticationMechanism[] $values() {
      return new AuthenticationMechanism[]{GSSAPI, MONGODB_AWS, MONGODB_X509, PLAIN, SCRAM_SHA_1, SCRAM_SHA_256};
   }

   static {
      AuthenticationMechanism[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         AuthenticationMechanism value = var0[var2];
         AUTH_MAP.put(value.getMechanismName(), value);
      }

   }
}
