package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;

/**
 * Kit Button
 * Button for kit selection in menus
 */
public class KitButton extends Button {
   
   private final Kit kit;
   
   /**
    * Constructor
    * @param kit the kit
    */
   public KitButton(Kit kit) {
      this.kit = kit;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      ItemStack icon = kit.getIcon();
      
      // If kit has no icon, create a default one
      if (icon == null || icon.getType() == org.bukkit.Material.AIR) {
         icon = new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
      }
      
      ItemBuilder itemBuilder = new ItemBuilder(icon.clone());
      
      // Set name
      itemBuilder.name(ChatUtils.colorize("&6" + kit.getName()));
      
      // Build lore
      List<String> lore = new ArrayList<>();
      lore.add(ChatUtils.colorize("&7Click to select this kit"));
      lore.add("");
      lore.add(ChatUtils.colorize("&7Kit Type: &f" + kit.getKitType().getDisplayName()));
      lore.add(ChatUtils.colorize("&7Enabled: " + (kit.isEnabled() ? "&aYes" : "&cNo")));
      
      if (kit.getDescriptionText() != null && !kit.getDescriptionText().isEmpty()) {
         lore.add("");
         lore.add(ChatUtils.colorize("&7Description: &f" + kit.getDescriptionText()));
      }
      
      itemBuilder.lore(lore);
      
      return itemBuilder.build();
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      if (!kit.isEnabled()) {
         player.sendMessage(ChatUtils.colorize("&cThis kit is currently disabled!"));
         return;
      }
      
      // Handle kit selection
      player.sendMessage(ChatUtils.colorize("&aSelected kit: &f" + kit.getName()));
      
      // Apply kit to player
      kit.applyToPlayer(player);
      
      // Close inventory
      player.closeInventory();
   }
   
   /**
    * Get the kit
    * @return kit
    */
   public Kit getKit() {
      return this.kit;
   }
}
