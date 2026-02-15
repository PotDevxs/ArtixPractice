package dev.artixdev.api.practice.tablist;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import dev.artixdev.api.practice.tablist.adapter.TabAdapter;
import dev.artixdev.api.practice.tablist.adapter.impl.ExampleAdapter;
import dev.artixdev.api.practice.tablist.listener.SkinCacheListener;
import dev.artixdev.api.practice.tablist.listener.TabListener;
import dev.artixdev.api.practice.tablist.listener.TeamsPacketListener;
import dev.artixdev.api.practice.tablist.setup.TabLayout;
import dev.artixdev.api.practice.tablist.skin.SkinCache;
import dev.artixdev.api.practice.tablist.thread.TablistThread;
import dev.artixdev.api.practice.tablist.util.PacketUtils;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;

public class TablistHandler {
   private static final Logger log = LogManager.getLogger(TablistHandler.class);
   private static TablistHandler instance;
   private final Map<UUID, TabLayout> layoutMapping = new ConcurrentHashMap();
   private final JavaPlugin plugin;
   private SkinCache skinCache;
   private TabAdapter adapter;
   private TabListener listener;
   private TablistThread thread;
   private PacketEventsAPI<?> packetEvents;
   private final boolean debug;

   public TablistHandler(JavaPlugin plugin) {
      instance = this;
      this.plugin = plugin;
      this.debug = Boolean.getBoolean("BDebug");
   }

   public void init(PacketEventsAPI<?> packetEventsAPI, TeamsPacketListener listener) {
      this.packetEvents = packetEventsAPI;
      this.adapter = new ExampleAdapter();
      this.listener = new TabListener(this);
      this.packetEvents.getEventManager().registerListener(listener);
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      this.setupSkinCache();
   }

   public void setupSkinCache() {
      this.skinCache = new SkinCache();
      Bukkit.getPluginManager().registerEvents(new SkinCacheListener(this), this.plugin);
   }

   public void registerAdapter(TabAdapter tabAdapter, long ticks) {
      this.adapter = (TabAdapter)(tabAdapter == null ? new ExampleAdapter() : tabAdapter);
      if (ticks < 20L) {
         log.info("[{}] Provided refresh tick rate for Tablist is too low, reverting to 20 ticks!", this.plugin.getName());
         ticks = 20L;
      }

      if (Bukkit.getMaxPlayers() < 60) {
         log.fatal("[{}] Max Players is below 60, this will cause issues for players on 1.7 and below!", this.plugin.getName());
      }

      this.thread = new TablistThread(this);
      this.thread.runTaskTimerAsynchronously(this.plugin, 0L, ticks);
   }

   public void unload() {
      if (this.listener != null) {
         HandlerList.unregisterAll(this.listener);
         this.listener = null;
      }

      Iterator var1 = this.layoutMapping.entrySet().iterator();

      while(true) {
         UUID uuid;
         Player player;
         do {
            do {
               if (!var1.hasNext()) {
                  this.thread.cancel();
                  this.thread = null;
                  return;
               }

               Entry<UUID, TabLayout> entry = (Entry)var1.next();
               uuid = (UUID)entry.getKey();
               player = Bukkit.getPlayer(uuid);
            } while(player == null);
         } while(!player.isOnline());

         if (PacketUtils.isLegacyClient(player)) {
            for(int i = 0; i < 60; ++i) {
               Team team = player.getScoreboard().getTeam("$" + TabLayout.TAB_NAMES[i]);
               if (team != null) {
                  team.unregister();
               }
            }
         }

         Team team = player.getScoreboard().getTeam("tab");
         if (team != null) {
            team.unregister();
         }

         this.layoutMapping.remove(uuid);
      }
   }

   public Map<UUID, TabLayout> getLayoutMapping() {
      return this.layoutMapping;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public SkinCache getSkinCache() {
      return this.skinCache;
   }

   public TabAdapter getAdapter() {
      return this.adapter;
   }

   public TabListener getListener() {
      return this.listener;
   }

   public TablistThread getThread() {
      return this.thread;
   }

   public PacketEventsAPI<?> getPacketEvents() {
      return this.packetEvents;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public static TablistHandler getInstance() {
      return instance;
   }
}
