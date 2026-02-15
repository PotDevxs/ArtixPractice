package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Post Match Inventory Menu
 * Menu for viewing post-match inventory of a player
 */
public class PostMatchInventoryMenu extends Menu {
   
   private final Player target;
   private final Main plugin;
   
   /**
    * Constructor
    * @param target the target player whose inventory to view
    */
   public PostMatchInventoryMenu(Player target) {
      this.target = target;
      this.plugin = Main.getInstance();
   }
   
   @Override
   public String getTitle(Player player) {
      String title = MatchMenus.MATCH_INVENTORY_TITLE;
      if (title == null || title.isEmpty()) {
         return ChatUtils.colorize("&6&lPost-Match Inventory: &f" + target.getName());
      }
      return ChatUtils.colorize(title.replace("%player%", target.getName()));
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // Get target player's profile
      PlayerProfile targetProfile = plugin.getPlayerManager().getPlayerProfile(target.getUniqueId());
      if (targetProfile == null) {
         return buttons;
      }
      
      // Get target's current match
      if (targetProfile.getCurrentMatch() == null) {
         return buttons;
      }
      
      // Add inventory items as buttons
      // This is a placeholder - in a full implementation, you would
      // display the player's inventory items from the match
      addInventoryButtons(buttons, player, targetProfile);
      
      return buttons;
   }
   
   /**
    * Add inventory buttons
    * @param buttons the buttons map
    * @param viewer the viewer
    * @param targetProfile the target player's profile
    */
   private void addInventoryButtons(Map<Integer, Button> buttons, Player viewer, PlayerProfile targetProfile) {
      // Placeholder implementation
      // In a full implementation, this would display the target player's
      // inventory items from their last match
      
      // You could iterate through the target's inventory and create buttons
      // for each item, or display match statistics, etc.
   }
   
   @Override
   public int getSize() {
      return 54; // Standard chest size
   }
}
