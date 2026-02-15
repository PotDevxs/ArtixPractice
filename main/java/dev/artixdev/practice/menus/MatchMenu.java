package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Match Menu
 * Menu for displaying match information
 */
public class MatchMenu extends Menu {
   
   private final Match match;
   
   /**
    * Constructor
    * @param match the match
    */
   public MatchMenu(Match match) {
      this.match = match;
      this.setPlaceholder(true);
   }
   
   @Override
   public String getTitle(Player player) {
      return ChatUtils.colorize("&6&lMatch Information");
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // TODO: Add buttons for match information
      // This would typically show match details like:
      // - Participants
      // - Kit
      // - Arena
      // - Match status
      // - Time elapsed
      // - Statistics
      
      return buttons;
   }
   
   @Override
   public int getSize() {
      return 54; // 6 rows
   }
   
   /**
    * Get match
    * @return match
    */
   public Match getMatch() {
      return match;
   }
}
