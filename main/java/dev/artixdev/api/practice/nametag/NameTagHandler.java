package dev.artixdev.api.practice.nametag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.nametag.adapter.DefaultNameTagAdapter;
import dev.artixdev.api.practice.nametag.adapter.NameTagAdapter;
import dev.artixdev.api.practice.nametag.listener.DisguiseListener;
import dev.artixdev.api.practice.nametag.listener.GlitchFixListener;
import dev.artixdev.api.practice.nametag.listener.NameTagListener;
import dev.artixdev.api.practice.nametag.packet.ScoreboardPacket;
import dev.artixdev.api.practice.nametag.setup.NameTagTeam;
import dev.artixdev.api.practice.nametag.setup.NameTagUpdate;
import dev.artixdev.api.practice.nametag.thread.NameTagThread;
import dev.artixdev.api.practice.nametag.util.ColorUtil;
import dev.artixdev.api.practice.nametag.util.PacketUtil;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectLists;
import dev.artixdev.practice.nametag.NameTagManager;

public class NameTagHandler {
   private static final Logger log = LogManager.getLogger(NameTagHandler.class);
   private static int TEAM_INDEX = 0;
   private static NameTagHandler instance;
   private final Map<UUID, Map<UUID, NameTagTeam>> teamMap = new ConcurrentHashMap();
   private final List<NameTagTeam> registeredTeams = ObjectLists.synchronize(new ObjectArrayList());
   private final JavaPlugin plugin;
   private NameTagAdapter adapter;
   private NameTagThread thread;
   private PacketEventsAPI<?> packetEvents;
   private boolean collisionEnabled;
   private boolean debugMode;
   private NameTagTeam cachedTeam;

   public NameTagHandler(JavaPlugin plugin) {
      instance = this;
      this.plugin = plugin;
      this.debugMode = Boolean.getBoolean("BDebug");
   }

   public void init(PacketEventsAPI<?> packetEventsAPI) {
      this.packetEvents = packetEventsAPI;
      this.adapter = new DefaultNameTagAdapter();
      this.packetEvents.getEventManager().registerListener(new DisguiseListener());
      Bukkit.getPluginManager().registerEvents(new NameTagListener(this), this.plugin);
      Bukkit.getPluginManager().registerEvents(new GlitchFixListener(this), this.plugin);
   }

   public void unload() {
      this.thread.stopExecuting();
   }

   public void registerAdapter(NameTagAdapter adapter, long ticks) {
      this.adapter = (NameTagAdapter)(adapter == null ? new DefaultNameTagAdapter() : adapter);
      if (ticks < 1L) {
         log.info("[{}] Provided refresh tick rate for NameTag is too low, reverting to 2 ticks!", this.plugin.getName());
         ticks = 2L;
      }

      this.thread = new NameTagThread(this, ticks);
      this.thread.start();
   }

   public void createTeams(Player player) {
      this.adapter.fetchNameTag(player, player);
   }

   public void initiatePlayer(Player player) {
      Iterator var2 = this.registeredTeams.iterator();

      while(var2.hasNext()) {
         NameTagTeam teamInfo = (NameTagTeam)var2.next();
         if (VersionUtil.MINOR_VERSION > 12) {
            PacketUtil.sendPacket(player, teamInfo.getPECreatePacket());
         } else {
            PacketUtil.sendPacket(player, teamInfo.getNormalCreatePacket());
         }
      }

   }

   public void unloadPlayer(Player player) {
      Iterator var2 = this.registeredTeams.iterator();

      while(var2.hasNext()) {
         NameTagTeam team = (NameTagTeam)var2.next();
         team.destroyFor(player);
      }

      this.teamMap.remove(player.getUniqueId());
   }

   public void reloadPlayer(Player toRefresh, Player refreshFor) {
      NameTagUpdate update = new NameTagUpdate(toRefresh, refreshFor);
      this.thread.getUpdatesQueue().add(update);
   }

   public void reloadPlayer(Player toRefresh) {
      NameTagUpdate update = new NameTagUpdate(toRefresh);
      this.thread.getUpdatesQueue().add(update);
   }

