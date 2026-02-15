package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

/**
 * Empty Button
 * A button that does nothing when clicked
 */
public class EmptyButton extends Button {
   
   /**
    * Constructor
    */
   public EmptyButton() {
      // Empty constructor
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      return null;
   }
   
   @Override
   public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
      // Do nothing
   }
}
