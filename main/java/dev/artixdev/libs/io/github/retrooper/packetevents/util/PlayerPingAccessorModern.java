package dev.artixdev.libs.io.github.retrooper.packetevents.util;

import org.bukkit.entity.Player;

public class PlayerPingAccessorModern {
   public static int getPing(Player player) {
      try {
         Object result = Player.class.getMethod("getPing").invoke(player);
         return result != null ? ((Number) result).intValue() : 0;
      } catch (ReflectiveOperationException e) {
         return 0;
      }
   }
}
