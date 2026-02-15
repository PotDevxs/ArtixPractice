package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import java.security.PublicKey;
import java.time.Instant;

public class PublicProfileKey {
   private final Instant expiresAt;
   private final PublicKey key;
   private final byte[] keySignature;

   public PublicProfileKey(Instant expiresAt, PublicKey key, byte[] keySignature) {
      this.expiresAt = expiresAt;
      this.key = key;
      this.keySignature = keySignature;
   }

   public Instant getExpiresAt() {
      return this.expiresAt;
   }

   public PublicKey getKey() {
      return this.key;
   }

   public byte[] getKeySignature() {
      return this.keySignature;
   }

   public boolean hasExpired() {
      return this.expiresAt.isBefore(Instant.now());
   }
}
