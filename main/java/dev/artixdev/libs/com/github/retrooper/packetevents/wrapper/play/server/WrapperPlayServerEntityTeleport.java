package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.Location;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.MathUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3d;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityTeleport extends PacketWrapper<WrapperPlayServerEntityTeleport> {
   private static final float ROTATION_FACTOR = 0.7111111F;
   private int entityID;
   private Vector3d position;
   private float yaw;
   private float pitch;
   private boolean onGround;

   public WrapperPlayServerEntityTeleport(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityTeleport(int entityID, Location location, boolean onGround) {
      this(entityID, location.getPosition(), location.getYaw(), location.getPitch(), onGround);
   }

   public WrapperPlayServerEntityTeleport(int entityID, Vector3d position, float yaw, float pitch, boolean onGround) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_TELEPORT);
      this.entityID = entityID;
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
   }

   public void read() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.entityID = this.readInt();
      } else {
         this.entityID = this.readVarInt();
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
         this.position = new Vector3d((double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D, (double)this.readInt() / 32.0D);
      } else {
         this.position = new Vector3d(this.readDouble(), this.readDouble(), this.readDouble());
      }

      this.yaw = (float)this.readByte() / 0.7111111F;
      this.pitch = (float)this.readByte() / 0.7111111F;
      if (this.serverVersion != ServerVersion.V_1_7_10) {
         this.onGround = this.readBoolean();
      } else {
         this.onGround = false;
      }

   }

   public void write() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.writeInt(this.entityID);
      } else {
         this.writeVarInt(this.entityID);
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_8_8)) {
         this.writeInt(MathUtil.floor(this.position.x * 32.0D));
         this.writeInt(MathUtil.floor(this.position.y * 32.0D));
         this.writeInt(MathUtil.floor(this.position.z * 32.0D));
      } else {
         this.writeDouble(this.position.x);
         this.writeDouble(this.position.y);
         this.writeDouble(this.position.z);
      }

      this.writeByte((int)(this.yaw * 0.7111111F));
      this.writeByte((int)(this.pitch * 0.7111111F));
      if (this.serverVersion != ServerVersion.V_1_7_10) {
         this.writeBoolean(this.onGround);
      }

   }

   public void copy(WrapperPlayServerEntityTeleport wrapper) {
      this.entityID = wrapper.entityID;
      this.position = wrapper.position;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
      this.onGround = wrapper.onGround;
   }

   public int getEntityId() {
      return this.entityID;
   }

   public void setEntityId(int entityID) {
      this.entityID = entityID;
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}
