package dev.artixdev.libs.com.cryptomorin.xseries;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ReflectionUtils {
   public static final String NMS_VERSION;
   public static final int MINOR_NUMBER;
   public static final int PATCH_NUMBER;
   public static final String CRAFTBUKKIT_PACKAGE;
   public static final String NMS_PACKAGE;
   private static final MethodHandle PLAYER_CONNECTION;
   private static final MethodHandle GET_HANDLE;
   private static final MethodHandle SEND_PACKET;

   public static String getVersionInformation() {
      return "(NMS: " + NMS_VERSION + " | Minecraft: " + Bukkit.getVersion() + " | Bukkit: " + Bukkit.getBukkitVersion() + ')';
   }

   public static Integer getLatestPatchNumberOf(int minorVersion) {
      if (minorVersion <= 0) {
         throw new IllegalArgumentException("Minor version must be positive: " + minorVersion);
      } else {
         int[] patches = new int[]{1, 5, 2, 7, 2, 4, 10, 8, 4, 2, 2, 2, 2, 4, 2, 5, 1, 2, 4, 2};
         return minorVersion > patches.length ? null : patches[minorVersion - 1];
      }
   }

   private ReflectionUtils() {
   }

   public static <T> ReflectionUtils.VersionHandler<T> v(int version, T handle) {
      return new ReflectionUtils.VersionHandler(version, handle);
   }

   public static <T> ReflectionUtils.VersionHandler<T> v(int version, int patch, T handle) {
      return new ReflectionUtils.VersionHandler(version, patch, handle);
   }

   public static <T> ReflectionUtils.CallableVersionHandler<T> v(int version, Callable<T> handle) {
      return new ReflectionUtils.CallableVersionHandler(version, handle);
   }

   public static boolean supports(int minorNumber) {
      return MINOR_NUMBER >= minorNumber;
   }

   public static boolean supports(int minorNumber, int patchNumber) {
      return MINOR_NUMBER == minorNumber ? supportsPatch(patchNumber) : supports(minorNumber);
   }

   public static boolean supportsPatch(int patchNumber) {
      return PATCH_NUMBER >= patchNumber;
   }

   @Nullable
   public static Class<?> getNMSClass(@Nullable String packageName, @Nonnull String name) {
      if (packageName != null && supports(17)) {
         name = packageName + '.' + name;
      }

      try {
         return Class.forName(NMS_PACKAGE + name);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Nullable
   public static Class<?> getNMSClass(@Nonnull String name) {
      return getNMSClass((String)null, name);
   }

   @Nonnull
   public static CompletableFuture<Void> sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
      return CompletableFuture.runAsync(() -> {
         sendPacketSync(player, packets);
      }).exceptionally((ex) -> {
         ex.printStackTrace();
         return null;
      });
   }

   public static void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
      try {
         Object handle = GET_HANDLE.invoke(player);
         Object connection = PLAYER_CONNECTION.invoke(handle);
         if (connection != null) {
            for (Object packet : packets) {
               SEND_PACKET.invoke(connection, packet);
            }
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

   }

   @Nullable
   public static Object getHandle(@Nonnull Player player) {
      Objects.requireNonNull(player, "Cannot get handle of null player");

      try {
         return GET_HANDLE.invoke(player);
      } catch (Throwable t) {
         t.printStackTrace();
         return null;
      }
   }

   @Nullable
   public static Object getConnection(@Nonnull Player player) {
      Objects.requireNonNull(player, "Cannot get connection of null player");

      try {
         Object handle = GET_HANDLE.invoke(player);
         return PLAYER_CONNECTION.invoke(handle);
      } catch (Throwable t) {
         t.printStackTrace();
         return null;
      }
   }

   @Nullable
   public static Class<?> getCraftClass(@Nonnull String name) {
      try {
         return Class.forName(CRAFTBUKKIT_PACKAGE + name);
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   /** @deprecated */
   @Deprecated
   public static Class<?> getArrayClass(String clazz, boolean nms) {
      clazz = "[L" + (nms ? NMS_PACKAGE : CRAFTBUKKIT_PACKAGE) + clazz + ';';

      try {
         return Class.forName(clazz);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static Class<?> toArrayClass(Class<?> clazz) {
      try {
         return Class.forName("[L" + clazz.getName() + ';');
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
         return null;
      }
   }

   static {
      String found = null;
      Package[] packages = Package.getPackages();
      int packageCount = packages.length;

      for (int i = 0; i < packageCount; ++i) {
         Package pack = packages[i];
         String name = pack.getName();
         if (name.startsWith("org.bukkit.craftbukkit.v")) {
            found = pack.getName().split("\\.")[3];

            try {
               Class.forName("org.bukkit.craftbukkit." + found + ".entity.CraftPlayer");
               break;
            } catch (ClassNotFoundException ignored) {
               found = null;
            }
         }
      }

      if (found == null) {
         throw new IllegalArgumentException("Failed to parse server version. Could not find any package starting with name: 'org.bukkit.craftbukkit.v'");
      } else {
         NMS_VERSION = found;
         String[] split = NMS_VERSION.substring(1).split("_");
         if (split.length < 1) {
            throw new IllegalStateException("Version number division error: " + Arrays.toString(split) + ' ' + getVersionInformation());
         } else {
            String minorVer = split[1];

            try {
               MINOR_NUMBER = Integer.parseInt(minorVer);
               if (MINOR_NUMBER < 0) {
                  throw new IllegalStateException("Negative minor number? " + minorVer + ' ' + getVersionInformation());
               }
            } catch (Throwable t) {
               throw new RuntimeException("Failed to parse minor number: " + minorVer + ' ' + getVersionInformation(), t);
            }

            Matcher bukkitVer = Pattern.compile("^\\d+\\.\\d+\\.(\\d+)").matcher(Bukkit.getBukkitVersion());
            if (bukkitVer.find()) {
               try {
                  PATCH_NUMBER = Integer.parseInt(bukkitVer.group(1));
               } catch (Throwable t) {
                  throw new RuntimeException("Failed to parse minor number: " + bukkitVer + ' ' + getVersionInformation(), t);
               }
            } else {
               PATCH_NUMBER = 0;
            }

            CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit." + NMS_VERSION + '.';
            NMS_PACKAGE = (String)v(17, (Object)"net.minecraft.").orElse("net.minecraft.server." + NMS_VERSION + '.');
            Class<?> entityPlayer = getNMSClass("server.level", "EntityPlayer");
            Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
            Class<?> playerConnection = getNMSClass("server.network", "PlayerConnection");
            Class playerCommonConnection;
            if (supports(20) && supportsPatch(2)) {
               playerCommonConnection = getNMSClass("server.network", "ServerCommonPacketListenerImpl");
            } else {
               playerCommonConnection = playerConnection;
            }

            Lookup lookup = MethodHandles.lookup();
            MethodHandle sendPacket = null;
            MethodHandle getHandle = null;
            MethodHandle connection = null;

            try {
               connection = lookup.findGetter(entityPlayer, (String)v(20, (Object)"c").v(17, "b").orElse("playerConnection"), playerConnection);
               getHandle = lookup.findVirtual(craftPlayer, "getHandle", MethodType.methodType(entityPlayer));
               sendPacket = lookup.findVirtual(playerCommonConnection, (String)v(20, 2, "b").v(18, "a").orElse("sendPacket"), MethodType.methodType(Void.TYPE, getNMSClass("network.protocol", "Packet")));
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
               e.printStackTrace();
            }

            PLAYER_CONNECTION = connection;
            SEND_PACKET = sendPacket;
            GET_HANDLE = getHandle;
         }
      }
   }

   public static final class VersionHandler<T> {
      private int version;
      private int patch;
      private T handle;

      private VersionHandler(int version, T handle) {
         this(version, 0, handle);
      }

      private VersionHandler(int version, int patch, T handle) {
         if (ReflectionUtils.supports(version) && ReflectionUtils.supportsPatch(patch)) {
            this.version = version;
            this.patch = patch;
            this.handle = handle;
         }

      }

      public ReflectionUtils.VersionHandler<T> v(int version, T handle) {
         return this.v(version, 0, handle);
      }

      public ReflectionUtils.VersionHandler<T> v(int version, int patch, T handle) {
         if (version == this.version && patch == this.patch) {
            throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version + '.' + patch);
         } else {
            if (version > this.version && ReflectionUtils.supports(version) && patch >= this.patch && ReflectionUtils.supportsPatch(patch)) {
               this.version = version;
               this.patch = patch;
               this.handle = handle;
            }

            return this;
         }
      }

      public T orElse(T handle) {
         return this.version == 0 ? handle : this.handle;
      }

      // $FF: synthetic method
      VersionHandler(int x0, Object x1, Object x2) {
         this(x0, (T) x1);
      }

      // $FF: synthetic method
      VersionHandler(int x0, int x1, Object x2, Object x3) {
         this(x0, x1, (T) x2);
      }
   }

   public static final class CallableVersionHandler<T> {
      private int version;
      private Callable<T> handle;

      private CallableVersionHandler(int version, Callable<T> handle) {
         if (ReflectionUtils.supports(version)) {
            this.version = version;
            this.handle = handle;
         }

      }

      public ReflectionUtils.CallableVersionHandler<T> v(int version, Callable<T> handle) {
         if (version == this.version) {
            throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version);
         } else {
            if (version > this.version && ReflectionUtils.supports(version)) {
               this.version = version;
               this.handle = handle;
            }

            return this;
         }
      }

      public T orElse(Callable<T> handle) {
         try {
            return (this.version == 0 ? handle : this.handle).call();
         } catch (Exception e) {
            e.printStackTrace();
            return null;
         }
      }

      // $FF: synthetic method
      CallableVersionHandler(int x0, Callable x1, Object x2) {
         this(x0, x1);
      }
   }
}
