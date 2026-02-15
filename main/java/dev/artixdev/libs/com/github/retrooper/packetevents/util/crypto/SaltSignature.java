package dev.artixdev.libs.com.github.retrooper.packetevents.util.crypto;

public class SaltSignature {
   private long salt;
   private byte[] signature;

   public SaltSignature(long salt, byte[] signature) {
      this.salt = salt;
      this.signature = signature;
   }

   public long getSalt() {
      return this.salt;
   }

   public void setSalt(long salt) {
      this.salt = salt;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }
}
