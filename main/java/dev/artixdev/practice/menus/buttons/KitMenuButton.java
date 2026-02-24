package dev.artixdev.practice.menus.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

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
      if (kit == null) return new ItemStack(Material.BARRIER);
      ItemStack icon = kit.getDisplayIcon() != null ? kit.getDisplayIcon().clone() : new ItemStack(Material.DIAMOND_SWORD);
      String name = kit.getDisplayName() != null ? kit.getDisplayName() : kit.getName();
      List<String> lore = new ArrayList<>();
      lore.add(ChatUtils.translate("&7" + kit.getName()));
      lore.add(ChatUtils.translate("&eClick to open kit menu."));
      return new ItemBuilder(icon).name(ChatUtils.translate("&a" + name)).lore(lore).build();
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
