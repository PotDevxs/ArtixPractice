package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;

/**
 * Cosmetics Button
 * Button for cosmetics menu items
 */
public class CosmeticsButton extends Button {
   
   private final String name;
   private final String description;
   private final String materialName;
   private final String action;
   
   /**
    * Constructor
    * @param name the button name
    * @param description the button description
    * @param materialName the material name (XMaterial)
    * @param action the action identifier
    */
   public CosmeticsButton(String name, String description, String materialName, String action) {
      this.name = name;
      this.description = description;
      this.materialName = materialName;
      this.action = action;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      // Parse material
      XMaterial material = XMaterial.matchXMaterial(materialName).orElse(XMaterial.DIAMOND);
      
      ItemBuilder itemBuilder = new ItemBuilder(material);
      
      // Set name
      itemBuilder.name(ChatUtils.colorize("&6&l" + name));
      
      // Set lore
      List<String> lore = new ArrayList<>();
      lore.add("");
      lore.add(ChatUtils.colorize("&7" + description));
      lore.add("");
      lore.add(ChatUtils.colorize("&eClick to open!"));
      
      itemBuilder.lore(lore);
      
      return itemBuilder.build();
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      // Handle click based on action
      // TODO: Implement action handling (open appropriate menu)
      player.sendMessage(ChatUtils.colorize("&7Opening " + name + " menu..."));
   }
}
