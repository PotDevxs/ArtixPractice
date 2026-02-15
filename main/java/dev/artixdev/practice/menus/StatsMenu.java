package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.StatsMenus;
import dev.artixdev.practice.menus.buttons.StatsButton;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * Stats Menu
 * Paginated menu for player statistics
 */
public class StatsMenu extends PaginatedMenu {
   
   private final PlayerProfile targetProfile;
   
   /**
    * Constructor
    * @param targetProfile the target player profile
    */
   public StatsMenu(PlayerProfile targetProfile) {
      super();
      this.targetProfile = targetProfile;
      this.setBordered(true);
      this.setAutoUpdate(true);
   }
   
   @Override
   public int getSize() {
      return 45; // 5 rows
   }
   
   @Override
   public String getPrePaginatedTitle(Player player) {
      String title = StatsMenus.STATS_MENU_TITLE;
      String placeholder = "{player}";
      
      String playerName = targetProfile != null ? targetProfile.getName() : player.getName();
      return title.replace(placeholder, playerName);
   }
   
   @Override
   public Map<Integer, Button> getAllPagesButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // Get player manager
      dev.artixdev.practice.managers.PlayerManager playerManager = Main.getInstance().getPlayerManager();
      
      // Get all player profiles
      Iterator<PlayerProfile> profileIterator = playerManager.getAllPlayerProfiles().values().iterator();
      
      while (profileIterator.hasNext()) {
         PlayerProfile profile = profileIterator.next();
         
         // Only show enabled profiles
         if (profile != null) {
            Integer slot = buttons.size();
            StatsButton statsButton = new StatsButton(targetProfile, profile);
            buttons.put(slot, statsButton);
         }
      }
      
      return buttons;
   }
   
   @Override
   public Map<Integer, Button> getGlobalButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      
      // Add global buttons like back button, refresh button, etc.
      // This would typically include buttons that appear on all pages
      
      return buttons;
   }
   
   /**
    * Get target profile
    * @return target profile
    */
   public PlayerProfile getTargetProfile() {
      return targetProfile;
   }
}
