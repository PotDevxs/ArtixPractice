package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.MatchMenu;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * Match Menu Button
 * Button that opens a match menu when clicked
 */
public class MatchMenuButton extends Button {
   
   private final int slot;
   private final Match match;
   private final PlayerProfile playerProfile;
   
   /**
    * Constructor
    * @param match the match
    * @param playerProfile the player profile
    * @param slot the slot
    */
   public MatchMenuButton(Match match, PlayerProfile playerProfile, int slot) {
      this.match = match;
      this.playerProfile = playerProfile;
      this.slot = slot;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      // Return null for now - this would be implemented based on match needs
      return null;
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      try {
         // Create and open match menu
         MatchMenu matchMenu = new MatchMenu(match);
         
         // Open menu using MenuHandler
         MenuHandler.getInstance().openMenu(matchMenu, player);
         
      } catch (Exception e) {
         player.sendMessage("§cAn error occurred while opening the match menu!");
         e.printStackTrace();
      }
   }
   
   /**
    * Get match
    * @return match
    */
   public Match getMatch() {
      return match;
   }
   
   /**
    * Get player profile
    * @return player profile
    */
   public PlayerProfile getPlayerProfile() {
      return playerProfile;
   }
   
   /**
    * Get slot
    * @return slot
    */
   public int getSlot() {
      return slot;
   }
}
