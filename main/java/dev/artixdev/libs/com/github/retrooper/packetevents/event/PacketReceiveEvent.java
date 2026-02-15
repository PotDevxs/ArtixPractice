package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import dev.artixdev.libs.com.github.retrooper.packetevents.exception.PacketProcessException;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.PacketSide;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public class PacketReceiveEvent extends ProtocolPacketEvent<Object> {
   protected PacketReceiveEvent(Object channel, User user, Object player, Object rawByteBuf, boolean autoProtocolTranslation) throws PacketProcessException {
      super(PacketSide.CLIENT, channel, user, player, rawByteBuf, autoProtocolTranslation);
   }

   protected PacketReceiveEvent(int packetID, PacketTypeCommon packetType, ServerVersion serverVersion, Object channel, User user, Object player, Object byteBuf) throws PacketProcessException {
      super(packetID, packetType, serverVersion, channel, user, player, byteBuf);
   }

   public void call(PacketListenerCommon listener) {
      listener.onPacketReceive(this);
   }

   public PacketReceiveEvent clone() {
      try {
         Object clonedBuffer = ByteBufHelper.retainedDuplicate(this.getByteBuf());
         return new PacketReceiveEvent(this.getPacketId(), this.getPacketType(), this.getServerVersion(), this.getChannel(), this.getUser(), this.getPlayer(), clonedBuffer);
      } catch (PacketProcessException ex) {
         ex.printStackTrace();
         return null;
      }
   }
}
