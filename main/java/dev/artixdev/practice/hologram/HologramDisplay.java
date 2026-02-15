package dev.artixdev.practice.hologram;

import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.expansions.hologram.HologramInterface;
import dev.artixdev.practice.expansions.hologram.HologramManager;

/**
 * Hologram Display
 * Handles hologram display operations
 */
public class HologramDisplay implements HologramInterface {
   
   private final Main plugin;
   private final HologramManager hologramManager;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param hologramManager the hologram manager
    */
   public HologramDisplay(Main plugin, HologramManager hologramManager) {
      this.plugin = plugin;
      this.hologramManager = hologramManager;
   }
   
   /**
    * Display hologram to players
    * @param players the players to display to
    * @param location the location to display at
    */
   public void displayHologram(List<Player> players, Location location) {
      if (players == null || location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Display hologram to each player
         for (Player player : players) {
            if (player != null && player.isOnline()) {
               hologramManager.showHologram(hologram.getName(), player);
            }
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to display hologram: " + e.getMessage());
      }
   }
   
   /**
    * Hide hologram from players
    * @param players the players to hide from
    * @param location the location to hide at
    */
   public void hideHologram(List<Player> players, Location location) {
      if (players == null || location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Hide hologram from each player
         for (Player player : players) {
            if (player != null && player.isOnline()) {
               hologramManager.hideHologram(hologram.getName(), player);
            }
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to hide hologram: " + e.getMessage());
      }
   }
   
   /**
    * Update hologram for players
    * @param players the players to update for
    * @param location the location to update at
    */
   public void updateHologram(List<Player> players, Location location) {
      if (players == null || location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Update hologram for each player
         for (Player player : players) {
            if (player != null && player.isOnline()) {
               hologramManager.updateHologram(hologram.getName(), player);
            }
         }
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update hologram: " + e.getMessage());
      }
   }
   
   /**
    * Display hologram to all players
    * @param location the location to display at
    */
   public void displayHologramToAll(Location location) {
      if (location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Display hologram to all online players
         hologramManager.showHologramToAll(hologram.getName());
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to display hologram to all: " + e.getMessage());
      }
   }
   
   /**
    * Hide hologram from all players
    * @param location the location to hide at
    */
   public void hideHologramFromAll(Location location) {
      if (location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Hide hologram from all online players
         hologramManager.hideHologramFromAll(hologram.getName());
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to hide hologram from all: " + e.getMessage());
      }
   }
   
   /**
    * Update hologram for all players
    * @param location the location to update at
    */
   public void updateHologramForAll(Location location) {
      if (location == null) {
         return;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return;
         }
         
         // Update hologram for all online players
         hologramManager.updateHologramForAll(hologram.getName());
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update hologram for all: " + e.getMessage());
      }
   }
   
   /**
    * Check if hologram is visible to player
    * @param player the player
    * @param location the location
    * @return true if visible
    */
   public boolean isHologramVisible(Player player, Location location) {
      if (player == null || location == null) {
         return false;
      }
      
      try {
         // Get hologram at location
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologramAtLocation(location);
         if (hologram == null) {
            return false;
         }
         
         // Check if hologram is visible to player
         return hologramManager.isHologramVisible(hologram.getName(), player);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check hologram visibility: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Get hologram at location
    * @param location the location
    * @return hologram or null
    */
   public dev.artixdev.practice.models.HologramInterface getHologramAtLocation(Location location) {
      if (location == null) {
         return null;
      }
      
      try {
         return hologramManager.getHologramAtLocation(location);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get hologram at location: " + e.getMessage());
         return null;
      }
   }
   
   /**
    * Get hologram by name
    * @param name the name
    * @return hologram or null
    */
   public dev.artixdev.practice.models.HologramInterface getHologram(String name) {
      if (name == null || name.isEmpty()) {
         return null;
      }
      
      try {
         return hologramManager.getHologram(name);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get hologram: " + e.getMessage());
         return null;
      }
   }
   
   /**
    * Check if hologram exists
    * @param name the name
    * @return true if exists
    */
   public boolean hologramExists(String name) {
      if (name == null || name.isEmpty()) {
         return false;
      }
      
      try {
         return hologramManager.getHologram(name) != null;
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check hologram existence: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Get all holograms
    * @return list of holograms
    */
   public List<dev.artixdev.practice.models.HologramInterface> getAllHolograms() {
      try {
         return hologramManager.getAllHolograms();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get all holograms: " + e.getMessage());
         return Collections.emptyList();
      }
   }
   
   /**
    * Get hologram count
    * @return hologram count
    */
   public int getHologramCount() {
      try {
         return hologramManager.getHologramCount();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get hologram count: " + e.getMessage());
         return 0;
      }
   }
   
   /**
    * Check if hologram is enabled
    * @param name the name
    * @return true if enabled
    */
   public boolean isHologramEnabled(String name) {
      if (name == null || name.isEmpty()) {
         return false;
      }
      
      try {
         dev.artixdev.practice.models.HologramInterface hologram = hologramManager.getHologram(name);
         return hologram != null && hologram.isEnabled();
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check hologram enabled status: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Enable hologram
    * @param name the name
    * @return true if successful
    */
   public boolean enableHologram(String name) {
      if (name == null || name.isEmpty()) {
         return false;
      }
      
      try {
         return hologramManager.enableHologram(name);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to enable hologram: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Disable hologram
    * @param name the name
    * @return true if successful
    */
   public boolean disableHologram(String name) {
      if (name == null || name.isEmpty()) {
         return false;
      }
      
      try {
         return hologramManager.disableHologram(name);
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to disable hologram: " + e.getMessage());
         return false;
      }
   }

    @Override
    public void createHologramLine(String text, Location location, double yOffset) {

    }

    @Override
    public void createHologramLine(String text, Location location, Player player) {

    }

    @Override
    public void createHologramLine(String text, Location location, List<Player> players) {

    }
}
