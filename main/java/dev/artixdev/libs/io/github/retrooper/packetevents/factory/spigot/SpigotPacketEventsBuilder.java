package dev.artixdev.libs.io.github.retrooper.packetevents.factory.spigot;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.injector.ChannelInjector;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.NettyManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.settings.PacketEventsSettings;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.LogManager;
import dev.artixdev.libs.io.github.retrooper.packetevents.bstats.Metrics;
import dev.artixdev.libs.io.github.retrooper.packetevents.bukkit.InternalBukkitListener;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import dev.artixdev.libs.io.github.retrooper.packetevents.manager.InternalBukkitPacketListener;
import dev.artixdev.libs.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import dev.artixdev.libs.io.github.retrooper.packetevents.manager.protocol.ProtocolManagerImpl;
import dev.artixdev.libs.io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import dev.artixdev.libs.io.github.retrooper.packetevents.netty.NettyManagerImpl;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.BukkitLogManager;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.FoliaCompatUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;

public class SpigotPacketEventsBuilder {
   private static PacketEventsAPI<Plugin> API_INSTANCE;

   public static void clearBuildCache() {
      API_INSTANCE = null;
   }

   public static PacketEventsAPI<Plugin> build(Plugin plugin) {
      if (API_INSTANCE == null) {
         API_INSTANCE = buildNoCache(plugin);
      }

      return API_INSTANCE;
   }

   public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
      if (API_INSTANCE == null) {
         API_INSTANCE = buildNoCache(plugin, settings);
      }

      return API_INSTANCE;
   }

   public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
      return buildNoCache(plugin, new PacketEventsSettings());
   }

   public static PacketEventsAPI<Plugin> buildNoCache(final Plugin plugin, final PacketEventsSettings inSettings) {
      return new PacketEventsAPI<Plugin>() {
         private final PacketEventsSettings settings = inSettings;
         private final ProtocolManager protocolManager = new ProtocolManagerImpl();
         private final ServerManager serverManager = new ServerManagerImpl();
         private final PlayerManager playerManager = new PlayerManagerImpl();
         private final NettyManager nettyManager = new NettyManagerImpl();
         private final SpigotChannelInjector injector = new SpigotChannelInjector();
         private final LogManager logManager = new BukkitLogManager();
         private boolean loaded;
         private boolean initialized;
         private boolean lateBind = false;

         public void load() {
            if (!this.loaded) {
               String id = plugin.getName().toLowerCase();
               PacketEvents.IDENTIFIER = "pe-" + id;
               PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
               PacketEvents.DECODER_NAME = "pe-decoder-" + id;
               PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
               PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
               PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;

               try {
                  SpigotReflectionUtil.init();
                  CustomPipelineUtil.init();
               } catch (Exception e) {
                  throw new IllegalStateException(e);
               }

               if (!PacketType.isPrepared()) {
                  PacketType.prepare();
               }

               this.lateBind = !this.injector.isServerBound();
               if (!this.lateBind) {
                  this.injector.inject();
               }

               this.loaded = true;
               this.getEventManager().registerListener(new InternalBukkitPacketListener());
            }

         }

         public boolean isLoaded() {
            return this.loaded;
         }

         public void init() {
            this.load();
            if (!this.initialized) {
               if (this.settings.shouldCheckForUpdates()) {
                  this.getUpdateChecker().handleUpdateCheck();
               }

               if (this.settings.isbStatsEnabled()) {
                  Metrics metrics = new Metrics((JavaPlugin)plugin, 11327);
                  metrics.addCustomChart(new Metrics.SimplePie("packetevents_version", () -> {
                     return this.getVersion().toString();
                  }));
               }

               Bukkit.getPluginManager().registerEvents(new InternalBukkitListener(plugin), plugin);
               if (this.lateBind) {
                  Runnable lateBindTask = () -> {
                     if (this.injector.isServerBound()) {
                        this.injector.inject();
                     }

                  };
                  FoliaCompatUtil.runTaskOnInit(plugin, lateBindTask);
               }

               if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
                  this.checkCompatibility();
               }

               this.initialized = true;
            }

         }

         private void checkCompatibility() {
            ViaVersionUtil.checkIfViaIsPresent();
            ProtocolSupportUtil.checkIfProtocolSupportIsPresent();
            Plugin viaPlugin = Bukkit.getPluginManager().getPlugin("ViaVersion");
            int majorVersion;
            if (viaPlugin != null) {
               String[] ver = viaPlugin.getDescription().getVersion().split("\\.", 3);
               majorVersion = Integer.parseInt(ver[0]);
               int minor = Integer.parseInt(ver[1]);
               if (majorVersion < 4 || majorVersion == 4 && minor < 5) {
                  PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ViaVersion older than 4.5.0, please update your ViaVersion!");
                  Plugin ourPluginx = this.getPlugin();
                  Bukkit.getPluginManager().disablePlugin(ourPluginx);
                  throw new IllegalStateException("ViaVersion incompatibility! Update to v4.5.0 or newer!");
               }
            }

            Plugin protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
            if (protocolLibPlugin != null) {
               majorVersion = Integer.parseInt(protocolLibPlugin.getDescription().getVersion().split("\\.", 2)[0]);
               if (majorVersion < 5) {
                  PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a ProtocolLib version older than v5.0.0. This is no longer works, please update to their dev builds. https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                  Plugin ourPlugin = this.getPlugin();
                  Bukkit.getPluginManager().disablePlugin(ourPlugin);
                  throw new IllegalStateException("ProtocolLib incompatibility! Update to v5.0.0 or newer!");
               }
            }

         }

         public boolean isInitialized() {
            return this.initialized;
         }

         public void terminate() {
            if (this.initialized) {
               this.injector.uninject();
               Iterator var1 = ProtocolManager.USERS.values().iterator();

               while(var1.hasNext()) {
                  User user = (User)var1.next();
                  ServerConnectionInitializer.destroyHandlers(user.getChannel());
               }

               this.getEventManager().unregisterAllListeners();
               this.initialized = false;
            }

         }

         public Plugin getPlugin() {
            return plugin;
         }

         public ProtocolManager getProtocolManager() {
            return this.protocolManager;
         }

         public ServerManager getServerManager() {
            return this.serverManager;
         }

         public PlayerManager getPlayerManager() {
            return this.playerManager;
         }

         public PacketEventsSettings getSettings() {
            return this.settings;
         }

         public NettyManager getNettyManager() {
            return this.nettyManager;
         }

         public ChannelInjector getInjector() {
            return this.injector;
         }

         public LogManager getLogManager() {
            return this.logManager;
         }
      };
   }
}
