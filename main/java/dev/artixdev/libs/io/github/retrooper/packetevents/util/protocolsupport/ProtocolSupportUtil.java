package dev.artixdev.libs.io.github.retrooper.packetevents.util.protocolsupport;

import java.net.SocketAddress;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProtocolSupportUtil {
   private static ProtocolSupportState available;

   public static boolean isAvailable() {
      if (available == ProtocolSupportState.UNKNOWN) {
         try {
            Class.forName("protocolsupport.api.ProtocolSupportAPI");
            available = ProtocolSupportState.ENABLED;
            return true;
         } catch (Exception ignored) {
            available = ProtocolSupportState.DISABLED;
            return false;
         }
      } else {
         return available == ProtocolSupportState.ENABLED;
      }
   }

   public static void checkIfProtocolSupportIsPresent() {
      boolean present = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
      available = present ? ProtocolSupportState.ENABLED : ProtocolSupportState.DISABLED;
   }

   public static int getProtocolVersion(SocketAddress address) {
      try {
         Class<?> apiClass = Class.forName("protocolsupport.api.ProtocolSupportAPI");
         Object protocolVersion = apiClass.getMethod("getProtocolVersion", SocketAddress.class).invoke(null, address);
         return (Integer) protocolVersion.getClass().getMethod("getId").invoke(protocolVersion);
      } catch (ReflectiveOperationException e) {
         return -1;
      }
   }

   public static int getProtocolVersion(Player player) {
      try {
         Class<?> apiClass = Class.forName("protocolsupport.api.ProtocolSupportAPI");
         Object protocolVersion = apiClass.getMethod("getProtocolVersion", Player.class).invoke(null, player);
         return (Integer) protocolVersion.getClass().getMethod("getId").invoke(protocolVersion);
      } catch (ReflectiveOperationException e) {
         return -1;
      }
   }

   static {
      available = ProtocolSupportState.UNKNOWN;
   }
}
