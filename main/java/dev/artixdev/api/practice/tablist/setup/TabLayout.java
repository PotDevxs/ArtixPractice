package dev.artixdev.api.practice.tablist.setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.api.practice.tablist.adapter.TabAdapter;
import dev.artixdev.api.practice.tablist.util.PacketUtils;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.api.practice.tablist.util.StringUtils;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerListHeaderAndFooter;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class TabLayout {
   private static final Logger log = LogManager.getLogger(TabLayout.class);
   public static String[] TAB_NAMES = new String[80];
   private final Map<Integer, TabEntryInfo> entryMapping = new Int2ObjectArrayMap();
   private final int mod;
   private final int maxEntries;
   private final Player player;
   private boolean isFirstJoin = true;

   public TabLayout(Player player) {
      this.mod = PacketUtils.isLegacyClient(player) ? 3 : 4;
      this.maxEntries = PacketUtils.isLegacyClient(player) ? 60 : 80;
      this.player = player;
   }

   public void create() {
      PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
      ServerManager manager = packetEvents.getServerManager();
      int index;
      int x;
      int y;
      int i;
      if (manager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> dataList = new ArrayList();

         for(index = 0; index < this.maxEntries; ++index) {
            x = index % this.mod;
            y = index / this.mod;
            i = y * this.mod + x;
            UserProfile gameProfile = this.generateProfile(i);
            TabEntryInfo info = new TabEntryInfo(gameProfile);
            this.entryMapping.put(i, info);
            dataList.add(new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, true, 0, GameMode.SURVIVAL, !PacketUtils.isLegacyClient(this.player) ? AdventureSerializer.fromLegacyFormat(this.getTeamAt(i)) : null, (RemoteChatSession)null));
         }

         WrapperPlayServerPlayerInfoUpdate packetInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, dataList);
         WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, dataList);
         WrapperPlayServerPlayerInfoUpdate gamemode = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE, dataList);
         this.sendPacket(packetInfo);
         if (!PacketUtils.isLegacyClient(this.player)) {
            WrapperPlayServerPlayerInfoUpdate display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, dataList);
            this.sendPacket(display);
         }

         this.sendPacket(list);
         this.sendPacket(gamemode);
      } else {
         WrapperPlayServerPlayerInfo packetInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData[0]);
         List<WrapperPlayServerPlayerInfo.PlayerData> dataList = packetInfo.getPlayerDataList();

         for(x = 0; x < this.maxEntries; ++x) {
            y = x % this.mod;
            i = x / this.mod;
            i = i * this.mod + y;
            UserProfile gameProfile = this.generateProfile(i);
            TabEntryInfo info = new TabEntryInfo(gameProfile);
            this.entryMapping.put(i, info);
            dataList.add(new WrapperPlayServerPlayerInfo.PlayerData(!PacketUtils.isLegacyClient(this.player) ? AdventureSerializer.fromLegacyFormat(this.getTeamAt(i)) : null, gameProfile, GameMode.SURVIVAL, 0));
         }

         this.sendPacket(packetInfo);
      }

      Team bukkitTeam = this.player.getScoreboard().getTeam("tab");
      if (bukkitTeam == null) {
         bukkitTeam = this.player.getScoreboard().registerNewTeam("tab");
      }

      Bukkit.getOnlinePlayers().stream().filter(Objects::nonNull).map(Player::getName).forEach(bukkitTeam::addEntry);

      for(index = 0; index < this.maxEntries; ++index) {
         String displayName = this.getTeamAt(index);
         String team = "$" + displayName;
         Team scoreboardTeam = this.player.getScoreboard().getTeam(team);
         if (scoreboardTeam == null) {
            scoreboardTeam = this.player.getScoreboard().registerNewTeam(team);
            scoreboardTeam.addEntry(displayName);
         }
      }

      Iterator var16 = Bukkit.getOnlinePlayers().iterator();

      while(var16.hasNext()) {
         Player target = (Player)var16.next();
         Team team = target.getScoreboard().getTeam("tab");
         if (team != null) {
            team.addEntry(this.player.getName());
         }
      }

   }

   public void setHeaderAndFooter() {
      if (!PacketUtils.isLegacyClient(this.player)) {
         TabAdapter tablistAdapter = TablistHandler.getInstance().getAdapter();
         if (tablistAdapter != null) {
            String header = StringUtils.color(tablistAdapter.getHeader(this.player));
            String footer = StringUtils.color(tablistAdapter.getFooter(this.player));
            WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = new WrapperPlayServerPlayerListHeaderAndFooter(AdventureSerializer.fromLegacyFormat(header), AdventureSerializer.fromLegacyFormat(footer));
            this.sendPacket(headerAndFooter);
         }
      }
   }

   public void update(int index, String text, int ping, Skin skin) {
      if (!PacketUtils.isLegacyClient(this.player) || index < 60) {
         text = StringUtils.color(text);
         String[] splitString = StringUtils.split(text);
         String prefix = splitString[0];
         String suffix = splitString[1];
         String displayName = this.getTeamAt(index);
         String team = "$" + displayName;
         if ((team.length() > 16 || displayName.length() > 16) && TablistHandler.getInstance().isDebug()) {
            log.info("[TablistAPI] Team Name or Display Name is longer than 16");
         }

         if ((prefix.length() > 16 || suffix.length() > 16) && TablistHandler.getInstance().isDebug()) {
            log.info("[TablistAPI] Prefix or Suffix is longer than 16");
         }

         TabEntryInfo entry = (TabEntryInfo)this.entryMapping.get(index);
         if (entry != null) {
            boolean changed = false;
            if (!prefix.equals(entry.getPrefix())) {
               entry.setPrefix(prefix);
               changed = true;
            }

            if (!suffix.equals(entry.getSuffix())) {
               entry.setSuffix(suffix);
               changed = true;
            }

            if (PacketUtils.isLegacyClient(this.player)) {
               Scoreboard scoreboard = this.player.getScoreboard();
               Team bukkitTeam = scoreboard.getTeam(team);
               boolean teamExists = bukkitTeam != null;
               if (prefix.length() > 16) {
                  prefix = prefix.substring(0, 16);
               }

               if (suffix.length() > 16) {
                  suffix = suffix.substring(0, 16);
               }

               if (bukkitTeam == null) {
                  bukkitTeam = scoreboard.registerNewTeam(team);
                  bukkitTeam.addEntry(displayName);
               }

               if (changed || !teamExists) {
                  bukkitTeam.setPrefix(prefix);
                  bukkitTeam.setSuffix(suffix);
               }

               this.updatePing(entry, ping);
            } else {
               boolean updated = this.updateSkin(entry, skin, text);
               this.updatePing(entry, ping);
               if (!updated && (changed || this.isFirstJoin)) {
                  this.updateDisplayName(entry, text.length() == 0 ? this.getTeamAt(index) : text);
                  if (this.isFirstJoin) {
                     this.isFirstJoin = false;
                  }
               }
            }

         }
      }
   }

   private void updatePing(TabEntryInfo info, int ping) {
      PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
      ServerManager serverManager = packetEvents.getServerManager();
      boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);
      int lastConnection = info.getPing();
      if (lastConnection != ping) {
         info.setPing(ping);
         UserProfile gameProfile = info.getProfile();
         Object playerInfo;
         if (isClientNew) {
            playerInfo = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, true, ping, GameMode.SURVIVAL, (Component)null, (RemoteChatSession)null)});
         } else {
            playerInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_LATENCY, new WrapperPlayServerPlayerInfo.PlayerData[]{new WrapperPlayServerPlayerInfo.PlayerData((Component)null, gameProfile, GameMode.SURVIVAL, ping)});
         }

         this.sendPacket((PacketWrapper)playerInfo);
      }
   }

   private boolean updateSkin(TabEntryInfo info, Skin skin, String text) {
      PacketEventsAPI<?> packetEvents = TablistHandler.getInstance().getPacketEvents();
      ServerManager serverManager = packetEvents.getServerManager();
      boolean isServerNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);
      if (skin == null) {
         skin = Skin.DEFAULT_SKIN;
      }

      Skin lastSkin = info.getSkin();
      if (skin.equals(lastSkin)) {
         return false;
      } else {
         info.setSkin(skin);
         UserProfile userProfile = info.getProfile();
         TextureProperty textureProperty = new TextureProperty("textures", skin.getValue(), skin.getSignature());
         userProfile.setTextureProperties(Collections.singletonList(textureProperty));
         int ping = info.getPing();
         PacketWrapper<?> playerInfoRemove = null;
         if (isServerNew) {
            playerInfoRemove = new WrapperPlayServerPlayerInfoRemove(new UUID[]{userProfile.getUUID()});
         } else {
            playerInfoRemove = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData[]{new WrapperPlayServerPlayerInfo.PlayerData((Component)null, userProfile, GameMode.SURVIVAL, ping)});
         }

         if (isServerNew) {
            WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(userProfile, true, ping, GameMode.SURVIVAL, !PacketUtils.isLegacyClient(this.player) ? AdventureSerializer.fromLegacyFormat(text) : null, (RemoteChatSession)null);
            WrapperPlayServerPlayerInfoUpdate add = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{data});
            WrapperPlayServerPlayerInfoUpdate list = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{data});
            if (PacketUtils.isModernClient(this.player)) {
               this.sendPacket((PacketWrapper)playerInfoRemove);
            }

            this.sendPacket(add);
            this.sendPacket(list);
            if (!PacketUtils.isLegacyClient(this.player)) {
               WrapperPlayServerPlayerInfoUpdate display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{data});
               this.sendPacket(display);
            }
         } else {
            WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(!PacketUtils.isLegacyClient(this.player) ? AdventureSerializer.fromLegacyFormat(text) : null, userProfile, GameMode.SURVIVAL, ping);
            PacketWrapper<?> playerInfoAdd = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData[]{data});
            if (PacketUtils.isModernClient(this.player)) {
               this.sendPacket((PacketWrapper)playerInfoRemove);
            }

            this.sendPacket(playerInfoAdd);
         }

         return true;
      }
   }

   private void sendPacket(PacketWrapper<?> packetWrapper) {
      if (this.player != null) {
         PacketUtils.sendPacket(this.player, packetWrapper);
      }
   }

   private UserProfile generateProfile(int index) {
      Skin defaultSkin = Skin.DEFAULT_SKIN;
      UserProfile gameProfile = new UserProfile(UUID.randomUUID(), this.getTeamAt(index));
      TextureProperty textureProperty = new TextureProperty("textures", defaultSkin.getValue(), defaultSkin.getSignature());
      gameProfile.setTextureProperties(Collections.singletonList(textureProperty));
      return gameProfile;
   }

   private void updateDisplayName(TabEntryInfo entry, String text) {
      PacketEventsAPI<?> packetEvents = PacketEvents.getAPI();
      ServerManager manager = packetEvents.getServerManager();
      UserProfile profile = entry.getProfile();
      Object display;
      if (manager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         WrapperPlayServerPlayerInfoUpdate.PlayerInfo data = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(profile, true, 0, (GameMode)null, AdventureSerializer.fromLegacyFormat(text), (RemoteChatSession)null);
         display = new WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME, new WrapperPlayServerPlayerInfoUpdate.PlayerInfo[]{data});
      } else {
         WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(AdventureSerializer.fromLegacyFormat(text), profile, (GameMode)null, 0);
         display = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_DISPLAY_NAME, new WrapperPlayServerPlayerInfo.PlayerData[]{data});
      }

      this.sendPacket((PacketWrapper)display);
   }

   public String getTeamAt(int index) {
      return TAB_NAMES[index];
   }

   public int getMod() {
      return this.mod;
   }

   public int getMaxEntries() {
      return this.maxEntries;
   }

   public Player getPlayer() {
      return this.player;
   }

   static {
      for(int i = 0; i < TAB_NAMES.length; ++i) {
         int x = i % 4;
         int y = i / 4;
         String name = "§0§" + x + (y > 9 ? "§" + String.valueOf(y).toCharArray()[0] + "§" + String.valueOf(y).toCharArray()[1] : "§0§" + String.valueOf(y).toCharArray()[0]) + ChatColor.RESET;
         TAB_NAMES[i] = name;
      }

   }
}
