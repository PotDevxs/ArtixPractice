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
      // Handle inventory click logic
      // This would typically check if the click is in a custom menu
      // and handle the appropriate action
      
      if (event.getWhoClicked() instanceof org.bukkit.entity.Player) {
         org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
         
         // Check if player is in a custom menu
         if (isCustomMenu(event.getView().getTitle())) {
            event.setCancelled(true);
            handleCustomMenuClick(player, event);
         }
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
      // Handle custom menu click logic
      // This would typically delegate to the appropriate menu handler
      
      String title = event.getView().getTitle();
      int slot = event.getSlot();
      
      // Log the click for debugging
      plugin.getLogger().info(String.format("Player %s clicked slot %d in menu: %s", 
         player.getName(), slot, title));
      
      // Handle different menu types
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
