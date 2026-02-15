package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.util.Optional;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class MessageSignature {
   private byte[] bytes;

   public MessageSignature(byte[] bytes) {
      this.bytes = bytes;
   }

   public byte[] getBytes() {
      return this.bytes;
   }

   public void setBytes(byte[] bytes) {
      this.bytes = bytes;
   }

   public static class Packed {
      private int id;
      @Nullable
      private MessageSignature fullSignature;

      public Packed(@Nullable MessageSignature fullSignature) {
         this.id = -1;
         this.fullSignature = fullSignature;
      }

      public Packed(int id) {
         this.id = id;
         this.fullSignature = null;
      }

      public int getId() {
         return this.id;
      }

      public void setId(int id) {
         this.id = id;
      }

      public Optional<MessageSignature> getFullSignature() {
         return Optional.ofNullable(this.fullSignature);
      }

      public void setFullSignature(@Nullable MessageSignature fullSignature) {
         this.fullSignature = fullSignature;
      }
   }
}
