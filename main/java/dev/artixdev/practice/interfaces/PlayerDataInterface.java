package dev.artixdev.practice.interfaces;

import java.util.UUID;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.util.Skin;

/**
 * Player Data Interface
 * Interface for player data operations
 */
public interface PlayerDataInterface {
   
   /**
    * Check if player is online
    * @param player the player
    * @return true if online
    */
   boolean isPlayerOnline(Player player);
   
   /**
    * Check if player is in lobby
    * @param player the player
    * @return true if in lobby
    */
   boolean isPlayerInLobby(Player player);
   
   /**
    * Handle player data update
    * @param player the player
    * @param uuid the player UUID
    */
   default void handlePlayerDataUpdate(Player player, UUID uuid) {
      // Default implementation - can be overridden
   }
   
   /**
    * Get player display name
    * @param player the player
    * @return display name
    */
   String getPlayerDisplayName(Player player);
   
   /**
    * Check if player has permission
    * @param player the player
    * @return true if has permission
    */
   boolean hasPlayerPermission(Player player);
   
   /**
    * Check if players can interact
    * @param player1 the first player
    * @param player2 the second player
    * @return true if can interact
    */
   boolean canPlayersInteract(Player player1, Player player2);
   
   /**
    * Get player prefix
    * @param player the player
    * @return player prefix
    */
   String getPlayerPrefix(Player player);
   
   /**
    * Handle player join
    * @param player the player
    */
   default void handlePlayerJoin(Player player) {
      // Default implementation - can be overridden
   }
   
   /**
    * Get player skin
    * @param player the player
    * @return player skin
    */
   Skin getPlayerSkin(Player player);
   
   /**
    * Check if UUID is valid
    * @param uuid the UUID
    * @return true if valid
    */
   default boolean isValidUUID(UUID uuid) {
      return uuid != null;
   }
   
   /**
    * Check if player is in match
    * @param player the player
    * @return true if in match
    */
   boolean isPlayerInMatch(Player player);
   
   /**
    * Check if interface is enabled
    * @return true if enabled
    */
   boolean isInterfaceEnabled();
}
