package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

enum ViaState {
   UNKNOWN,
   DISABLED,
   ENABLED;

   // $FF: synthetic method
   private static ViaState[] $values() {
      return new ViaState[]{UNKNOWN, DISABLED, ENABLED};
   }
}
