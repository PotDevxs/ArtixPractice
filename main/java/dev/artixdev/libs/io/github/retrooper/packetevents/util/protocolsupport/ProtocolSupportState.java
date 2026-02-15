package dev.artixdev.libs.io.github.retrooper.packetevents.util.protocolsupport;

enum ProtocolSupportState {
   UNKNOWN,
   DISABLED,
   ENABLED;

   // $FF: synthetic method
   private static ProtocolSupportState[] $values() {
      return new ProtocolSupportState[]{UNKNOWN, DISABLED, ENABLED};
   }
}
