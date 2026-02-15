package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.menus.buttons.CosmeticsButton;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Cosmetics Menu
 * Menu for managing player cosmetics
 */
public class CosmeticsMenu extends Menu {
   
   /**
    * Constructor
    */
   public CosmeticsMenu() {
      // Empty constructor
   }
   
   @Override
   public String getTitle(Player player) {
      return ChatUtils.colorize("&6&lCosmetics Menu");
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // Add cosmetics buttons
      addCosmeticsButtons(buttons, player);
      
      return buttons;
   }
   
   /**
    * Add cosmetics buttons
    * @param buttons the buttons map
    * @param player the player
    */
   private void addCosmeticsButtons(Map<Integer, Button> buttons, Player player) {
      // Kill Messages Button
      buttons.put(GeneralMenus.COSMETICS_MENU_KILL_MESSAGES_SLOT, new CosmeticsButton(
         "Kill Messages",
         "Customize your kill messages",
         GeneralMenus.COSMETICS_MENU_KILL_MESSAGES_MATERIAL,
         "kill_messages"
      ));
      
      // Kill Effects Button
      buttons.put(GeneralMenus.COSMETICS_MENU_KILL_EFFECTS_SLOT, new CosmeticsButton(
         "Kill Effects",
         "Customize your kill effects",
         GeneralMenus.COSMETICS_MENU_KILL_EFFECTS_MATERIAL,
         "kill_effects"
      ));
      
      // Projectile Trails Button
      buttons.put(GeneralMenus.COSMETICS_MENU_PROJECTILE_TRAILS_SLOT, new CosmeticsButton(
         "Projectile Trails",
         "Customize your projectile trails",
         "ARROW",
         "projectile_trails"
      ));
      
      // Victory Effects Button
      buttons.put(GeneralMenus.COSMETICS_MENU_VICTORY_EFFECTS_SLOT, new CosmeticsButton(
         "Victory Effects",
         "Customize your victory effects",
         "GOLDEN_APPLE",
         "victory_effects"
      ));
   }
   
   @Override
   public int getSize() {
      return 54; // 6 rows
   }
   
}
