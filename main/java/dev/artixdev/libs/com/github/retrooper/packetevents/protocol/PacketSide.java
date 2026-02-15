package dev.artixdev.libs.com.github.retrooper.packetevents.protocol;

public enum PacketSide {
   CLIENT,
   SERVER;

   public PacketSide getOpposite() {
      return this == CLIENT ? SERVER : CLIENT;
   }

   // $FF: synthetic method
   private static PacketSide[] $values() {
      return new PacketSide[]{CLIENT, SERVER};
   }
}
