package dev.artixdev.libs.com.github.retrooper.packetevents.event.simple;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.PacketProcessException;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public class PacketPlayReceiveEvent extends PacketReceiveEvent {
   public PacketPlayReceiveEvent(Object channel, User user, Object player, Object rawByteBuf, boolean autoProtocolTranslation) throws PacketProcessException {
      super(channel, user, player, rawByteBuf, autoProtocolTranslation);
   }

   protected PacketPlayReceiveEvent(int packetId, PacketTypeCommon packetType, ServerVersion serverVersion, Object channel, User user, Object player, Object byteBuf) throws PacketProcessException {
      super(packetId, packetType, serverVersion, channel, user, player, byteBuf);
   }

   public PacketPlayReceiveEvent clone() {
      try {
         Object clonedBuffer = ByteBufHelper.retainedDuplicate(this.getByteBuf());
         return new PacketPlayReceiveEvent(this.getPacketId(), this.getPacketType(), this.getServerVersion(), this.getChannel(), this.getUser(), this.getPlayer(), clonedBuffer);
      } catch (PacketProcessException ex) {
         ex.printStackTrace();
         return null;
      }
   }

   public PacketType.Play.Client getPacketType() {
      return (PacketType.Play.Client)super.getPacketType();
   }
}
