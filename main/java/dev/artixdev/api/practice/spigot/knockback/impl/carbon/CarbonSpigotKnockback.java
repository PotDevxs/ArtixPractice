package dev.artixdev.api.practice.spigot.knockback.impl.carbon;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class CarbonSpigotKnockback implements IKnockbackType {
   private static Class<?> knockbackAPIClass;
   private static Class<?> knockbackProfileClass;
   private static boolean carbonSpigotAvailable = false;

   static {
      try {
         knockbackAPIClass = Class.forName("dev.artixdev.spigot.api.knockback.KnockbackAPI");
         knockbackProfileClass = Class.forName("dev.artixdev.spigot.knockback.KnockbackProfile");
         carbonSpigotAvailable = true;
      } catch (ClassNotFoundException e) {
         carbonSpigotAvailable = false;
      }
   }

   public void setKnockback(Player player, String knockback) {
      if (!carbonSpigotAvailable) {
         return;
      }
      
      try {
         // Get KnockbackAPI instance
         Method getInstance = knockbackAPIClass.getMethod("getInstance");
         Object api = getInstance.invoke(null);
         
         // Get profile by name
         Method getProfile = knockbackAPIClass.getMethod("getProfile", String.class);
         Object profile = getProfile.invoke(api, knockback);
         
         // If profile is null, use default profile
         if (profile == null) {
            Method getDefaultProfile = knockbackAPIClass.getMethod("getDefaultProfile");
            profile = getDefaultProfile.invoke(api);
         }

         // Set player profile
         Method setPlayerProfile = knockbackAPIClass.getMethod("setPlayerProfile", Player.class, knockbackProfileClass);
         setPlayerProfile.invoke(api, player, profile);
      } catch (Exception e) {
         // Reflection failed, CarbonSpigot API may have changed or not available
      }
   }
}
