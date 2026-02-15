package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.ColorUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.LegacyFormat;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class WrapperPlayServerTeams extends PacketWrapper<WrapperPlayServerTeams> {
   private String teamName;
   private WrapperPlayServerTeams.TeamMode teamMode;
   private Collection<String> players;
   private Optional<WrapperPlayServerTeams.ScoreBoardTeamInfo> teamInfo;

   public WrapperPlayServerTeams(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTeams(String teamName, WrapperPlayServerTeams.TeamMode teamMode, @Nullable WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo, String... entities) {
      this(teamName, teamMode, (WrapperPlayServerTeams.ScoreBoardTeamInfo)teamInfo, (Collection)Arrays.asList(entities));
   }

   public WrapperPlayServerTeams(String teamName, WrapperPlayServerTeams.TeamMode teamMode, @Nullable WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo, Collection<String> entities) {
      super((PacketTypeCommon)PacketType.Play.Server.TEAMS);
      this.teamName = teamName;
      this.teamMode = teamMode;
      this.players = entities;
      this.teamInfo = Optional.ofNullable(teamInfo);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerTeams(String teamName, WrapperPlayServerTeams.TeamMode teamMode, Optional<WrapperPlayServerTeams.ScoreBoardTeamInfo> teamInfo, String... entities) {
      this(teamName, teamMode, (Optional)teamInfo, (Collection)Arrays.asList(entities));
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerTeams(String teamName, WrapperPlayServerTeams.TeamMode teamMode, Optional<WrapperPlayServerTeams.ScoreBoardTeamInfo> teamInfo, Collection<String> entities) {
      super((PacketTypeCommon)PacketType.Play.Server.TEAMS);
      this.teamName = teamName;
      this.teamMode = teamMode;
      this.players = entities;
      this.teamInfo = teamInfo;
   }

   public void read() {
      int teamNameLimit = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? 32767 : 16;
      this.teamName = this.readString(teamNameLimit);
      this.teamMode = WrapperPlayServerTeams.TeamMode.values()[this.readByte()];
      WrapperPlayServerTeams.ScoreBoardTeamInfo info = null;
      if (this.teamMode == WrapperPlayServerTeams.TeamMode.CREATE || this.teamMode == WrapperPlayServerTeams.TeamMode.UPDATE) {
         WrapperPlayServerTeams.CollisionRule collisionRule = null;
         Component displayName;
         Component prefix;
         Component suffix;
         WrapperPlayServerTeams.OptionData optionData;
         WrapperPlayServerTeams.NameTagVisibility nameTagVisibility;
         NamedTextColor color;
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
            displayName = AdventureSerializer.fromLegacyFormat(this.readString(32));
            prefix = AdventureSerializer.fromLegacyFormat(this.readString(16));
            suffix = AdventureSerializer.fromLegacyFormat(this.readString(16));
            optionData = WrapperPlayServerTeams.OptionData.values()[this.readByte()];
            if (this.serverVersion == ServerVersion.V_1_7_10) {
               nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.ALWAYS;
               color = NamedTextColor.WHITE;
            } else {
               nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.fromID(this.readString(32));
               if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                  collisionRule = WrapperPlayServerTeams.CollisionRule.fromID(this.readString(32));
               }

               if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                  int colorId = this.readVarInt();
                  if (colorId == 21) {
                     colorId = -1;
                  }

                  color = ColorUtil.fromId(colorId);
               } else {
                  color = ColorUtil.fromId(this.readByte());
               }
            }
         } else {
            displayName = this.readComponent();
            optionData = WrapperPlayServerTeams.OptionData.fromValue(this.readByte());
            nameTagVisibility = WrapperPlayServerTeams.NameTagVisibility.fromID(this.readString(40));
            collisionRule = WrapperPlayServerTeams.CollisionRule.fromID(this.readString(40));
            color = ColorUtil.fromId(this.readByte());
            prefix = this.readComponent();
            suffix = this.readComponent();
         }

         info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(displayName, prefix, suffix, nameTagVisibility, collisionRule == null ? WrapperPlayServerTeams.CollisionRule.ALWAYS : collisionRule, color, optionData);
      }

      this.teamInfo = Optional.ofNullable(info);
      this.players = new ArrayList();
      if (this.teamMode == WrapperPlayServerTeams.TeamMode.CREATE || this.teamMode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || this.teamMode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
         int size;
         if (this.serverVersion == ServerVersion.V_1_7_10) {
            size = this.readShort();
         } else {
            size = this.readVarInt();
         }

         for(int i = 0; i < size; ++i) {
            this.players.add(this.readString(40));
         }
      }

   }

   public void write() {
      int teamNameLimit = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? 32767 : 16;
      this.writeString(this.teamName, teamNameLimit);
      this.writeByte(this.teamMode.ordinal());
      if (this.teamMode == WrapperPlayServerTeams.TeamMode.CREATE || this.teamMode == WrapperPlayServerTeams.TeamMode.UPDATE) {
         WrapperPlayServerTeams.ScoreBoardTeamInfo info = (WrapperPlayServerTeams.ScoreBoardTeamInfo)this.teamInfo.orElse(new WrapperPlayServerTeams.ScoreBoardTeamInfo(Component.empty(), Component.empty(), Component.empty(), WrapperPlayServerTeams.NameTagVisibility.ALWAYS, WrapperPlayServerTeams.CollisionRule.ALWAYS, NamedTextColor.WHITE, WrapperPlayServerTeams.OptionData.NONE));
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
            this.writeString(LegacyFormat.trimLegacyFormat(AdventureSerializer.asVanilla(info.displayName), 32));
            this.writeString(LegacyFormat.trimLegacyFormat(AdventureSerializer.asVanilla(info.prefix), 16));
            this.writeString(LegacyFormat.trimLegacyFormat(AdventureSerializer.asVanilla(info.suffix), 16));
            this.writeByte(info.optionData.ordinal());
            if (this.serverVersion == ServerVersion.V_1_7_10) {
               this.writeString(WrapperPlayServerTeams.NameTagVisibility.ALWAYS.getId(), 32);
               this.writeByte(15);
            } else {
               this.writeString(info.tagVisibility.id, 32);
               if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                  this.writeString(info.collisionRule.getId(), 32);
               }

               this.writeByte(ColorUtil.getId(info.color));
            }
         } else {
            this.writeComponent(info.displayName);
            this.writeByte(info.optionData.getByteValue());
            this.writeString(info.tagVisibility.id);
            this.writeString(info.collisionRule.getId());
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
               int colorId = ColorUtil.getId(info.color);
               if (colorId < 0) {
                  colorId = 21;
               }

               this.writeVarInt(colorId);
            } else {
               this.writeByte(ColorUtil.getId(info.color));
            }

            this.writeComponent(info.prefix);
            this.writeComponent(info.suffix);
         }
      }

      if (this.teamMode == WrapperPlayServerTeams.TeamMode.CREATE || this.teamMode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || this.teamMode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
         if (this.serverVersion == ServerVersion.V_1_7_10) {
            this.writeShort(this.players.size());
         } else {
            this.writeVarInt(this.players.size());
         }

         for (String playerName : this.players) {
            this.writeString(playerName, 40);
         }
      }
   }

   public void copy(WrapperPlayServerTeams wrapper) {
      this.teamName = wrapper.teamName;
      this.teamMode = wrapper.teamMode;
      this.players = wrapper.players;
      this.teamInfo = wrapper.teamInfo;
   }

   public String getTeamName() {
      return this.teamName;
   }

   public void setTeamName(String teamName) {
      this.teamName = teamName;
   }

   public WrapperPlayServerTeams.TeamMode getTeamMode() {
      return this.teamMode;
   }

   public void setTeamMode(WrapperPlayServerTeams.TeamMode teamMode) {
      this.teamMode = teamMode;
   }

   public Collection<String> getPlayers() {
      return this.players;
   }

   public void setPlayers(Collection<String> players) {
      this.players = players;
   }

   public Optional<WrapperPlayServerTeams.ScoreBoardTeamInfo> getTeamInfo() {
      return this.teamInfo;
   }

   public void setTeamInfo(@Nullable WrapperPlayServerTeams.ScoreBoardTeamInfo teamInfo) {
      this.teamInfo = Optional.ofNullable(teamInfo);
   }

   public static enum TeamMode {
      CREATE,
      REMOVE,
      UPDATE,
      ADD_ENTITIES,
      REMOVE_ENTITIES;

      // $FF: synthetic method
      private static WrapperPlayServerTeams.TeamMode[] $values() {
         return new WrapperPlayServerTeams.TeamMode[]{CREATE, REMOVE, UPDATE, ADD_ENTITIES, REMOVE_ENTITIES};
      }
   }

   public static class ScoreBoardTeamInfo {
      private Component displayName;
      private Component prefix;
      private Component suffix;
      private WrapperPlayServerTeams.NameTagVisibility tagVisibility;
      private WrapperPlayServerTeams.CollisionRule collisionRule;
      private NamedTextColor color;
      private WrapperPlayServerTeams.OptionData optionData;

      public ScoreBoardTeamInfo(Component displayName, @Nullable Component prefix, @Nullable Component suffix, WrapperPlayServerTeams.NameTagVisibility tagVisibility, WrapperPlayServerTeams.CollisionRule collisionRule, NamedTextColor color, WrapperPlayServerTeams.OptionData optionData) {
         this.displayName = displayName;
         if (prefix == null) {
            prefix = Component.empty();
         }

         if (suffix == null) {
            suffix = Component.empty();
         }

         this.prefix = (Component)prefix;
         this.suffix = (Component)suffix;
         this.tagVisibility = tagVisibility;
         this.collisionRule = collisionRule;
         this.color = color;
         this.optionData = optionData;
      }

      public Component getDisplayName() {
         return this.displayName;
      }

      public void setDisplayName(Component displayName) {
         this.displayName = displayName;
      }

      public Component getPrefix() {
         return this.prefix;
      }

      public void setPrefix(Component prefix) {
         this.prefix = prefix;
      }

      public Component getSuffix() {
         return this.suffix;
      }

      public void setSuffix(Component suffix) {
         this.suffix = suffix;
      }

      public WrapperPlayServerTeams.NameTagVisibility getTagVisibility() {
         return this.tagVisibility;
      }

      public void setTagVisibility(WrapperPlayServerTeams.NameTagVisibility tagVisibility) {
         this.tagVisibility = tagVisibility;
      }

      public WrapperPlayServerTeams.CollisionRule getCollisionRule() {
         return this.collisionRule;
      }

      public void setCollisionRule(WrapperPlayServerTeams.CollisionRule collisionRule) {
         this.collisionRule = collisionRule;
      }

      public NamedTextColor getColor() {
         return this.color;
      }

      public void setColor(NamedTextColor color) {
         this.color = color;
      }

      public WrapperPlayServerTeams.OptionData getOptionData() {
         return this.optionData;
      }

      public void setOptionData(WrapperPlayServerTeams.OptionData optionData) {
         this.optionData = optionData;
      }
   }

   public static enum OptionData {
      NONE((byte)0),
      FRIENDLY_FIRE((byte)1),
      FRIENDLY_CAN_SEE_INVISIBLE((byte)2),
      ALL((byte)3);

      private static final WrapperPlayServerTeams.OptionData[] VALUES = values();
      private final byte byteValue;

      private OptionData(byte value) {
         this.byteValue = value;
      }

      public byte getByteValue() {
         return this.byteValue;
      }

      @Nullable
      public static WrapperPlayServerTeams.OptionData fromValue(byte value) {
         for (WrapperPlayServerTeams.OptionData data : VALUES) {
            if (data.getByteValue() == value) {
               return data;
            }
         }
         return null;
      }

      // $FF: synthetic method
      private static WrapperPlayServerTeams.OptionData[] $values() {
         return new WrapperPlayServerTeams.OptionData[]{NONE, FRIENDLY_FIRE, FRIENDLY_CAN_SEE_INVISIBLE, ALL};
      }
   }

   public static enum NameTagVisibility {
      ALWAYS("always"),
      NEVER("never"),
      HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
      HIDE_FOR_OWN_TEAM("hideForOwnTeam");

      private final String id;

      private NameTagVisibility(String id) {
         this.id = id;
      }

      @Nullable
      public static WrapperPlayServerTeams.NameTagVisibility fromID(String id) {
         for (WrapperPlayServerTeams.NameTagVisibility visibility : values()) {
            if (visibility.id.equalsIgnoreCase(id)) {
               return visibility;
            }
         }
         return null;
      }

      public String getId() {
         return this.id;
      }

      // $FF: synthetic method
      private static WrapperPlayServerTeams.NameTagVisibility[] $values() {
         return new WrapperPlayServerTeams.NameTagVisibility[]{ALWAYS, NEVER, HIDE_FOR_OTHER_TEAMS, HIDE_FOR_OWN_TEAM};
      }
   }

   public static enum CollisionRule {
      ALWAYS("always"),
      NEVER("never"),
      PUSH_OTHER_TEAMS("pushOtherTeams"),
      PUSH_OWN_TEAM("pushOwnTeam");

      private final String id;

      private CollisionRule(String id) {
         this.id = id;
      }

      @Nullable
      public static WrapperPlayServerTeams.CollisionRule fromID(String id) {
         for (WrapperPlayServerTeams.CollisionRule rule : values()) {
            if (rule.id.equalsIgnoreCase(id)) {
               return rule;
            }
         }
         return null;
      }

      public String getId() {
         return this.id;
      }

      // $FF: synthetic method
      private static WrapperPlayServerTeams.CollisionRule[] $values() {
         return new WrapperPlayServerTeams.CollisionRule[]{ALWAYS, NEVER, PUSH_OTHER_TEAMS, PUSH_OWN_TEAM};
      }
   }
}
