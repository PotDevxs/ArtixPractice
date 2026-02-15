package dev.artixdev.api.practice.spigot.knockback.impl.ispigot;

import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class iSpigotKnockback implements IKnockbackType {
   private static Class<?> knockbackServiceClass;
   private static Class<?> knockbackClass;
   private static boolean iSpigotAvailable = false;

   static {
      try {
         knockbackServiceClass = Class.forName("dev.imanity.knockback.api.KnockbackService");
         knockbackClass = Class.forName("dev.imanity.knockback.api.Knockback");
         iSpigotAvailable = true;
      } catch (ClassNotFoundException e) {
         iSpigotAvailable = false;
      }
   }

   public void setKnockback(Player player, String knockback) {
      if (!iSpigotAvailable) {
         return;
      }
      
      try {
         // Get server and call imanity() method
         Object server = Bukkit.getServer();
         Method imanityMethod = server.getClass().getMethod("imanity");
         Object imanity = imanityMethod.invoke(server);
         
         // Get knockback service
         Method getKnockbackService = imanity.getClass().getMethod("getKnockbackService");
         Object knockbackHandler = getKnockbackService.invoke(imanity);
         
         // Get knockback profile by name
         Method getKnockbackByName = knockbackServiceClass.getMethod("getKnockbackByName", String.class);
         Object knockbackProfile = getKnockbackByName.invoke(knockbackHandler, knockback);
         
         // Set knockback for player
         Method setKnockback = knockbackServiceClass.getMethod("setKnockback", Player.class, knockbackClass);
         setKnockback.invoke(knockbackHandler, player, knockbackProfile);
      } catch (Exception e) {
         // Reflection failed, iSpigot API may have changed or not available
      }
   }
}
