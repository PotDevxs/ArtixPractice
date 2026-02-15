package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityStatus extends PacketWrapper<WrapperPlayServerEntityStatus> {
   private int entityID;
   private int status;

   public WrapperPlayServerEntityStatus(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityStatus(int entityID, int status) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_STATUS);
      this.entityID = entityID;
      this.status = status;
   }

   public void read() {
      this.entityID = this.readInt();
      this.status = this.readByte();
   }

   public void write() {
      this.writeInt(this.entityID);
      this.writeByte(this.status);
   }

   public void copy(WrapperPlayServerEntityStatus wrapper) {
      this.entityID = wrapper.entityID;
      this.status = wrapper.status;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int status) {
      this.status = status;
   }
}
