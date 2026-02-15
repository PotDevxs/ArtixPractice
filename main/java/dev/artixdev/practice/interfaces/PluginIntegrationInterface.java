package dev.artixdev.practice.interfaces;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.util.Skin;

/**
 * Plugin Integration Interface
 * Interface for plugin integration operations
 */
public interface PluginIntegrationInterface {
   
   /**
    * Check if player1 can see player2
    * @param player1 the first player
    * @param player2 the second player
    * @return true if player1 can see player2
    */
   boolean canSeePlayer(Player player1, Player player2);
   
   /**
    * Check if player is frozen
    * @param player the player
    * @return true if frozen
    */
   boolean isPlayerFrozen(Player player);
   
   /**
    * Check if player is vanished
    * @param player the player
    * @return true if vanished
    */
   boolean isPlayerVanished(Player player);
   
   /**
    * Get player original name
    * @param player the player
    * @return original name
    */
   String getPlayerOriginalName(Player player);
   
   /**
    * Check if player is in staff mode
    * @param player the player
    * @return true if in staff mode
    */
   boolean isPlayerInStaffMode(Player player);
   
   /**
    * Check if plugin is enabled
    * @return true if enabled
    */
   boolean isPluginEnabled();
   
   /**
    * Get player disguise name
    * @param player the player
    * @return disguise name
    */
   String getPlayerDisguiseName(Player player);
   
   /**
    * Get player skin
    * @param player the player
    * @return player skin
    */
   Skin getPlayerSkin(Player player);
}
