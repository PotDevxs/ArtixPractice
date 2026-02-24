package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import dev.artixdev.practice.Main;

/**
 * Inventory Click Listener
 * Handles inventory click events
 */
public class InventoryClickListener implements Listener {

   private static final boolean DEBUG_MENU_CLICKS = false;
   private final Main plugin;
   
   /**
    * Constructor
    */
   public InventoryClickListener() {
      this.plugin = Main.getInstance();
   }
   
   /**
    * Handle inventory click event
    * @param event the event
    */
   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void onInventoryClick(InventoryClickEvent event) {
      if (!(event.getWhoClicked() instanceof org.bukkit.entity.Player)) return;
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();

      if (dev.artixdev.api.practice.menu.MenuHandler.getInstance().getOpenedMenus().containsKey(player.getUniqueId())) {
         return;
      }
      if (isCustomMenu(event.getView().getTitle())) {
         event.setCancelled(true);
         handleCustomMenuClick(player, event);
      }
   }
   
   /**
    * Check if inventory is a custom menu
    * @param title the inventory title
    * @return true if custom menu
    */
   private boolean isCustomMenu(String title) {
      // Check if the title matches any custom menu titles
      return title != null && (
         title.contains("Practice") ||
         title.contains("Arena") ||
         title.contains("Kit") ||
         title.contains("Queue") ||
         title.contains("Match")
      );
   }
   
   /**
    * Handle custom menu click
    * @param player the player
    * @param event the click event
    */
   private void handleCustomMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      String title = event.getView().getTitle();
      int slot = event.getSlot();

      if (DEBUG_MENU_CLICKS) {
         plugin.getLogger().info(String.format("Player %s clicked slot %d in menu: %s",
            player.getName(), slot, title));
      }

      // Delegate by menu title when menu was not opened via MenuHandler
      if (title.contains("Practice")) {
         handlePracticeMenuClick(player, event);
      } else if (title.contains("Arena")) {
         handleArenaMenuClick(player, event);
      } else if (title.contains("Kit")) {
         handleKitMenuClick(player, event);
      } else if (title.contains("Queue")) {
         handleQueueMenuClick(player, event);
      } else if (title.contains("Match")) {
         handleMatchMenuClick(player, event);
      }
   }
   
   /**
    * Handle practice menu click
    * @param player the player
    * @param event the click event
    */
   private void handlePracticeMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      // Handle practice menu click logic
   }
   
   /**
    * Handle arena menu click
    * @param player the player
    * @param event the click event
    */
   private void handleArenaMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      // Handle arena menu click logic
   }
   
   /**
    * Handle kit menu click
    * @param player the player
    * @param event the click event
    */
   private void handleKitMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      // Handle kit menu click logic
   }
   
   /**
    * Handle queue menu click
    * @param player the player
    * @param event the click event
    */
   private void handleQueueMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      // Handle queue menu click logic
   }
   
   /**
    * Handle match menu click
    * @param player the player
    * @param event the click event
    */
   private void handleMatchMenuClick(org.bukkit.entity.Player player, InventoryClickEvent event) {
      // Handle match menu click logic
   }
}
