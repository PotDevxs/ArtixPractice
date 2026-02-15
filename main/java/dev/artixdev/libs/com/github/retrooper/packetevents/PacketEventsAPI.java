package dev.artixdev.libs.com.github.retrooper.packetevents;

import java.util.logging.Logger;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.EventManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.injector.ChannelInjector;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.NettyManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.settings.PacketEventsSettings;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.LogManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PEVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.updatechecker.UpdateChecker;

public abstract class PacketEventsAPI<T> {
   private static final EventManager EVENT_MANAGER = new EventManager();
   private static final PacketEventsSettings SETTINGS = new PacketEventsSettings();
   private static final UpdateChecker UPDATE_CHECKER = new UpdateChecker();
   private final Logger LOGGER = Logger.getLogger(PacketEventsAPI.class.getName());
   private static final LogManager LOG_MANAGER = new LogManager();
   private static final PEVersion VERSION = new PEVersion(new int[]{2, 2, 0});

   public EventManager getEventManager() {
      return EVENT_MANAGER;
   }

   public PacketEventsSettings getSettings() {
      return SETTINGS;
   }

   public UpdateChecker getUpdateChecker() {
      return UPDATE_CHECKER;
   }

   public PEVersion getVersion() {
      return VERSION;
   }

   public Logger getLogger() {
      return this.LOGGER;
   }

   public LogManager getLogManager() {
      return LOG_MANAGER;
   }

   public abstract void load();

   public abstract boolean isLoaded();

   public abstract void init();

   public abstract boolean isInitialized();

   public abstract void terminate();

   public abstract T getPlugin();

   public abstract ServerManager getServerManager();

   public abstract ProtocolManager getProtocolManager();

   public abstract PlayerManager getPlayerManager();

   public abstract NettyManager getNettyManager();

   public abstract ChannelInjector getInjector();
}
