package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.Dimension;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class User {
   private final Object channel;
   private ConnectionState decoderState;
   private ConnectionState encoderState;
   private ClientVersion clientVersion;
   private final UserProfile profile;
   private int entityId = -1;
   private int minWorldHeight = 0;
   private int totalWorldHeight = 256;
   private List<NBTCompound> worldNBT;
   private Dimension dimension = new Dimension(0);

   public User(Object channel, ConnectionState connectionState, ClientVersion clientVersion, UserProfile profile) {
      this.channel = channel;
      this.decoderState = connectionState;
      this.encoderState = connectionState;
      this.clientVersion = clientVersion;
      this.profile = profile;
   }

   public Object getChannel() {
      return this.channel;
   }

   public InetSocketAddress getAddress() {
      return (InetSocketAddress)ChannelHelper.remoteAddress(this.channel);
   }

   public ConnectionState getConnectionState() {
      ConnectionState decoderState = this.decoderState;
      ConnectionState encoderState = this.encoderState;
      if (decoderState != encoderState) {
         throw new IllegalArgumentException("Can't get common connection state: " + decoderState + " != " + encoderState);
      } else {
         return decoderState;
      }
   }

   public void setConnectionState(ConnectionState connectionState) {
      this.setDecoderState(connectionState);
      this.setEncoderState(connectionState);
   }

   public ConnectionState getDecoderState() {
      return this.decoderState;
   }

   public void setDecoderState(ConnectionState decoderState) {
      this.decoderState = decoderState;
      PacketEvents.getAPI().getLogManager().debug("Transitioned " + this.getName() + "'s decoder into " + decoderState + " state!");
   }

   public ConnectionState getEncoderState() {
      return this.encoderState;
   }

   public void setEncoderState(ConnectionState encoderState) {
      this.encoderState = encoderState;
      PacketEvents.getAPI().getLogManager().debug("Transitioned " + this.getName() + "'s encoder into " + encoderState + " state!");
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
   }

   public UserProfile getProfile() {
      return this.profile;
   }

   public String getName() {
      return this.profile.getName();
   }

   public UUID getUUID() {
      return this.profile.getUUID();
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public void sendPacket(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, buffer);
   }

   public void sendPacket(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, wrapper);
   }

   public void sendPacketSilently(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.channel, wrapper);
   }

   public void writePacket(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().writePacket(this.channel, wrapper);
   }

   public void flushPackets() {
      ChannelHelper.flush(this.channel);
   }

   public void closeConnection() {
      ChannelHelper.close(this.channel);
   }

   public void closeInventory() {
      WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, (PacketWrapper)closeWindow);
   }

   public void sendMessage(String legacyMessage) {
      Component component = AdventureSerializer.fromLegacyFormat(legacyMessage);
      this.sendMessage(component);
   }

   public void sendMessage(Component component) {
      this.sendMessage(component, ChatTypes.CHAT);
   }

   public void sendMessage(Component component, ChatType type) {
      ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? this.getClientVersion().toServerVersion() : PacketEvents.getAPI().getServerManager().getVersion();
      Object chatPacket;
      if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         chatPacket = new WrapperPlayServerSystemChatMessage(false, component);
      } else {
         Object message;
         if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            message = new ChatMessage_v1_16(component, type, new UUID(0L, 0L));
         } else {
            message = new ChatMessageLegacy(component, type);
         }

         chatPacket = new WrapperPlayServerChatMessage((ChatMessage)message);
      }

      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, (PacketWrapper)chatPacket);
   }

   public void sendTitle(String legacyTitle, String legacySubtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      Component title = AdventureSerializer.fromLegacyFormat(legacyTitle);
      Component subtitle = AdventureSerializer.fromLegacyFormat(legacySubtitle);
      this.sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
   }

   public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? this.getClientVersion().toServerVersion() : PacketEvents.getAPI().getServerManager().getVersion();
      boolean modern = version.isNewerThanOrEquals(ServerVersion.V_1_17);
      PacketWrapper<?> setTitle = null;
      PacketWrapper<?> setSubtitle = null;
      Object animation;
      if (modern) {
         animation = new WrapperPlayServerSetTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
         if (title != null) {
            setTitle = new WrapperPlayServerSetTitleText(title);
         }

         if (subtitle != null) {
            setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
         }
      } else {
         animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_TIMES_AND_DISPLAY, (Component)null, (Component)null, (Component)null, fadeInTicks, stayTicks, fadeOutTicks);
         if (title != null) {
            setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_TITLE, title, (Component)null, (Component)null, 0, 0, 0);
         }

         if (subtitle != null) {
            setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_SUBTITLE, (Component)null, subtitle, (Component)null, 0, 0, 0);
         }
      }

      this.sendPacket((PacketWrapper)animation);
      if (setTitle != null) {
         this.sendPacket((PacketWrapper)setTitle);
      }

      if (setSubtitle != null) {
         this.sendPacket((PacketWrapper)setSubtitle);
      }

   }

   public int getMinWorldHeight() {
      return this.minWorldHeight;
   }

   public void setMinWorldHeight(int minWorldHeight) {
      this.minWorldHeight = minWorldHeight;
   }

   public int getTotalWorldHeight() {
      return this.totalWorldHeight;
   }

   public void setTotalWorldHeight(int totalWorldHeight) {
      this.totalWorldHeight = totalWorldHeight;
   }

   public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
      this.worldNBT = worldNBT.getTags();
   }

   public Dimension getDimension() {
      return this.dimension;
   }

   public void setDimension(Dimension dimension) {
      this.dimension = dimension;
   }

   @Nullable
   public NBTCompound getWorldNBT(String worldName) {
      if (this.worldNBT == null) {
         return null;
      } else {
         Iterator var2 = this.worldNBT.iterator();

         NBTCompound compound;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            compound = (NBTCompound)var2.next();
         } while(!compound.getStringTagOrNull("name").getValue().equals(worldName));

         return compound;
      }
   }
}
