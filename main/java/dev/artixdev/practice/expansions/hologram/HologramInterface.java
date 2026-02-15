package dev.artixdev.practice.expansions.hologram;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Hologram Interface
 * Defines methods for hologram operations
 */
public interface HologramInterface {
   
   /**
    * Create hologram line with item
    * @param item the item
    * @param player the player
    * @param slot the slot
    */
   default void createHologramLine(ItemStack item, Player player, int slot) {
      // Default implementation - can be overridden
      if (item != null && player != null) {
         // Create hologram line with item
         HologramLine line = new HologramLine(item);
         line.setSlot(slot);
         line.setPlayer(player);
         // Additional logic can be added here
      }
   }
   
   /**
    * Create hologram line with text
    * @param text the text
    * @param location the location
    * @param yOffset the y offset
    */
   void createHologramLine(String text, Location location, double yOffset);
   
   /**
    * Create hologram line with text for specific player
    * @param text the text
    * @param location the location
    * @param player the player
    */
   void createHologramLine(String text, Location location, Player player);
   
   /**
    * Create hologram line with text for multiple players
    * @param text the text
    * @param location the location
    * @param players the players
    */
   void createHologramLine(String text, Location location, List<Player> players);
   
   /**
    * Update hologram line
    * @param line the hologram line
    * @param newText the new text
    */
   default void updateHologramLine(HologramLine line, String newText) {
      if (line != null) {
         line.setText(newText);
         line.update();
      }
   }
   
   /**
    * Remove hologram line
    * @param line the hologram line
    */
   default void removeHologramLine(HologramLine line) {
      if (line != null) {
         line.remove();
      }
   }
   
   /**
    * Check if hologram line exists
    * @param line the hologram line
    * @return true if exists
    */
   default boolean hologramLineExists(HologramLine line) {
      return line != null && line.isValid();
   }
   
   /**
    * Get hologram line at location
    * @param location the location
    * @return hologram line or null
    */
   default HologramLine getHologramLineAt(Location location) {
      // Default implementation - can be overridden
      return null;
   }
   
   /**
    * Get all hologram lines
    * @return list of hologram lines
    */
   default List<HologramLine> getAllHologramLines() {
      // Default implementation - can be overridden
      return new java.util.ArrayList<>();
   }
   
   /**
    * Clear all hologram lines
    */
   default void clearAllHologramLines() {
      List<HologramLine> lines = getAllHologramLines();
      for (HologramLine line : lines) {
         removeHologramLine(line);
      }
   }
   
   /**
    * Check if hologram system is available
    * @return true if available
    */
   default boolean isHologramSystemAvailable() {
      return true; // Default implementation
   }
   
   /**
    * Get hologram system name
    * @return system name
    */
   default String getHologramSystemName() {
      return "Default";
   }
}
