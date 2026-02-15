package dev.artixdev.practice.interfaces;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.util.Skin;

/**
 * Skin Interface
 * Interface for skin-related operations
 */
public interface SkinInterface {
   
   /**
    * Check if player has custom skin
    * @param player the player
    * @return true if has custom skin
    */
   boolean hasCustomSkin(Player player);
   
   /**
    * Get player skin
    * @param player the player
    * @return player skin
    */
   Skin getPlayerSkin(Player player);
   
   /**
    * Set player skin
    * @param player the player
    * @param skin the skin
    */
   void setPlayerSkin(Player player, Skin skin);
   
   /**
    * Reset player skin
    * @param player the player
    */
   void resetPlayerSkin(Player player);
   
   /**
    * Get default skin
    * @return default skin
    */
   Skin getDefaultSkin();
   
   /**
    * Check if skin is valid
    * @param skin the skin
    * @return true if valid
    */
   boolean isValidSkin(Skin skin);
   
   /**
    * Get skin by name
    * @param skinName the skin name
    * @return skin
    */
   Skin getSkinByName(String skinName);
   
   /**
    * Get available skins
    * @return list of available skins
    */
   java.util.List<Skin> getAvailableSkins();
   
   /**
    * Check if skin is available
    * @param skinName the skin name
    * @return true if available
    */
   boolean isSkinAvailable(String skinName);
   
   /**
    * Load skin from URL
    * @param skinUrl the skin URL
    * @return skin
    */
   Skin loadSkinFromUrl(String skinUrl);
   
   /**
    * Save skin to file
    * @param skin the skin
    * @param fileName the file name
    * @return true if saved
    */
   boolean saveSkinToFile(Skin skin, String fileName);
   
   /**
    * Load skin from file
    * @param fileName the file name
    * @return skin
    */
   Skin loadSkinFromFile(String fileName);
   
   /**
    * Get skin cache size
    * @return cache size
    */
   int getSkinCacheSize();
   
   /**
    * Clear skin cache
    */
   void clearSkinCache();
   
   /**
    * Reload skins
    */
   void reloadSkins();
}
