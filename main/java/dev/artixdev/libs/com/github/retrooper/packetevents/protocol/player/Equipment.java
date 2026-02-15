package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;

public class Equipment {
   private EquipmentSlot slot;
   private ItemStack item;

   public Equipment(EquipmentSlot slot, ItemStack item) {
      this.slot = slot;
      this.item = item;
   }

   public EquipmentSlot getSlot() {
      return this.slot;
   }

   public void setSlot(EquipmentSlot slot) {
      this.slot = slot;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public void setItem(ItemStack item) {
      this.item = item;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Equipment)) {
         return false;
      } else {
         Equipment equipment = (Equipment)obj;
         return equipment.getSlot() == this.slot && equipment.getItem().equals(this.item);
      }
   }
}
