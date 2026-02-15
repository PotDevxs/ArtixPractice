package dev.artixdev.api.practice.spigot.knockback.impl.atom;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class AtomSpigotKnockback implements IKnockbackType {
   private static Class<?> knockbackClass;
   private static boolean atomSpigotAvailable = false;

   static {
      try {
         knockbackClass = Class.forName("xyz.yooniks.atomspigot.knockback.Knockback");
         atomSpigotAvailable = true;
      } catch (ClassNotFoundException e) {
         atomSpigotAvailable = false;
      }
   }

   public void setKnockback(Player player, String knockback) {
      if (!atomSpigotAvailable) {
         return;
      }
      
      try {
         // Get knockback by name
         Method getKnockback = knockbackClass.getMethod("getKnockback", String.class);
         Object knockbackImplement = getKnockback.invoke(null, knockback);
         
         // Set knockback for player
         Method setKnockback = player.getClass().getMethod("setKnockback", knockbackClass);
         setKnockback.invoke(player, knockbackImplement);
      } catch (Exception e) {
         // Reflection failed, AtomSpigot API may have changed or not available
      }
   }
}