   public void reloadOthersFor(Player refreshFor) {
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player toRefresh = (Player)var2.next();
         if (refreshFor != toRefresh) {
            this.reloadPlayer(toRefresh, refreshFor);
         }
      }

   }

   public void applyUpdate(NameTagUpdate nameTagUpdate) {
      if (nameTagUpdate.getToRefresh() != null) {
         Player toRefreshPlayer = Bukkit.getPlayer(nameTagUpdate.getToRefresh());
         if (toRefreshPlayer != null) {
            if (nameTagUpdate.getRefreshFor() == null) {
               Bukkit.getOnlinePlayers().forEach((refreshFor) -> {
                  this.reloadPlayerInternal(toRefreshPlayer, refreshFor);
               });
            } else {
               Player refreshForPlayer = Bukkit.getPlayer(nameTagUpdate.getRefreshFor());
               if (refreshForPlayer != null) {
                  this.reloadPlayerInternal(toRefreshPlayer, refreshForPlayer);
               }
            }

         }
      }
   }

   public void reloadPlayerInternal(Player toRefresh, Player refreshFor) {
      NameTagTeam provided = this.adapter.fetchNameTag(toRefresh, refreshFor);
      if (provided != null) {
         if (VersionUtil.MINOR_VERSION > 12) {
            WrapperPlayServerTeams packet = new WrapperPlayServerTeams(provided.getName(), WrapperPlayServerTeams.TeamMode.ADD_ENTITIES, (WrapperPlayServerTeams.ScoreBoardTeamInfo)null, new String[]{toRefresh.getName()});
            PacketUtil.sendPacket(refreshFor, (PacketWrapper)packet);
         } else {
            ScoreboardPacket packet = new ScoreboardPacket(provided.getName(), Collections.singletonList(toRefresh.getName()));
            PacketUtil.sendPacket(refreshFor, packet);
         }

         if (VersionUtil.canHex()) {
            ServerManager manager = this.packetEvents.getServerManager();
            UserProfile profile = new UserProfile(toRefresh.getUniqueId(), toRefresh.getName());
            String text = ColorUtil.color(provided.getPrefix() + toRefresh.getName() + provided.getSuffix());
            Object display;
            if (manager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
               WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(profile, true, 0, (GameMode)null, AdventureSerializer.fromLegacyFormat(text), (RemoteChatSession)null);
               display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{data});
            } else {
               WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(AdventureSerializer.fromLegacyFormat(text), profile, (GameMode)null, 0);
               display = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_DISPLAY_NAME, new WrapperPlayServerPlayerInfo.PlayerData[]{data});
            }

            PacketUtil.sendPacket(refreshFor, (PacketWrapper)display);
         }

         Map<UUID, NameTagTeam> teamInfoMap = new HashMap();
         if (this.teamMap.containsKey(refreshFor.getUniqueId())) {
            teamInfoMap = (Map)this.teamMap.get(refreshFor.getUniqueId());
         }

         ((Map)teamInfoMap).put(toRefresh.getUniqueId(), provided);
         this.teamMap.put(refreshFor.getUniqueId(), teamInfoMap);
      }
   }

   public NameTagTeam getOrCreate(String prefix, String suffix) {
      if (this.debugMode) {
         log.info("[NameTagAPI-Debug] Trying to fetch a team with prefix {} and suffix {}", ColorUtil.getRaw(prefix), ColorUtil.getRaw(suffix));
      }

      if (this.cachedTeam != null && this.cachedTeam.getPrefix().equals(prefix) && this.cachedTeam.getSuffix().equals(suffix)) {
         return this.cachedTeam;
      } else {
         Iterator var3 = this.registeredTeams.iterator();

         NameTagTeam teamInfo;
         do {
            if (!var3.hasNext()) {
               ++TEAM_INDEX;
               NameTagTeam newTeam = new NameTagTeam("artixNT" + TEAM_INDEX, prefix, suffix, this.collisionEnabled);
               this.cachedTeam = newTeam;
               if (this.debugMode) {
                  log.info("[NameTagAPI-Debug] Creating Team with Name: {} with Prefix {} and Suffix {}", newTeam.getName(), ColorUtil.getRaw(newTeam.getPrefix()), ColorUtil.getRaw(newTeam.getSuffix()));
               }

               this.registeredTeams.add(newTeam);
               if (VersionUtil.MINOR_VERSION > 12) {
                  PacketUtil.broadcast(newTeam.getPECreatePacket());
               } else {
                  PacketUtil.broadcast(newTeam.getNormalCreatePacket());
               }

               return newTeam;
            }

            teamInfo = (NameTagTeam)var3.next();
         } while(!teamInfo.getPrefix().equals(prefix) || !teamInfo.getSuffix().equals(suffix));

         return teamInfo;
      }
   }

   public Map<UUID, Map<UUID, NameTagTeam>> getTeamMap() {
      return this.teamMap;
   }

   public List<NameTagTeam> getRegisteredTeams() {
      return this.registeredTeams;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public NameTagAdapter getAdapter() {
      return this.adapter;
   }

   public NameTagThread getThread() {
      return this.thread;
   }

   public PacketEventsAPI<?> getPacketEvents() {
      return this.packetEvents;
   }

   public boolean isCollisionEnabled() {
      return this.collisionEnabled;
   }

   public boolean isDebugMode() {
      return this.debugMode;
   }

   public NameTagTeam getCachedTeam() {
      return this.cachedTeam;
   }

   public void setAdapter(NameTagAdapter adapter) {
      this.adapter = adapter;
   }

   public void setThread(NameTagThread thread) {
      this.thread = thread;
   }

   public void setPacketEvents(PacketEventsAPI<?> packetEvents) {
      this.packetEvents = packetEvents;
   }

   public void setCollisionEnabled(boolean collisionEnabled) {
      this.collisionEnabled = collisionEnabled;
   }

   public void setDebugMode(boolean debugMode) {
      this.debugMode = debugMode;
   }

   public void setCachedTeam(NameTagTeam cachedTeam) {
      this.cachedTeam = cachedTeam;
   }

   public static NameTagHandler getInstance() {
      return instance;
   }

   public void setNameTagManager(NameTagManager nameTagManager) {
      // Store reference to name tag manager
      // This would typically be used for integration with the practice plugin
   }

   public void updateNameTag(Player player, Player other, String nameTag) {
      // Update name tag for specific players
      // This would typically refresh the name tag between two players
      if (player != null && other != null && nameTag != null) {
         // Implementation would go here
      }
   }
}
