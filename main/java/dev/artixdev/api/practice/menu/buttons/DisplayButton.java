package dev.artixdev.api.practice.menu.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

public class DisplayButton extends Button {
   private final ItemStack itemStack;
   private final boolean cancel;

   public ItemStack getButtonItem(Player player) {
      return this.itemStack == null ? new ItemStack(Material.AIR) : this.itemStack;
   }

   public boolean shouldCancel(Player player, ClickType clickType) {
      return this.cancel;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public boolean isCancel() {
      return this.cancel;
   }

   public DisplayButton(ItemStack itemStack, boolean cancel) {
      this.itemStack = itemStack;
      this.cancel = cancel;
   }
}
