package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3d;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityVelocity extends PacketWrapper<WrapperPlayServerEntityVelocity> {
   private int entityID;
   private Vector3d velocity;

   public WrapperPlayServerEntityVelocity(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityVelocity(int entityID, Vector3d velocity) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_VELOCITY);
      this.entityID = entityID;
      this.velocity = velocity;
   }

   public void read() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.entityID = this.readInt();
      } else {
         this.entityID = this.readVarInt();
      }

      double velX = (double)this.readShort() / 8000.0D;
      double velY = (double)this.readShort() / 8000.0D;
      double velZ = (double)this.readShort() / 8000.0D;
      this.velocity = new Vector3d(velX, velY, velZ);
   }

   public void write() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.writeInt(this.entityID);
      } else {
         this.writeVarInt(this.entityID);
      }

      this.writeShort((int)(this.velocity.x * 8000.0D));
      this.writeShort((int)(this.velocity.y * 8000.0D));
      this.writeShort((int)(this.velocity.z * 8000.0D));
   }

   public void copy(WrapperPlayServerEntityVelocity wrapper) {
      this.entityID = wrapper.entityID;
      this.velocity = wrapper.velocity;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public Vector3d getVelocity() {
      return this.velocity;
   }

   public void setVelocity(Vector3d velocity) {
      this.velocity = velocity;
   }
}
