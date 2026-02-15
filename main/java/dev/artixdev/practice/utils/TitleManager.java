package dev.artixdev.practice.utils;

import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;

/**
 * Title Manager
 * Manages title and subtitle display for players
 */
public class TitleManager {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public TitleManager(Main plugin) {
      this.plugin = plugin;
   }
   
   /**
    * Send title to player
    * @param player the player
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time (ticks)
    * @param stay stay time (ticks)
    * @param fadeOut fade out time (ticks)
    */
   public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      try {
         // Use Bukkit's built-in title API
         player.sendTitle(title, subtitle);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to send title to " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Send title to player with default timing
    * @param player the player
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitle(Player player, String title, String subtitle) {
      sendTitle(player, title, subtitle, 10, 70, 20);
   }
   
   /**
    * Send title only to player
    * @param player the player
    * @param title the title
    */
   public void sendTitleOnly(Player player, String title) {
      sendTitle(player, title, "", 10, 70, 20);
   }
   
   /**
    * Send subtitle only to player
    * @param player the player
    * @param subtitle the subtitle
    */
   public void sendSubtitleOnly(Player player, String subtitle) {
      sendTitle(player, "", subtitle, 10, 70, 20);
   }
   
   /**
    * Reset title for player
    * @param player the player
    */
   public void resetTitle(Player player) {
      try {
         player.resetTitle();
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to reset title for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Send action bar message to player
    * @param player the player
    * @param message the message
    */
   public void sendActionBar(Player player, String message) {
      try {
         // Fallback to chat message since sendActionBar is not available
         player.sendMessage("§7[ActionBar] " + message);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to send action bar to " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Send title to all online players
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time (ticks)
    * @param stay stay time (ticks)
    * @param fadeOut fade out time (ticks)
    */
   public void sendTitleToAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      for (Player player : plugin.getServer().getOnlinePlayers()) {
         sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
      }
   }
   
   /**
    * Send title to all online players with default timing
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitleToAll(String title, String subtitle) {
      sendTitleToAll(title, subtitle, 10, 70, 20);
   }
   
   /**
    * Send title to all online players in a specific world
    * @param worldName the world name
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time (ticks)
    * @param stay stay time (ticks)
    * @param fadeOut fade out time (ticks)
    */
   public void sendTitleToWorld(String worldName, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      for (Player player : plugin.getServer().getOnlinePlayers()) {
         if (player.getWorld().getName().equals(worldName)) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
         }
      }
   }
   
   /**
    * Send title to all online players in a specific world with default timing
    * @param worldName the world name
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitleToWorld(String worldName, String title, String subtitle) {
      sendTitleToWorld(worldName, title, subtitle, 10, 70, 20);
   }
   
   /**
    * Send title to players with specific permission
    * @param permission the permission
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time (ticks)
    * @param stay stay time (ticks)
    * @param fadeOut fade out time (ticks)
    */
   public void sendTitleToPermission(String permission, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      for (Player player : plugin.getServer().getOnlinePlayers()) {
         if (player.hasPermission(permission)) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
         }
      }
   }
   
   /**
    * Send title to players with specific permission with default timing
    * @param permission the permission
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitleToPermission(String permission, String title, String subtitle) {
      sendTitleToPermission(permission, title, subtitle, 10, 70, 20);
   }
   
   /**
    * Send title to players in a specific range
    * @param center the center location
    * @param range the range
    * @param title the title
    * @param subtitle the subtitle
    * @param fadeIn fade in time (ticks)
    * @param stay stay time (ticks)
    * @param fadeOut fade out time (ticks)
    */
   public void sendTitleToRange(org.bukkit.Location center, double range, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
      for (Player player : plugin.getServer().getOnlinePlayers()) {
         if (player.getWorld().equals(center.getWorld()) && 
             player.getLocation().distance(center) <= range) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
         }
      }
   }
   
   /**
    * Send title to players in a specific range with default timing
    * @param center the center location
    * @param range the range
    * @param title the title
    * @param subtitle the subtitle
    */
   public void sendTitleToRange(org.bukkit.Location center, double range, String title, String subtitle) {
      sendTitleToRange(center, range, title, subtitle, 10, 70, 20);
   }
   
   /**
    * Get title manager statistics
    * @return statistics
    */
   public String getStatistics() {
      return String.format("TitleManager: Active, Online Players: %d", 
         plugin.getServer().getOnlinePlayers().size());
   }
}
