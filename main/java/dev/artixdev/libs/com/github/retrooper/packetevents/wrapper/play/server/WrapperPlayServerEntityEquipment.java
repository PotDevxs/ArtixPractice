package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import java.util.ArrayList;
import java.util.List;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.Equipment;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityEquipment extends PacketWrapper<WrapperPlayServerEntityEquipment> {
   private int entityId;
   private List<Equipment> equipment;

   public WrapperPlayServerEntityEquipment(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerEntityEquipment(int entityId, List<Equipment> equipment) {
      super((PacketTypeCommon)PacketType.Play.Server.ENTITY_EQUIPMENT);
      this.entityId = entityId;
      this.equipment = equipment;
   }

   public void read() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.entityId = this.readInt();
      } else {
         this.entityId = this.readVarInt();
      }

      this.equipment = new ArrayList();
      byte value;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         do {
            value = this.readByte();
            EquipmentSlot equipmentSlot = EquipmentSlot.getById(this.serverVersion, value & 127);
            ItemStack itemStack = this.readItemStack();
            this.equipment.add(new Equipment(equipmentSlot, itemStack));
         } while((value & -128) != 0);
      } else {
         EquipmentSlot slot;
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            slot = EquipmentSlot.getById(this.serverVersion, this.readVarInt());
         } else {
            slot = EquipmentSlot.getById(this.serverVersion, this.readShort());
         }

         this.equipment.add(new Equipment(slot, this.readItemStack()));
      }

   }

   public void write() {
      if (this.serverVersion == ServerVersion.V_1_7_10) {
         this.writeInt(this.entityId);
      } else {
         this.writeVarInt(this.entityId);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
         for(int i = 0; i < this.equipment.size(); ++i) {
            Equipment equipment = (Equipment)this.equipment.get(i);
            boolean last = i == this.equipment.size() - 1;
            this.writeByte(last ? equipment.getSlot().getId(this.serverVersion) : equipment.getSlot().getId(this.serverVersion) | -128);
            this.writeItemStack(equipment.getItem());
         }
      } else {
         Equipment equipment = (Equipment)this.equipment.get(0);
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.writeVarInt(equipment.getSlot().getId(this.serverVersion));
         } else {
            this.writeShort(equipment.getSlot().getId(this.serverVersion));
         }

         this.writeItemStack(equipment.getItem());
      }

   }

   public void copy(WrapperPlayServerEntityEquipment wrapper) {
      this.entityId = wrapper.entityId;
      this.equipment = wrapper.equipment;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public List<Equipment> getEquipment() {
      return this.equipment;
   }

   public void setEquipment(List<Equipment> equipment) {
      this.equipment = equipment;
   }
}
