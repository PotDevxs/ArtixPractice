package dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto;

import java.security.PublicKey;
import java.time.Instant;

public class SignatureData {
   private final Instant timestamp;
   private final PublicKey publicKey;
   private final byte[] signature;

   public SignatureData(Instant timestamp, PublicKey publicKey, byte[] signature) {
      this.timestamp = timestamp;
      this.publicKey = publicKey;
      this.signature = signature;
   }

   public SignatureData(Instant timestamp, byte[] encodedPublicKey, byte[] signature) {
      this.timestamp = timestamp;
      this.publicKey = MinecraftEncryptionUtil.publicKey(encodedPublicKey);
      this.signature = signature;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public PublicKey getPublicKey() {
      return this.publicKey;
   }

   public byte[] getSignature() {
      return this.signature;
   }
}
