package dev.artixdev.api.practice.nametag.util;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.packet.ScoreboardPacket;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class PacketUtil {
   public static void sendPacket(Player target, ScoreboardPacket packetWrapper) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      protocolManager.sendServerPacket(target, packetWrapper.getContainer());
   }

   public static void sendPacket(Player target, PacketWrapper<?> packetWrapper) {
      PlayerManager protocolManager = PacketEvents.getAPI().getPlayerManager();
      protocolManager.sendPacket(target, (PacketWrapper)packetWrapper);
   }

   public static void broadcast(ScoreboardPacket packetWrapper) {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player target = (Player)var2.next();
         protocolManager.sendServerPacket(target, packetWrapper.getContainer());
      }

   }

   public static void broadcast(PacketWrapper<?> packetWrapper) {
      PlayerManager playerManager = PacketEvents.getAPI().getPlayerManager();
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player target = (Player)var2.next();
         playerManager.sendPacket(target, (PacketWrapper)packetWrapper);
      }

   }

   private PacketUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
