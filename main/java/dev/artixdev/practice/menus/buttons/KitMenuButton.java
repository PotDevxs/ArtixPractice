package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Kit;

/**
 * Kit Menu Button
 * Button that opens a kit menu when clicked
 */
public class KitMenuButton extends Button {
   
   private final Kit kit;
   
   /**
    * Constructor
    * @param kit the kit
    */
   public KitMenuButton(Kit kit) {
      this.kit = kit;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      // Return null for now - this would be implemented based on kit needs
      return null;
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      try {
         // Handle kit menu opening logic
         if (kit == null) {
            player.sendMessage("§cKit not found!");
            return;
         }
         
         // Open kit menu or perform kit action
         player.sendMessage("§aOpening kit menu for: " + kit.getName());
         
         // Additional kit menu logic would go here
         
      } catch (Exception e) {
         player.sendMessage("§cAn error occurred while opening the kit menu!");
         e.printStackTrace();
      }
   }
   
   /**
    * Get kit
    * @return kit
    */
   public Kit getKit() {
      return kit;
   }
}
