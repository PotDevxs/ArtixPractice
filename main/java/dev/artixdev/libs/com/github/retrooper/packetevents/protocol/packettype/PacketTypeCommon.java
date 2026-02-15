package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.PacketSide;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface PacketTypeCommon {
   default String getName() {
      return ((Enum)this).name();
   }

   int getId(ClientVersion var1);

   PacketSide getSide();
}
