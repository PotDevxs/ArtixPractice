package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.menus.buttons.MatchInfoButton;
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
      buttons.put(4, new MatchInfoButton(match, MatchInfoButton.InfoType.OVERVIEW));
      buttons.put(20, new MatchInfoButton(match, MatchInfoButton.InfoType.PLAYER1));
      buttons.put(22, new MatchInfoButton(match, MatchInfoButton.InfoType.KIT));
      buttons.put(24, new MatchInfoButton(match, MatchInfoButton.InfoType.PLAYER2));
      buttons.put(31, new MatchInfoButton(match, MatchInfoButton.InfoType.ARENA));
      buttons.put(40, new MatchInfoButton(match, MatchInfoButton.InfoType.STATUS));
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
