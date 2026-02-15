package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerCloseWindow extends PacketWrapper<WrapperPlayServerCloseWindow> {
   private int windowId;

   public WrapperPlayServerCloseWindow(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCloseWindow(int id) {
      super((PacketTypeCommon)PacketType.Play.Server.CLOSE_WINDOW);
      this.windowId = id;
   }

   public void read() {
      this.windowId = this.readUnsignedByte();
   }

   public void write() {
      this.writeByte(this.windowId);
   }

   public void copy(WrapperPlayServerCloseWindow wrapper) {
      this.windowId = wrapper.windowId;
   }

   /** @deprecated */
   @Deprecated
   public int getWindowId() {
      return this.windowId;
   }

   /** @deprecated */
   @Deprecated
   public void setWindowId(int windowId) {
      this.windowId = windowId;
   }
}
