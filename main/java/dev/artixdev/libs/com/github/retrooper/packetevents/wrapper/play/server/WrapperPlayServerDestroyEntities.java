package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDestroyEntities extends PacketWrapper<WrapperPlayServerDestroyEntities> {
   private int[] entityIDs;

   public WrapperPlayServerDestroyEntities(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDestroyEntities(int... entityIDs) {
      super((PacketTypeCommon)PacketType.Play.Server.DESTROY_ENTITIES);
      this.entityIDs = entityIDs;
   }

   public WrapperPlayServerDestroyEntities(int entityID) {
      super((PacketTypeCommon)PacketType.Play.Server.DESTROY_ENTITIES);
      this.entityIDs = new int[]{entityID};
   }

   public void read() {
      if (this.serverVersion == ServerVersion.V_1_17) {
         this.entityIDs = new int[]{this.readVarInt()};
      } else {
         int i;
         if (this.serverVersion == ServerVersion.V_1_7_10) {
            int entityIDCount = this.readUnsignedByte();
            this.entityIDs = new int[entityIDCount];

            for(i = 0; i < entityIDCount; ++i) {
               this.entityIDs[i] = this.readInt();
            }
         } else {
            int entityIDCount = this.readVarInt();
            this.entityIDs = new int[entityIDCount];

            for(i = 0; i < entityIDCount; ++i) {
               this.entityIDs[i] = this.readVarInt();
            }
         }
      }

   }

   public void write() {
      if (this.serverVersion == ServerVersion.V_1_17) {
         this.writeVarInt(this.entityIDs[0]);
      } else {
         if (this.serverVersion == ServerVersion.V_1_7_10) {
            this.writeByte(this.entityIDs.length);
            for (int entityID : this.entityIDs) {
               this.writeInt(entityID);
            }
         } else {
            this.writeVarInt(this.entityIDs.length);
            for (int entityID : this.entityIDs) {
               this.writeVarInt(entityID);
            }
         }
      }

   }

   public void copy(WrapperPlayServerDestroyEntities wrapper) {
      this.entityIDs = wrapper.entityIDs;
   }

   public int[] getEntityIds() {
      return this.entityIDs;
   }

   public void setEntityIds(int[] entityIDs) {
      this.entityIDs = entityIDs;
   }
}
