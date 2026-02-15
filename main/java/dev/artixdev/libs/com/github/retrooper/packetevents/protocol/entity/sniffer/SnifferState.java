package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.sniffer;

public enum SnifferState {
   IDLING,
   FEELING_HAPPY,
   SCENTING,
   SNIFFING,
   SEARCHING,
   DIGGING,
   RISING;

   // $FF: synthetic method
   private static SnifferState[] $values() {
      return new SnifferState[]{IDLING, FEELING_HAPPY, SCENTING, SNIFFING, SEARCHING, DIGGING, RISING};
   }
}
