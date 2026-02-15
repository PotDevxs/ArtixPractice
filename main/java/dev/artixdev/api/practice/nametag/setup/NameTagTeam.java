package dev.artixdev.api.practice.nametag.setup;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.packet.ScoreboardPacket;
import dev.artixdev.api.practice.nametag.util.ColorUtil;
import dev.artixdev.api.practice.nametag.util.PacketUtil;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;

public class NameTagTeam {
   private final String name;
   private final String prefix;
   private final String suffix;
   private final Object createPacket;

   public NameTagTeam(String name, String prefix, String suffix, boolean collide) {
      this.name = name;
      this.prefix = prefix;
      this.suffix = suffix;
      if (VersionUtil.MINOR_VERSION > 12) {
         WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(ColorUtil.translate(name), ColorUtil.translate(prefix), ColorUtil.translate(suffix), WrapperPlayServerTeams.NameTagVisibility.ALWAYS, collide ? WrapperPlayServerTeams.CollisionRule.ALWAYS : WrapperPlayServerTeams.CollisionRule.NEVER, ColorUtil.getLastColor(prefix), WrapperPlayServerTeams.OptionData.NONE);
         this.createPacket = new WrapperPlayServerTeams(name, WrapperPlayServerTeams.TeamMode.CREATE, info, new String[0]);
      } else {
         this.createPacket = new ScoreboardPacket(name, ColorUtil.color(prefix), ColorUtil.color(suffix));
      }

   }

   public PacketWrapper<?> getPECreatePacket() {
      return (PacketWrapper)this.createPacket;
   }

   public ScoreboardPacket getNormalCreatePacket() {
      return (ScoreboardPacket)this.createPacket;
   }

   public void destroyFor(Player player) {
      WrapperPlayServerTeams packet = new WrapperPlayServerTeams(this.name, WrapperPlayServerTeams.TeamMode.REMOVE, new WrapperPlayServerTeams.ScoreBoardTeamInfo(ColorUtil.translate(this.name), (Component)null, (Component)null, (WrapperPlayServerTeams.NameTagVisibility)null, (WrapperPlayServerTeams.CollisionRule)null, (NamedTextColor)null, (WrapperPlayServerTeams.OptionData)null), new String[0]);
      PacketUtil.sendPacket(player, (PacketWrapper)packet);
   }

   public String getName() {
      return this.name;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public Object getCreatePacket() {
      return this.createPacket;
   }
}
