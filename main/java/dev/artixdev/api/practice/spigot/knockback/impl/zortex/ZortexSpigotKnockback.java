package dev.artixdev.api.practice.spigot.knockback.impl.zortex;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class ZortexSpigotKnockback implements IKnockbackType {
   private static Class<?> zortexSpigotClass;
   private static Class<?> zKnockbackProfileClass;
   private static boolean zortexAvailable = false;

   static {
      try {
         zortexSpigotClass = Class.forName("club.zortex.spigot.ZortexSpigot");
         zKnockbackProfileClass = Class.forName("club.zortex.spigot.knockback.zKnockbackProfile");
         zortexAvailable = true;
      } catch (ClassNotFoundException e) {
         zortexAvailable = false;
      }
   }

   public void setKnockback(Player player, String knockback) {
      if (!zortexAvailable) {
         return;
      }
      
      try {
         // Get profile by name
         Method getKbProfileByName = zortexSpigotClass.getMethod("getKbProfileByName", String.class);
         Object profile = getKbProfileByName.invoke(null, knockback);
         
         // If profile is null, use global profile
         if (profile == null) {
            Object globalKbProfile = zortexSpigotClass.getField("globalKbProfile").get(null);
            profile = globalKbProfile;
         }

         // Get CraftPlayer handle
         Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
         
         // Set knockback profile
         Method setKbProfile = craftPlayer.getClass().getMethod("setKbProfile", zKnockbackProfileClass);
         setKbProfile.invoke(craftPlayer, profile);
      } catch (Exception e) {
         // Reflection failed, ZortexSpigot API may have changed or not available
      }
   }
}
