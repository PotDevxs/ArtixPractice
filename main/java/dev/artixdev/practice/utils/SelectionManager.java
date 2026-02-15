package dev.artixdev.practice.utils;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.cuboid.Cuboid;

/**
 * Selection Manager
 * Manages player selections for various purposes
 */
public class SelectionManager {
   
   private Location firstPosition;
   private Location secondPosition;
   private static final String SELECTION_METADATA = "CLAIM_SELECTION";
   public static ItemStack SELECTION_ITEM;

   /**
    * Constructor
    */
   public SelectionManager() {
      // Initialize selection item
      SELECTION_ITEM = XMaterial.WOODEN_AXE.parseItem();
      if (SELECTION_ITEM != null) {
         org.bukkit.inventory.meta.ItemMeta meta = SELECTION_ITEM.getItemMeta();
         if (meta != null) {
            meta.setDisplayName("§aSelection Tool");
            meta.setLore(Arrays.asList(
               "§7Left click to set first position",
               "§7Right click to set second position",
               "§7Use /clearselection to clear"
            ));
            SELECTION_ITEM.setItemMeta(meta);
         }
      }
   }
   
   /**
    * Get first position
    * @return first position
    */
   public Location getFirstPosition() {
      return firstPosition;
   }
   
   /**
    * Set first position
    * @param firstPosition the first position
    */
   public void setFirstPosition(Location firstPosition) {
      this.firstPosition = firstPosition;
   }
   
   /**
    * Get second position
    * @return second position
    */
   public Location getSecondPosition() {
      return secondPosition;
   }
   
   /**
    * Set second position
    * @param secondPosition the second position
    */
   public void setSecondPosition(Location secondPosition) {
      this.secondPosition = secondPosition;
   }
   
   /**
    * Check if selection is complete
    * @return true if complete
    */
   public boolean isSelectionComplete() {
      return firstPosition != null && secondPosition != null;
   }
   
   /**
    * Get selection cuboid
    * @return selection cuboid
    */
   public Cuboid getSelectionCuboid() {
      if (!isSelectionComplete()) {
         return null;
      }
      
      return new Cuboid(firstPosition, secondPosition);
   }
   
   /**
    * Clear selection
    */
   public void clearSelection() {
      firstPosition = null;
      secondPosition = null;
   }
   
   /**
    * Get selection size
    * @return selection size
    */
   public int getSelectionSize() {
      if (!isSelectionComplete()) {
         return 0;
      }
      
      Cuboid cuboid = getSelectionCuboid();
      return cuboid != null ? cuboid.getSize() : 0;
   }
   
   /**
    * Get selection volume
    * @return selection volume
    */
   public int getSelectionVolume() {
      if (!isSelectionComplete()) {
         return 0;
      }
      
      Cuboid cuboid = getSelectionCuboid();
      return cuboid != null ? cuboid.getVolume() : 0;
   }
   
   /**
    * Check if selection is valid
    * @return true if valid
    */
   public boolean isValidSelection() {
      if (!isSelectionComplete()) {
         return false;
      }
      
      // Check if positions are in the same world
      if (!firstPosition.getWorld().equals(secondPosition.getWorld())) {
         return false;
      }
      
      // Check if selection is not too large
      int volume = getSelectionVolume();
      return volume > 0 && volume <= 1000000; // Max 1M blocks
   }
   
   /**
    * Get selection info
    * @return selection info
    */
   public String getSelectionInfo() {
      if (!isSelectionComplete()) {
         return "Selection incomplete";
      }
      
      Cuboid cuboid = getSelectionCuboid();
      if (cuboid == null) {
         return "Invalid selection";
      }
      
      return String.format("Selection: %dx%dx%d (%d blocks)", 
         cuboid.getWidth(), cuboid.getHeight(), cuboid.getLength(), cuboid.getVolume());
   }
   
   /**
    * Get selection center
    * @return selection center
    */
   public Location getSelectionCenter() {
      if (!isSelectionComplete()) {
         return null;
      }
      
      Cuboid cuboid = getSelectionCuboid();
      return cuboid != null ? cuboid.getCenter() : null;
   }
   
   /**
    * Check if location is in selection
    * @param location the location
    * @return true if in selection
    */
   public boolean isInSelection(Location location) {
      if (!isSelectionComplete()) {
         return false;
      }
      
      Cuboid cuboid = getSelectionCuboid();
      return cuboid != null && cuboid.contains(location);
   }
   
   /**
    * Get selection bounds
    * @return selection bounds
    */
   public String getSelectionBounds() {
      if (!isSelectionComplete()) {
         return "No selection";
      }
      
      return String.format("From: %s To: %s", 
         formatLocation(firstPosition), 
         formatLocation(secondPosition));
   }
   
   /**
    * Format location
    * @param location the location
    * @return formatted location
    */
   private String formatLocation(Location location) {
      return String.format("%.1f, %.1f, %.1f", 
         location.getX(), location.getY(), location.getZ());
   }
   
   /**
    * Get selection tool
    * @return selection tool
    */
   public ItemStack getSelectionTool() {
      return SELECTION_ITEM.clone();
   }
   
   /**
    * Give selection tool to player
    * @param player the player
    */
   public void giveSelectionTool(Player player) {
      if (SELECTION_ITEM != null) {
         player.getInventory().addItem(SELECTION_ITEM.clone());
         player.sendMessage("§aSelection tool added to your inventory!");
      }
   }
   
   /**
    * Remove selection tool from player
    * @param player the player
    */
   public void removeSelectionTool(Player player) {
      player.getInventory().remove(SELECTION_ITEM);
      player.sendMessage("§cSelection tool removed from your inventory!");
   }
   
   /**
    * Check if player has selection tool
    * @param player the player
    * @return true if has tool
    */
   public boolean hasSelectionTool(Player player) {
      return player.getInventory().contains(SELECTION_ITEM);
   }
   
   /**
    * Get selection metadata
    * @return selection metadata
    */
   public static String getSelectionMetadata() {
      return SELECTION_METADATA;
   }
   
   /**
    * Set player selection metadata
    * @param player the player
    * @param selectionManager the selection manager
    */
   public static void setPlayerSelectionMetadata(Player player, SelectionManager selectionManager) {
      player.setMetadata(SELECTION_METADATA, new FixedMetadataValue(Main.getInstance(), selectionManager));
   }
   
   /**
    * Get player selection metadata
    * @param player the player
    * @return selection manager
    */
   public static SelectionManager getPlayerSelectionMetadata(Player player) {
      List<MetadataValue> values = player.getMetadata(SELECTION_METADATA);
      if (!values.isEmpty()) {
         return (SelectionManager) values.get(0).value();
      }
      return null;
   }
   
   /**
    * Remove player selection metadata
    * @param player the player
    */
   public static void removePlayerSelectionMetadata(Player player) {
      player.removeMetadata(SELECTION_METADATA, Main.getInstance());
   }
   
   @Override
   public String toString() {
      return String.format("SelectionManager{first=%s, second=%s, complete=%s}", 
         firstPosition, secondPosition, isSelectionComplete());
   }
}
