package dev.artixdev.libs.com.github.retrooper.packetevents.exception;

public class PacketProcessException extends RuntimeException {
   public PacketProcessException(String msg, Throwable cause) {
      super(msg, cause);
   }

   public PacketProcessException(Throwable cause) {
      super(cause);
   }

   public PacketProcessException(String msg) {
      super(msg);
   }
}
