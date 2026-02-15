package dev.artixdev.api.practice.spigot.equipment;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public final class EquipmentSetEvent extends PlayerEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final EquipmentSetEvent.EquipMethod equipType;
   private final EquipmentType type;
   private boolean cancel = false;
   private ItemStack oldArmorPiece;
   private ItemStack newArmorPiece;

   public EquipmentSetEvent(Player player, EquipmentSetEvent.EquipMethod equipType, EquipmentType type, ItemStack oldArmorPiece, ItemStack newArmorPiece) {
      super(player);
      this.equipType = equipType;
      this.type = type;
      this.oldArmorPiece = oldArmorPiece;
      this.newArmorPiece = newArmorPiece;
   }

   public static final HandlerList getHandlerList() {
      return handlers;
   }

   public final HandlerList getHandlers() {
      return handlers;
   }

   public final boolean isCancelled() {
      return this.cancel;
   }

   public final void setCancelled(boolean cancel) {
      this.cancel = cancel;
   }

   public final EquipmentType getType() {
      return this.type;
   }

   public final ItemStack getOldArmorPiece() {
      return this.oldArmorPiece;
   }

   public final void setOldArmorPiece(ItemStack oldArmorPiece) {
      this.oldArmorPiece = oldArmorPiece;
   }

   public final ItemStack getNewArmorPiece() {
      return this.newArmorPiece;
   }

   public final void setNewArmorPiece(ItemStack newArmorPiece) {
      this.newArmorPiece = newArmorPiece;
   }

   public EquipmentSetEvent.EquipMethod getMethod() {
      return this.equipType;
   }

   public static enum EquipMethod {
      JOIN,
      SHIFT_CLICK,
      DRAG,
      HOTBAR,
      HOTBAR_SWAP,
      DISPENSER,
      BROKE,
      DEATH;
   }
}
