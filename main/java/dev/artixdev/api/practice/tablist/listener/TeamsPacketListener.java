package dev.artixdev.api.practice.tablist.listener;

import java.util.EnumSet;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.api.practice.tablist.util.GlitchFixEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;

public class TeamsPacketListener extends PacketListenerAbstract {
   private final PacketEventsAPI<?> packetEvents;

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO || event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
         ServerManager serverManager = this.packetEvents.getServerManager();
         boolean isClientNew = serverManager.getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3);
         Player player = (Player)event.getPlayer();
         Iterator var7;
         UserProfile userProfile;
         if (isClientNew && event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            WrapperPlayServerPlayerInfoUpdate infoUpdate = new WrapperPlayServerPlayerInfoUpdate(event);
            EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> action = infoUpdate.getActions();
            if (!action.contains(WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER)) {
               return;
            }

            var7 = infoUpdate.getEntries().iterator();

            while(var7.hasNext()) {
               WrapperPlayServerPlayerInfoUpdate.PlayerInfo info = (WrapperPlayServerPlayerInfoUpdate.PlayerInfo)var7.next();
               userProfile = info.getGameProfile();
               if (userProfile != null) {
                  this.preventGlitch(player, userProfile);
               }
            }
         } else if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(event);
            WrapperPlayServerPlayerInfo.Action action = infoPacket.getAction();
            if (action != WrapperPlayServerPlayerInfo.Action.ADD_PLAYER) {
               return;
            }

            var7 = infoPacket.getPlayerDataList().iterator();

            while(var7.hasNext()) {
               WrapperPlayServerPlayerInfo.PlayerData data = (WrapperPlayServerPlayerInfo.PlayerData)var7.next();
               userProfile = data.getUserProfile();
               if (userProfile != null) {
                  this.preventGlitch(player, userProfile);
               }
            }
         }

      }
   }

   private void preventGlitch(Player player, UserProfile userProfile) {
      Player online = Bukkit.getPlayer(userProfile.getUUID());
      if (online != null) {
         Scoreboard scoreboard = player.getScoreboard();
         Team team = scoreboard.getTeam("tab");
         if (team == null) {
            team = scoreboard.registerNewTeam("tab");
            Iterator var6 = Bukkit.getOnlinePlayers().iterator();

            while(var6.hasNext()) {
               Player otherPlayer = (Player)var6.next();
               team.addEntry(otherPlayer.getName());
            }
         }

         team.addEntry(online.getName());
         GlitchFixEvent glitchFixEvent = new GlitchFixEvent(player);
         Bukkit.getScheduler().runTask(TablistHandler.getInstance().getPlugin(), () -> {
            Bukkit.getPluginManager().callEvent(glitchFixEvent);
         });
      }
   }

   public TeamsPacketListener(PacketEventsAPI<?> packetEvents) {
      this.packetEvents = packetEvents;
   }
}
