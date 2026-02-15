package dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ProtocolVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PacketTransformationUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;

public interface ProtocolManager {
   Map<UUID, Object> CHANNELS = new ConcurrentHashMap();
   Map<Object, User> USERS = new ConcurrentHashMap();

   default Collection<User> getUsers() {
      return USERS.values();
   }

   default Collection<Object> getChannels() {
      return CHANNELS.values();
   }

   ProtocolVersion getPlatformVersion();

   void sendPacket(Object var1, Object var2);

   void sendPacketSilently(Object var1, Object var2);

   void writePacket(Object var1, Object var2);

   void writePacketSilently(Object var1, Object var2);

   void receivePacket(Object var1, Object var2);

   void receivePacketSilently(Object var1, Object var2);

   ClientVersion getClientVersion(Object var1);

   default void sendPackets(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.sendPacket(channel, buf);
      }

   }

   default void sendPacketsSilently(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.sendPacketSilently(channel, buf);
      }

   }

   default void writePackets(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.writePacket(channel, buf);
      }

   }

   default void writePacketsSilently(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.writePacketSilently(channel, buf);
      }

   }

   default void receivePackets(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.receivePacket(channel, buf);
      }

   }

   default void receivePacketsSilently(Object channel, Object... byteBuf) {
      Object[] var3 = byteBuf;
      int var4 = byteBuf.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object buf = var3[var5];
         this.receivePacketSilently(channel, buf);
      }

   }

   default void setClientVersion(Object channel, ClientVersion version) {
      this.getUser(channel).setClientVersion(version);
   }

   @ApiStatus.Internal
   default Object[] transformWrappers(PacketWrapper<?> wrapper, Object channel, boolean outgoing) {
      PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
      Object[] buffers = new Object[wrappers.length];

      for(int i = 0; i < wrappers.length; ++i) {
         wrappers[i].prepareForSend(channel, outgoing);
         buffers[i] = wrappers[i].buffer;
         wrappers[i].buffer = null;
      }

      return buffers;
   }

   default void sendPacket(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, true);
      this.sendPackets(channel, transformed);
   }

   default void sendPacketSilently(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, true);
      this.sendPacketsSilently(channel, transformed);
   }

   default void writePacket(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, true);
      this.writePackets(channel, transformed);
   }

   default void writePacketSilently(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, true);
      this.writePacketsSilently(channel, transformed);
   }

   default void receivePacket(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, false);
      this.receivePackets(channel, transformed);
   }

   default void receivePacketSilently(Object channel, PacketWrapper<?> wrapper) {
      Object[] transformed = this.transformWrappers(wrapper, channel, false);
      this.receivePacketsSilently(channel, transformed);
   }

   default User getUser(Object channel) {
      Object pipeline = ChannelHelper.getPipeline(channel);
      return (User)USERS.get(pipeline);
   }

   default User removeUser(Object channel) {
      Object pipeline = ChannelHelper.getPipeline(channel);
      return (User)USERS.remove(pipeline);
   }

   default void setUser(Object channel, User user) {
      synchronized(channel) {
         Object pipeline = ChannelHelper.getPipeline(channel);
         USERS.put(pipeline, user);
      }

      PacketEvents.getAPI().getInjector().updateUser(channel, user);
   }

   default Object getChannel(UUID uuid) {
      return CHANNELS.get(uuid);
   }
}
