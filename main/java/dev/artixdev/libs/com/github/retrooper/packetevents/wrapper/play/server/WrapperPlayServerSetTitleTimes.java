package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetTitleTimes extends PacketWrapper<WrapperPlayServerSetTitleTimes> {
   private int fadeInTicks;
   private int stayTicks;
   private int fadeOutTicks;

   public WrapperPlayServerSetTitleTimes(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_TITLE_TIMES);
      this.fadeInTicks = fadeInTicks;
      this.stayTicks = stayTicks;
      this.fadeOutTicks = fadeOutTicks;
   }

   public void read() {
      this.fadeInTicks = this.readInt();
      this.stayTicks = this.readInt();
      this.fadeOutTicks = this.readInt();
   }

   public void write() {
      this.writeInt(this.fadeInTicks);
      this.writeInt(this.stayTicks);
      this.writeInt(this.fadeOutTicks);
   }

   public void copy(WrapperPlayServerSetTitleTimes wrapper) {
      this.fadeInTicks = wrapper.fadeInTicks;
      this.stayTicks = wrapper.stayTicks;
      this.fadeOutTicks = wrapper.fadeOutTicks;
   }

   public int getFadeInTicks() {
      return this.fadeInTicks;
   }

   public void setFadeInTicks(int fadeInTicks) {
      this.fadeInTicks = fadeInTicks;
   }

   public int getStayTicks() {
      return this.stayTicks;
   }

   public void setStayTicks(int stayTicks) {
      this.stayTicks = stayTicks;
   }

   public int getFadeOutTicks() {
      return this.fadeOutTicks;
   }

   public void setFadeOutTicks(int fadeOutTicks) {
      this.fadeOutTicks = fadeOutTicks;
   }
}
