package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.Reflection;

public class ViaVersionUtil {
   private static ViaState available;
   private static ViaVersionAccessor viaVersionAccessor;

   private ViaVersionUtil() {
   }

   private static void load() {
      if (viaVersionAccessor == null) {
         try {
            Class.forName("com.viaversion.viaversion.api.Via");
            viaVersionAccessor = new ViaVersionAccessorImpl();
         } catch (Exception e) {
            try {
               Class.forName("us.myles.ViaVersion.api.Via");
               viaVersionAccessor = new ViaVersionAccessorImplLegacy();
            } catch (ClassNotFoundException ignored) {
               viaVersionAccessor = null;
            }
         }
      }

   }

   public static void checkIfViaIsPresent() {
      boolean present = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
      available = present ? ViaState.ENABLED : ViaState.DISABLED;
   }

   public static boolean isAvailable() {
      if (available == ViaState.UNKNOWN) {
         return getViaVersionAccessor() != null;
      } else {
         return available == ViaState.ENABLED;
      }
   }

   public static ViaVersionAccessor getViaVersionAccessor() {
      load();
      return viaVersionAccessor;
   }

   public static int getProtocolVersion(User user) {
      try {
         if (user.getUUID() != null) {
            Player player = Bukkit.getPlayer(user.getUUID());
            if (player != null) {
               int version = getProtocolVersion(player);
               if (version != -1) {
                  return version;
               }
            }
         }

         Object viaEncoder = ((Channel)user.getChannel()).pipeline().get("via-encoder");
         Object connection = Reflection.getField(viaEncoder.getClass(), "connection").get(viaEncoder);
         Object protocolInfo = Reflection.getField(connection.getClass(), "protocolInfo").get(connection);
         return (Integer)Reflection.getField(protocolInfo.getClass(), "protocolVersion").get(protocolInfo);
      } catch (Exception e) {
         System.out.println("Unable to grab ViaVersion client version for player!");
         return -1;
      }
   }

   public static int getProtocolVersion(Player player) {
      return getViaVersionAccessor().getProtocolVersion(player);
   }

   public static Class<?> getUserConnectionClass() {
      return getViaVersionAccessor().getUserConnectionClass();
   }

   public static Class<?> getBukkitDecodeHandlerClass() {
      return getViaVersionAccessor().getBukkitDecodeHandlerClass();
   }

   public static Class<?> getBukkitEncodeHandlerClass() {
      return getViaVersionAccessor().getBukkitEncodeHandlerClass();
   }

   static {
      available = ViaState.UNKNOWN;
   }
}
