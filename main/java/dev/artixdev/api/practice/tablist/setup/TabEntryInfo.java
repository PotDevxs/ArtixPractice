package dev.artixdev.api.practice.tablist.setup;

import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;

public class TabEntryInfo {
   private final UserProfile profile;
   private int ping = 0;
   private Skin skin;
   private String prefix;
   private String suffix;
   private WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo;

   public boolean equals(Object o) {
      if (!(o instanceof TabEntryInfo)) {
         return false;
      } else {
         return o != this ? false : ((TabEntryInfo)o).getProfile().equals(this.profile);
      }
   }

   public int hashCode() {
      return this.profile.getUUID().hashCode() + 645;
   }

   public UserProfile getProfile() {
      return this.profile;
   }

   public int getPing() {
      return this.ping;
   }

   public Skin getSkin() {
      return this.skin;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public WrapperPlayServerTeams.ScoreBoardTeamInfo getTeamInfo() {
      return this.teamInfo;
   }

   public void setPing(int ping) {
      this.ping = ping;
   }

   public void setSkin(Skin skin) {
      this.skin = skin;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }

   public void setTeamInfo(WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo) {
      this.teamInfo = teamInfo;
   }

   public TabEntryInfo(UserProfile profile) {
      this.skin = Skin.DEFAULT_SKIN;
      this.prefix = "";
      this.suffix = "";
      this.teamInfo = null;
      this.profile = profile;
   }
}
