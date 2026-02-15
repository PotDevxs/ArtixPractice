package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.QueueMenus;

/**
 * Queue Type Menu
 * Menu for selecting queue types
 */
public class QueueTypeMenu extends Menu {
   
   private final boolean isRanked;
   
   /**
    * Constructor
    * @param isRanked whether the queue is ranked
    */
   public QueueTypeMenu(boolean isRanked) {
      this.isRanked = isRanked;
      this.setPlaceholder(true);
   }
   
   @Override
   public int getSize() {
      return QueueMenus.QUEUE_TYPE_SIZE;
   }
   
   @Override
   public String getTitle(Player player) {
      return QueueMenus.QUEUE_TYPE_TITLE;
   }
   
   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      
      // Add queue type buttons based on whether it's ranked or not
      if (isRanked) {
         addRankedQueueButtons(buttons, player);
      } else {
         addUnrankedQueueButtons(buttons, player);
      }
      
      return buttons;
   }
   
   /**
    * Add ranked queue buttons
    * @param buttons the buttons map
    * @param player the player
    */
   private void addRankedQueueButtons(Map<Integer, Button> buttons, Player player) {
      // Add ranked queue buttons
      // This would typically iterate through available ranked queue types
      // and add appropriate buttons for each type
   }
   
   /**
    * Add unranked queue buttons
    * @param buttons the buttons map
    * @param player the player
    */
   private void addUnrankedQueueButtons(Map<Integer, Button> buttons, Player player) {
      // Add unranked queue buttons
      // This would typically iterate through available unranked queue types
      // and add appropriate buttons for each type
   }
   
   /**
    * Check if queue is ranked
    * @return true if ranked
    */
   public boolean isRanked() {
      return isRanked;
   }
}
