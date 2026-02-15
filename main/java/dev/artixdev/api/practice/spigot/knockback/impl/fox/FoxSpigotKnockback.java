package dev.artixdev.api.practice.spigot.knockback.impl.fox;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class FoxSpigotKnockback implements IKnockbackType {
   private static Class<?> knockbackModuleClass;
   private static Class<?> knockbackProfileClass;
   private static boolean foxSpigotAvailable = false;

   static {
      try {
         knockbackModuleClass = Class.forName("pt.foxspigot.jar.knockback.KnockbackModule");
         knockbackProfileClass = Class.forName("pt.foxspigot.jar.knockback.KnockbackProfile");
         foxSpigotAvailable = true;
      } catch (ClassNotFoundException e) {
         foxSpigotAvailable = false;
      }
   }

   public void setKnockback(Player player, String knockback) {
      if (!foxSpigotAvailable) {
         return;
      }
      
      try {
         // Get knockback profile by name
         Method getByName = knockbackModuleClass.getMethod("getByName", String.class);
         Object profile = getByName.invoke(null, knockback);
         
         // Get CraftPlayer handle
         Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
         
         // Set knockback profile
         Method setKnockback = craftPlayer.getClass().getMethod("setKnockback", knockbackProfileClass);
         setKnockback.invoke(craftPlayer, profile);
      } catch (Exception e) {
         // Reflection failed, FoxSpigot API may have changed or not available
      }
   }
}
