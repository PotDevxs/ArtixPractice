package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.GeneralMenus;

/**
 * Settings Menu
 * Paginated menu for player settings
 */
public class SettingsMenu extends PaginatedMenu {
   
   /**
    * Constructor
    */
   public SettingsMenu() {
      this.setBordered(true);
   }
   
   @Override
   public String getPrePaginatedTitle(Player player) {
      return GeneralMenus.SETTINGS_MENU_TITLE;
   }
   
   @Override
   public Map<Integer, Button> getAllPagesButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      
      // Add settings buttons
      addSettingsButtons(buttons, player);
      
      return buttons;
   }
   
   /**
    * Add settings buttons
    * @param buttons the buttons map
    * @param player the player
    */
   private void addSettingsButtons(Map<Integer, Button> buttons, Player player) {
      // Add various settings buttons
      // This would typically include buttons for:
      // - Toggle messages
      // - Toggle sounds
      // - Toggle particles
      // - Change language
      // - Privacy settings
      // - etc.
   }
   
   /**
    * Get menu size
    * @return menu size
    */
   @Override
   public int getSize() {
      return 45; // 5 rows
   }
}
