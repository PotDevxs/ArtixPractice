package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.PacketProcessException;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.PacketSide;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public class PacketSendEvent extends ProtocolPacketEvent<Object> {
   private List<Runnable> tasksAfterSend = null;

   protected PacketSendEvent(Object channel, User user, Object player, Object rawByteBuf, boolean autoProtocolTranslation) throws PacketProcessException {
      super(PacketSide.SERVER, channel, user, player, rawByteBuf, autoProtocolTranslation);
   }

   protected PacketSendEvent(int packetID, PacketTypeCommon packetType, ServerVersion serverVersion, Object channel, User user, Object player, Object byteBuf) throws PacketProcessException {
      super(packetID, packetType, serverVersion, channel, user, player, byteBuf);
   }

   public void call(PacketListenerCommon listener) {
      listener.onPacketSend(this);
   }

   public List<Runnable> getTasksAfterSend() {
      if (this.tasksAfterSend == null) {
         this.tasksAfterSend = new ArrayList();
      }

      return this.tasksAfterSend;
   }

   public boolean hasTasksAfterSend() {
      return this.tasksAfterSend != null && !this.tasksAfterSend.isEmpty();
   }

   public PacketSendEvent clone() {
      try {
         Object clonedBuffer = ByteBufHelper.retainedDuplicate(this.getByteBuf());
         return new PacketSendEvent(this.getPacketId(), this.getPacketType(), this.getServerVersion(), this.getChannel(), this.getUser(), this.getPlayer(), clonedBuffer);
      } catch (PacketProcessException ex) {
         ex.printStackTrace();
         return null;
      }
   }
}
