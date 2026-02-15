package dev.artixdev.practice.hologram;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.expansions.hologram.HologramInterface;
import dev.artixdev.practice.expansions.hologram.HologramManager;

/**
 * Hologram Renderer
 * Handles hologram rendering operations
 */
public class HologramRenderer implements HologramInterface {
   
   private final Main plugin;
   private final HologramManager hologramManager;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param hologramManager the hologram manager
    */
   public HologramRenderer(Main plugin, HologramManager hologramManager) {
      this.plugin = plugin;
      this.hologramManager = hologramManager;
   }
   
   /**
    * Render hologram to players
    * @param players the players to render to
    * @param location the location to render at
    */
   public void renderHologram(List<Player> players, Location location) {
      if (players == null || location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Render hologram to each player
         for (Player player : players) {
            if (player != null && player.isOnline()) {
               renderHologramToPlayer(player, hologram);
            }
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to render hologram: " + e.getMessage());
      }
   }
   
   /**
    * Render hologram to player
    * @param player the player
    * @param hologram the hologram
    */
   private void renderHologramToPlayer(Player player, dev.artixdev.practice.models.HologramInterface hologram) {
      if (player == null || hologram == null) {
         return;
      }
      
      try {
         // Check if player is in range
         if (!isPlayerInRange(player, hologram.getLocation())) {
            return;
         }
         
         // Render each line of the hologram
         String[] lines = hologram.getLines();
         for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Location lineLocation = hologram.getLocation().clone().add(0, -i * 0.3, 0);
            
            // Create hologram line entity
            createHologramLine(player, lineLocation, line);
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to render hologram to player: " + e.getMessage());
      }
   }
   
   /**
    * Create hologram line
    * @param player the player
    * @param location the location
    * @param text the text
    */
   private void createHologramLine(Player player, Location location, String text) {
      if (player == null || location == null || text == null) {
         return;
      }
      
      try {
         // Use NMS or ProtocolLib to create hologram line
         // This is a simplified implementation
         hologramManager.createHologramLine(player, location, text);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to create hologram line: " + e.getMessage());
      }
   }
   
   /**
    * Check if player is in range
    * @param player the player
    * @param location the location
    * @return true if in range
    */
   private boolean isPlayerInRange(Player player, Location location) {
      if (player == null || location == null) {
         return false;
      }
      
      // Check if player is in the same world
      if (!player.getWorld().equals(location.getWorld())) {
         return false;
      }
      
      // Check distance (default range: 64 blocks)
      double distance = player.getLocation().distance(location);
      return distance <= 64.0;
   }
   
   /**
    * Update hologram rendering
    * @param players the players to update for
    * @param location the location to update at
    */
   public void updateHologramRendering(List<Player> players, Location location) {
      if (players == null || location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Update hologram rendering for each player
         for (Player player : players) {
            if (player != null && player.isOnline()) {
               updateHologramRenderingForPlayer(player, hologram);
            }
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update hologram rendering: " + e.getMessage());
      }
   }
   
   /**
    * Update hologram rendering for player
    * @param player the player
    * @param hologram the hologram
    */
   private void updateHologramRenderingForPlayer(Player player, dev.artixdev.practice.models.HologramInterface hologram) {
      if (player == null || hologram == null) {
         return;
      }
      
      try {
         // Remove old hologram lines
         removeHologramLines(player, hologram);
         
         // Render new hologram lines
         renderHologramToPlayer(player, hologram);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update hologram rendering for player: " + e.getMessage());
      }
   }
   
   /**
    * Remove hologram lines
    * @param player the player
    * @param hologram the hologram
    */
   private void removeHologramLines(Player player, dev.artixdev.practice.models.HologramInterface hologram) {
      if (player == null || hologram == null) {
         return;
      }
      
      try {
         // Remove each line of the hologram
         String[] lines = hologram.getLines();
         for (int i = 0; i < lines.length; i++) {
            Location lineLocation = hologram.getLocation().clone().add(0, -i * 0.3, 0);
            
            // Remove hologram line entity
            removeHologramLine(player, lineLocation);
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove hologram lines: " + e.getMessage());
      }
   }
   
   /**
    * Remove hologram line
    * @param player the player
    * @param location the location
    */
   private void removeHologramLine(Player player, Location location) {
      if (player == null || location == null) {
         return;
      }
      
      try {
         // Use NMS or ProtocolLib to remove hologram line
         // This is a simplified implementation
         hologramManager.removeHologramLine(player, location);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove hologram line: " + e.getMessage());
      }
   }
   
   /**
    * Clear all hologram rendering for player
    * @param player the player
    */
   public void clearHologramRendering(Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Clear all hologram lines for player
         hologramManager.clearHologramLines(player);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to clear hologram rendering: " + e.getMessage());
      }
   }
   
   /**
    * Clear all hologram rendering for all players
    */
   public void clearAllHologramRendering() {
      try {
         // Clear all hologram lines for all players
         hologramManager.clearAllHologramLines();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to clear all hologram rendering: " + e.getMessage());
      }
   }
   
   /**
    * Check if hologram is rendered for player
    * @param player the player
    * @param location the location
    * @return true if rendered
    */
   public boolean isHologramRendered(Player player, Location location) {
      if (player == null || location == null) {
         return false;
      }
      
      try {
         // Check if hologram is rendered for player
         return hologramManager.isHologramRendered(player, location);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check hologram rendering: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Get hologram rendering count for player
    * @param player the player
    * @return rendering count
    */
   public int getHologramRenderingCount(Player player) {
      if (player == null) {
         return 0;
      }
      
      try {
         // Get hologram rendering count for player
         return hologramManager.getHologramRenderingCount(player);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get hologram rendering count: " + e.getMessage());
         return 0;
      }
   }
   
   /**
    * Get total hologram rendering count
    * @return total rendering count
    */
   public int getTotalHologramRenderingCount() {
      try {
         // Get total hologram rendering count
         return hologramManager.getTotalHologramRenderingCount();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get total hologram rendering count: " + e.getMessage());
         return 0;
      }
   }
   
   /**
    * Set hologram rendering range
    * @param range the range
    */
   public void setHologramRenderingRange(double range) {
      try {
         // Set hologram rendering range
         hologramManager.setHologramRenderingRange(range);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to set hologram rendering range: " + e.getMessage());
      }
   }
   
   /**
    * Get hologram rendering range
    * @return rendering range
    */
   public double getHologramRenderingRange() {
      try {
         // Get hologram rendering range
         return hologramManager.getHologramRenderingRange();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get hologram rendering range: " + e.getMessage());
         return 64.0; // Default range
      }
   }
   
   /**
    * Enable hologram rendering
    * @param enabled the enabled status
    */
   public void setHologramRenderingEnabled(boolean enabled) {
      try {
         // Set hologram rendering enabled status
         hologramManager.setHologramRenderingEnabled(enabled);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to set hologram rendering enabled: " + e.getMessage());
      }
   }
   
   /**
    * Check if hologram rendering is enabled
    * @return true if enabled
    */
   public boolean isHologramRenderingEnabled() {
      try {
         // Check if hologram rendering is enabled
         return hologramManager.isHologramRenderingEnabled();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check hologram rendering enabled: " + e.getMessage());
         return true; // Default enabled
      }
   }

   @Override
   public void createHologramLine(String text, Location location, double yOffset) {
      if (text == null || location == null) return;
      Location lineLoc = location.clone().add(0, yOffset, 0);
      for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
         hologramManager.createHologramLine(p, lineLoc, text);
      }
   }

   @Override
   public void createHologramLine(String text, Location location, Player player) {
      if (text == null || location == null || player == null) return;
      hologramManager.createHologramLine(player, location, text);
   }

   @Override
   public void createHologramLine(String text, Location location, List<Player> players) {
      if (text == null || location == null || players == null) return;
      for (Player player : players) {
         if (player != null) hologramManager.createHologramLine(player, location, text);
      }
   }
}
