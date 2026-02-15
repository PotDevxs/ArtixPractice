package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public enum PacketListenerPriority {
   LOWEST,
   LOW,
   NORMAL,
   HIGH,
   HIGHEST,
   MONITOR;

   public static PacketListenerPriority getById(byte id) {
      return values()[id];
   }

   public byte getId() {
      return (byte)this.ordinal();
   }

   // $FF: synthetic method
   private static PacketListenerPriority[] $values() {
      return new PacketListenerPriority[]{LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR};
   }
}
