package dev.artixdev.api.practice.tablist.util;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class PacketUtils {
   public static boolean isLegacyClient(Player player) {
      ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
      return version.isOlderThan(ClientVersion.V_1_8);
   }

   public static boolean isModernClient(Player player) {
      ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
      return version.isNewerThanOrEquals(ClientVersion.V_1_16);
   }

   public static boolean isBrokenClient(Player player) {
      ClientVersion version = PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
      return version.isNewerThanOrEquals(ClientVersion.V_1_18);
   }

   public static void sendPacket(Player target, PacketWrapper<?> packetWrapper) {
      PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
      PlayerManager manager = packetEvents.getPlayerManager();
      manager.sendPacket(target, (PacketWrapper)packetWrapper);
   }

   private PacketUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
