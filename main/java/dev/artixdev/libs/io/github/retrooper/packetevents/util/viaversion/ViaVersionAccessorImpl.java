package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {
   public int getProtocolVersion(Player player) {
      return Via.getAPI().getPlayerVersion(player);
   }

   public Class<?> getUserConnectionClass() {
      return UserConnection.class;
   }

   public Class<?> getBukkitDecodeHandlerClass() {
      return BukkitDecodeHandler.class;
   }

   public Class<?> getBukkitEncodeHandlerClass() {
      return BukkitEncodeHandler.class;
   }
}
