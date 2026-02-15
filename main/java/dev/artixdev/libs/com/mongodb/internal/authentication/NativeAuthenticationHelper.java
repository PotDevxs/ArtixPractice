package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import dev.artixdev.libs.com.mongodb.internal.HexUtils;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class NativeAuthenticationHelper {
   public static String createAuthenticationHash(String userName, char[] password) {
      ByteArrayOutputStream bout = new ByteArrayOutputStream(userName.length() + 20 + password.length);

      try {
         bout.write(userName.getBytes(StandardCharsets.UTF_8));
         bout.write(":mongo:".getBytes(StandardCharsets.UTF_8));
         bout.write((new String(password)).getBytes(StandardCharsets.UTF_8));
      } catch (IOException e) {
         throw new RuntimeException("impossible", e);
      }

      return HexUtils.hexMD5(bout.toByteArray());
   }

   public static BsonDocument getAuthCommand(String userName, char[] password, String nonce) {
      return getAuthCommand(userName, createAuthenticationHash(userName, password), nonce);
   }

   public static BsonDocument getAuthCommand(String userName, String authHash, String nonce) {
      String key = nonce + userName + authHash;
      BsonDocument cmd = new BsonDocument();
      cmd.put((String)"authenticate", (BsonValue)(new BsonInt32(1)));
      cmd.put((String)"user", (BsonValue)(new BsonString(userName)));
      cmd.put((String)"nonce", (BsonValue)(new BsonString(nonce)));
      cmd.put((String)"key", (BsonValue)(new BsonString(HexUtils.hexMD5(key.getBytes(StandardCharsets.UTF_8)))));
      return cmd;
   }

   public static BsonDocument getNonceCommand() {
      return new BsonDocument("getnonce", new BsonInt32(1));
   }

   private NativeAuthenticationHelper() {
   }
}
