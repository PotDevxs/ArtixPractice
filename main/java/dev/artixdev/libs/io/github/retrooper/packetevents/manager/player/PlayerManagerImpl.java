package dev.artixdev.libs.io.github.retrooper.packetevents.manager.player;

import java.net.SocketAddress;
import java.util.UUID;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.PlayerPingAccessorModern;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class PlayerManagerImpl implements PlayerManager {
   public int getPing(@NotNull Object player) {
      return SpigotReflectionUtil.V_1_17_OR_HIGHER ? PlayerPingAccessorModern.getPing((Player)player) : SpigotReflectionUtil.getPlayerPingLegacy((Player)player);
   }

   @NotNull
   public ClientVersion getClientVersion(@NotNull Object p) {
      Player player = (Player)p;
      User user = this.getUser(player);
      if (user == null) {
         return ClientVersion.UNKNOWN;
      } else {
         if (user.getClientVersion() == null) {
            int protocolVersion;
            if (ProtocolSupportUtil.isAvailable()) {
               protocolVersion = ProtocolSupportUtil.getProtocolVersion((SocketAddress)user.getAddress());
               PacketEvents.getAPI().getLogManager().debug("Requested ProtocolSupport for user " + user.getName() + "'s protocol version. Protocol version: " + protocolVersion);
            } else if (ViaVersionUtil.isAvailable()) {
               protocolVersion = ViaVersionUtil.getProtocolVersion(player);
               PacketEvents.getAPI().getLogManager().debug("Requested ViaVersion for " + player.getName() + "'s protocol version. Protocol version: " + protocolVersion);
            } else {
               protocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
               PacketEvents.getAPI().getLogManager().debug("No protocol translation plugins are available. We will assume " + user.getName() + "'s protocol version is the same as the server's protocol version. Protocol version: " + protocolVersion);
            }

            ClientVersion version = ClientVersion.getById(protocolVersion);
            user.setClientVersion(version);
         }

         return user.getClientVersion();
      }
   }

   public Object getChannel(@NotNull Object player) {
      UUID uuid = ((Player)player).getUniqueId();
      Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(uuid);
      if (channel == null) {
         channel = SpigotReflectionUtil.getChannel((Player)player);
         if (channel != null) {
            synchronized(channel) {
               if (ChannelHelper.isOpen(channel)) {
                  ProtocolManager.CHANNELS.put(uuid, channel);
               }
            }
         }
      }

      return channel;
   }

   public User getUser(@NotNull Object player) {
      Player p = (Player)player;
      Object channel = this.getChannel(p);
      return channel == null ? null : PacketEvents.getAPI().getProtocolManager().getUser(channel);
   }
}
