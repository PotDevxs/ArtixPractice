package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslException;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class AuthorizationHeader {
   private static final String AWS4_HMAC_SHA256 = "AWS4-HMAC-SHA256";
   private static final String SERVICE = "sts";
   private final String host;
   private final String timestamp;
   private final String signature;
   private final String sessionToken;
   private final String authorizationHeader;
   private final byte[] nonce;
   private final Map<String, String> requestHeaders;
   private final String body;

   private AuthorizationHeader(AuthorizationHeader.Builder builder) throws SaslException {
      this.sessionToken = builder.sessionToken;
      this.host = builder.host;
      this.timestamp = builder.timestamp;
      this.nonce = builder.nonce;
      this.body = "Action=GetCallerIdentity&Version=2011-06-15";
      this.requestHeaders = this.getRequestHeaders();
      String canonicalRequest = createCanonicalRequest("POST", "", this.body, this.requestHeaders);
      String toSign = createStringToSign(hash(canonicalRequest), this.getTimestamp(), this.getCredentialScope());
      this.signature = calculateSignature(toSign, builder.secretKey, this.getDate(), getRegion(this.host), "sts");
      this.authorizationHeader = String.format("%s Credential=%s/%s, SignedHeaders=%s, Signature=%s", "AWS4-HMAC-SHA256", builder.accessKeyID, this.getCredentialScope(), getSignedHeaders(this.requestHeaders), this.getSignature());
   }

   static String createCanonicalRequest(String method, String query, String body, Map<String, String> requestHeaders) throws SaslException {
      String headers = getCanonicalHeaders(requestHeaders);
      String signedHeaders = getSignedHeaders(requestHeaders);
      List<String> request = Arrays.asList(method, "/", query, headers, signedHeaders, hash(body));
      return String.join("\n", request);
   }

   static String createStringToSign(String hash, String timestamp, String credentialScope) {
      List<String> toSign = Arrays.asList("AWS4-HMAC-SHA256", timestamp, credentialScope, hash);
      return String.join("\n", toSign);
   }

   static String calculateSignature(String toSign, String secret, String date, String region, String service) throws SaslException {
      byte[] kDate = hmac(decodeUTF8("AWS4" + secret), decodeUTF8(date));
      byte[] kRegion = hmac(kDate, decodeUTF8(region));
      byte[] kService = hmac(kRegion, decodeUTF8(service));
      byte[] kSigning = hmac(kService, decodeUTF8("aws4_request"));
      return hexEncode(hmac(kSigning, decodeUTF8(toSign)));
   }

   private Map<String, String> getRequestHeaders() {
      if (this.requestHeaders != null) {
         return this.requestHeaders;
      } else {
         Map<String, String> requestHeaders = new HashMap();
         requestHeaders.put("Content-Type", "application/x-www-form-urlencoded");
         requestHeaders.put("Content-Length", String.valueOf(this.body.length()));
         requestHeaders.put("Host", this.host);
         requestHeaders.put("X-Amz-Date", this.timestamp);
         requestHeaders.put("X-MongoDB-Server-Nonce", Base64.getEncoder().encodeToString(this.nonce));
         requestHeaders.put("X-MongoDB-GS2-CB-Flag", "n");
         if (this.sessionToken != null) {
            requestHeaders.put("X-Amz-Security-Token", this.sessionToken);
         }

         return requestHeaders;
      }
   }

   private String getCredentialScope() throws SaslException {
      return String.format("%s/%s/%s/aws4_request", this.getDate(), getRegion(this.host), "sts");
   }

   static String getSignedHeaders(Map<String, String> requestHeaders) {
      return (String)requestHeaders.keySet().stream().map(String::toLowerCase).sorted().collect(Collectors.joining(";"));
   }

   static String getCanonicalHeaders(Map<String, String> requestHeaders) {
      return (String)requestHeaders.entrySet().stream().map((kvp) -> {
         return String.format("%s:%s\n", ((String)kvp.getKey()).toLowerCase(), ((String)kvp.getValue()).trim().replaceAll(" +", " "));
      }).sorted().collect(Collectors.joining(""));
   }

   static String getRegion(String host) throws SaslException {
      String word = "(\\w)+(-\\w)*";
      if (!host.equals("sts.amazonaws.com") && !host.matches(String.format("%s", word))) {
         if (host.matches(String.format("%s(.%s)+", word, word))) {
            return host.split("\\.")[1];
         } else {
            throw new SaslException("Invalid host");
         }
      } else {
         return "us-east-1";
      }
   }

   String getSignature() {
      return this.signature;
   }

   String getTimestamp() {
      return this.timestamp;
   }

   private String getDate() {
      return this.getTimestamp().substring(0, "YYYYMMDD".length());
   }

   static String hash(String str) throws SaslException {
      return hexEncode(sha256(str)).toLowerCase();
   }

   private static String hexEncode(byte[] bytes) {
      StringBuilder sb = new StringBuilder();
      byte[] var2 = bytes;
      int var3 = bytes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte b = var2[var4];
         sb.append(String.format("%02x", b));
      }

      return sb.toString();
   }

   private static byte[] decodeUTF8(String str) {
      return str.getBytes(StandardCharsets.UTF_8);
   }

   private static byte[] hmac(byte[] secret, byte[] message) throws SaslException {
      try {
         Mac mac = Mac.getInstance("HmacSHA256");
         SecretKeySpec spec = new SecretKeySpec(secret, "HmacSHA256");
         mac.init(spec);
         byte[] hmacSha256 = mac.doFinal(message);
         return hmacSha256;
      } catch (Exception e) {
         throw new SaslException(e.getMessage());
      }
   }

   private static byte[] sha256(String payload) throws SaslException {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException e) {
         throw new SaslException(e.getMessage());
      }

      return md.digest(payload.getBytes(StandardCharsets.UTF_8));
   }

   public String toString() {
      return this.authorizationHeader;
   }

   public static AuthorizationHeader.Builder builder() {
      return new AuthorizationHeader.Builder();
   }

   // $FF: synthetic method
   AuthorizationHeader(AuthorizationHeader.Builder x0, Object x1) throws SaslException {
      this(x0);
   }

   static final class Builder {
      private String accessKeyID;
      private String secretKey;
      private String sessionToken;
      private String host;
      private String timestamp;
      private byte[] nonce;

      private Builder() {
      }

      AuthorizationHeader.Builder setAccessKeyID(String accessKeyID) {
         this.accessKeyID = accessKeyID;
         return this;
      }

      AuthorizationHeader.Builder setSecretKey(String secretKey) {
         this.secretKey = secretKey;
         return this;
      }

      AuthorizationHeader.Builder setSessionToken(@Nullable String sessionToken) {
         this.sessionToken = sessionToken;
         return this;
      }

      AuthorizationHeader.Builder setHost(String host) {
         this.host = host;
         return this;
      }

      AuthorizationHeader.Builder setTimestamp(String timestamp) {
         this.timestamp = timestamp;
         return this;
      }

      AuthorizationHeader.Builder setNonce(byte[] nonce) {
         this.nonce = nonce;
         return this;
      }

      AuthorizationHeader build() throws SaslException {
         return new AuthorizationHeader(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
