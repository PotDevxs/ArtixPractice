package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
   private Class<?> viaClass;
   private Class<?> bukkitDecodeHandlerClass;
   private Class<?> bukkitEncodeHandlerClass;
   private Field viaManagerField;
   private Method apiAccessor;
   private Method getPlayerVersionMethod;
   private Class<?> userConnectionClass;

   private void load() {
      if (this.viaClass == null) {
         try {
            this.viaClass = Class.forName("us.myles.ViaVersion.api.Via");
            this.viaManagerField = this.viaClass.getDeclaredField("manager");
            this.bukkitDecodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler");
            this.bukkitEncodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler");
            Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
            this.apiAccessor = this.viaClass.getMethod("getAPI");
            this.getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
         } catch (NoSuchMethodException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
         }
      }

      if (this.userConnectionClass == null) {
         try {
            this.userConnectionClass = Class.forName("us.myles.ViaVersion.api.data.UserConnection");
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
      }

   }

   public int getProtocolVersion(Player player) {
      this.load();

      try {
         Object viaAPI = this.apiAccessor.invoke((Object)null);
         return (Integer)this.getPlayerVersionMethod.invoke(viaAPI, player);
      } catch (InvocationTargetException | IllegalAccessException e) {
         e.printStackTrace();
         return -1;
      }
   }

   public Class<?> getUserConnectionClass() {
      this.load();
      return this.userConnectionClass;
   }

   public Class<?> getBukkitDecodeHandlerClass() {
      this.load();
      return this.bukkitDecodeHandlerClass;
   }

   public Class<?> getBukkitEncodeHandlerClass() {
      this.load();
      return this.bukkitEncodeHandlerClass;
   }
}
