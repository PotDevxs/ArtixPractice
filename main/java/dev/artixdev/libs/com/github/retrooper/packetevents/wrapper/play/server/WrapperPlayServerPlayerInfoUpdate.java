package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class WrapperPlayServerPlayerInfoUpdate extends PacketWrapper<WrapperPlayServerPlayerInfoUpdate> {
   private EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions;
   private List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries;

   public WrapperPlayServerPlayerInfoUpdate(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPlayerInfoUpdate(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions, List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_UPDATE);
      this.actions = actions;
      this.entries = entries;
   }

   public WrapperPlayServerPlayerInfoUpdate(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions, WrapperPlayServerPlayerInfoUpdate.PlayerInfo... entries) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_UPDATE);
      this.actions = actions;
      this.entries = new ArrayList();
      Collections.addAll(this.entries, entries);
   }

   public WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action action, List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      this(EnumSet.of(action), entries);
   }

   public WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action action, WrapperPlayServerPlayerInfoUpdate.PlayerInfo... entries) {
      this(EnumSet.of(action), entries);
   }

   public void read() {
      this.actions = this.readEnumSet(WrapperPlayServerPlayerInfoUpdate.Action.class);
      this.entries = this.readList((wrapper) -> {
         UUID uuid = wrapper.readUUID();
         UserProfile gameProfile = new UserProfile(uuid, (String)null);
         GameMode gameMode = GameMode.defaultGameMode();
         boolean listed = false;
         int latency = 0;
         RemoteChatSession chatSession = null;
         Component displayName = null;
         for (WrapperPlayServerPlayerInfoUpdate.Action action : this.actions) {
            switch (action) {
               case ADD_PLAYER:
                  gameProfile.setUUID(uuid);
                  gameProfile.setName(wrapper.readString(16));
                  int propertyCount = wrapper.readVarInt();
                  for (int j = 0; j < propertyCount; j++) {
                     String propertyName = wrapper.readString();
                     String propertyValue = wrapper.readString();
                     String propertySignature = (String) wrapper.readOptional(PacketWrapper::readString);
                     TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
                     gameProfile.getTextureProperties().add(textureProperty);
                  }
                  break;
               case INITIALIZE_CHAT:
                  chatSession = (RemoteChatSession) wrapper.readOptional(PacketWrapper::readRemoteChatSession);
                  break;
               case UPDATE_GAME_MODE:
                  gameMode = GameMode.getById(wrapper.readVarInt());
                  break;
               case UPDATE_LISTED:
                  listed = wrapper.readBoolean();
                  break;
               case UPDATE_LATENCY:
                  latency = wrapper.readVarInt();
                  break;
               case UPDATE_DISPLAY_NAME:
                  displayName = (Component) wrapper.readOptional(PacketWrapper::readComponent);
            }
         }
         return new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, listed, latency, gameMode, displayName, chatSession);
      });
   }

   public void write() {
      this.writeEnumSet(this.actions, WrapperPlayServerPlayerInfoUpdate.Action.class);
      this.writeList(this.entries, (wrapper, playerInfo) -> {
         wrapper.writeUUID(playerInfo.getProfileId());
         for (WrapperPlayServerPlayerInfoUpdate.Action action : this.actions) {
            switch (action) {
            case ADD_PLAYER:
               wrapper.writeString(playerInfo.getGameProfile().getName(), 16);
               this.writeList(playerInfo.getGameProfile().getTextureProperties(), (w, textureProperty) -> {
                  w.writeString(textureProperty.getName());
                  w.writeString(textureProperty.getValue());
                  w.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
               });
               break;
            case INITIALIZE_CHAT:
               wrapper.writeOptional(playerInfo.getChatSession(), PacketWrapper::writeRemoteChatSession);
               break;
            case UPDATE_GAME_MODE:
               wrapper.writeVarInt(playerInfo.getGameMode().getId());
               break;
            case UPDATE_LISTED:
               wrapper.writeBoolean(playerInfo.isListed());
               break;
            case UPDATE_LATENCY:
               wrapper.writeVarInt(playerInfo.getLatency());
               break;
            case UPDATE_DISPLAY_NAME:
               wrapper.writeOptional(playerInfo.getDisplayName(), PacketWrapper::writeComponent);
            }
         }
      });
   }

   public void copy(WrapperPlayServerPlayerInfoUpdate wrapper) {
      this.actions = wrapper.actions;
      this.entries = wrapper.entries;
   }

   public EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> getActions() {
      return this.actions;
   }

   public void setActions(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions) {
      this.actions = actions;
   }

   public List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> getEntries() {
      return this.entries;
   }

   public void setEntries(List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      this.entries = entries;
   }

   public static class PlayerInfo {
      private UserProfile gameProfile;
      private boolean listed;
      private int latency;
      private GameMode gameMode;
      @Nullable
      private Component displayName;
      @Nullable
      private RemoteChatSession chatSession;

      public PlayerInfo(UserProfile gameProfile, boolean listed, int latency, GameMode gameMode, @Nullable Component displayName, @Nullable RemoteChatSession chatSession) {
         this.gameProfile = gameProfile;
         this.listed = listed;
         this.latency = latency;
         this.gameMode = gameMode;
         this.displayName = displayName;
         this.chatSession = chatSession;
      }

      /** @deprecated */
      @Deprecated
      public UUID getProfileId() {
         return this.gameProfile.getUUID();
      }

      public UserProfile getGameProfile() {
         return this.gameProfile;
      }

      public boolean isListed() {
         return this.listed;
      }

      public int getLatency() {
         return this.latency;
      }

      public GameMode getGameMode() {
         return this.gameMode;
      }

      @Nullable
      public Component getDisplayName() {
         return this.displayName;
      }

      @Nullable
      public RemoteChatSession getChatSession() {
         return this.chatSession;
      }

      public void setGameProfile(UserProfile gameProfile) {
         this.gameProfile = gameProfile;
      }

      public void setListed(boolean listed) {
         this.listed = listed;
      }

      public void setLatency(int latency) {
         this.latency = latency;
      }

      public void setGameMode(GameMode gameMode) {
         this.gameMode = gameMode;
      }

      public void setDisplayName(@Nullable Component displayName) {
         this.displayName = displayName;
      }

      public void setChatSession(@Nullable RemoteChatSession chatSession) {
         this.chatSession = chatSession;
      }
   }

   public static enum Action {
      ADD_PLAYER,
      INITIALIZE_CHAT,
      UPDATE_GAME_MODE,
      UPDATE_LISTED,
      UPDATE_LATENCY,
      UPDATE_DISPLAY_NAME;

      public static final WrapperPlayServerPlayerInfoUpdate.Action[] VALUES = values();

      // $FF: synthetic method
      private static WrapperPlayServerPlayerInfoUpdate.Action[] $values() {
         return new WrapperPlayServerPlayerInfoUpdate.Action[]{ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME};
      }
   }
}
