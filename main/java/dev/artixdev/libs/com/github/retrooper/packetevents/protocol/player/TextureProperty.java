package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class TextureProperty {
   private final String name;
   private final String value;
   private final String signature;

   public TextureProperty(String name, String value, @Nullable String signature) {
      this.name = name;
      this.value = value;
      this.signature = signature;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   @Nullable
   public String getSignature() {
      return this.signature;
   }

   public boolean isSignatureValid(PublicKey publicKey) {
      if (this.getSignature() == null) {
         return false;
      } else {
         try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(this.value.getBytes());
            return signature.verify(Base64.getDecoder().decode(this.signature));
         } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
         }
      }
   }
}
