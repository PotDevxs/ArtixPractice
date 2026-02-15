package dev.artixdev.practice.managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;

/**
 * Player Relation Manager
 * Manages relationships between players (friends, enemies, etc.)
 */
public class PlayerRelationManager {
   
   private final Map<UUID, UUID> playerRelations;
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public PlayerRelationManager(Main plugin) {
      this.plugin = plugin;
      this.playerRelations = new HashMap<>();
   }
   
   /**
    * Get player relation
    * @param player the player
    * @return optional relation UUID
    */
   public Optional<UUID> getPlayerRelation(Player player) {
      try {
         if (!this.playerRelations.containsValue(player.getUniqueId())) {
            return Optional.empty();
         }
         
         // Find the relation
         for (Entry<UUID, UUID> entry : this.playerRelations.entrySet()) {
            if (entry.getValue().equals(player.getUniqueId())) {
               return Optional.of(entry.getKey());
            }
         }
         
         return Optional.empty();
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get player relation for " + player.getName() + ": " + e.getMessage());
         return Optional.empty();
      }
   }
   
   /**
    * Set player relation
    * @param player1 the first player
    * @param player2 the second player
    */
   public void setPlayerRelation(Player player1, Player player2) {
      try {
         this.playerRelations.put(player1.getUniqueId(), player2.getUniqueId());
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to set player relation between " + player1.getName() + " and " + player2.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Remove player relation
    * @param player the player
    */
   public void removePlayerRelation(Player player) {
      try {
         this.playerRelations.remove(player.getUniqueId());
         
         // Remove from values as well
         this.playerRelations.entrySet().removeIf(entry -> entry.getValue().equals(player.getUniqueId()));
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove player relation for " + player.getName() + ": " + e.getMessage());
      }
   }
   
   /**
    * Check if players are related
    * @param player1 the first player
    * @param player2 the second player
    * @return true if related
    */
   public boolean arePlayersRelated(Player player1, Player player2) {
      try {
         UUID player1Id = player1.getUniqueId();
         UUID player2Id = player2.getUniqueId();
         
         // Check direct relation
         if (this.playerRelations.containsKey(player1Id) && this.playerRelations.get(player1Id).equals(player2Id)) {
            return true;
         }
         
         // Check reverse relation
         if (this.playerRelations.containsKey(player2Id) && this.playerRelations.get(player2Id).equals(player1Id)) {
            return true;
         }
         
         return false;
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to check if players are related: " + e.getMessage());
         return false;
      }
   }
   
   /**
    * Get all relations
    * @return map of relations
    */
   public Map<UUID, UUID> getAllRelations() {
      return new HashMap<>(this.playerRelations);
   }
   
   /**
    * Get relation count
    * @return relation count
    */
   public int getRelationCount() {
      return this.playerRelations.size();
   }
   
   /**
    * Clear all relations
    */
   public void clearAllRelations() {
      this.playerRelations.clear();
   }
   
   /**
    * Get relations for player
    * @param player the player
    * @return set of related player UUIDs
    */
   public Set<UUID> getRelationsForPlayer(Player player) {
      Set<UUID> relations = new java.util.HashSet<>();
      
      try {
         UUID playerId = player.getUniqueId();
         
         // Find all relations where this player is the key
         for (Entry<UUID, UUID> entry : this.playerRelations.entrySet()) {
            if (entry.getKey().equals(playerId)) {
               relations.add(entry.getValue());
            }
            if (entry.getValue().equals(playerId)) {
               relations.add(entry.getKey());
            }
         }
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get relations for player " + player.getName() + ": " + e.getMessage());
      }
      
      return relations;
   }
   
   /**
    * Get relation statistics
    * @return relation statistics
    */
   public String getRelationStatistics() {
      return String.format("PlayerRelationManager: %d relations, %d unique players", 
         getRelationCount(), 
         this.playerRelations.keySet().size() + this.playerRelations.values().size());
   }
   
   /**
    * Check if player has relations
    * @param player the player
    * @return true if has relations
    */
   public boolean hasRelations(Player player) {
      return !getRelationsForPlayer(player).isEmpty();
   }
   
   /**
    * Get relation count for player
    * @param player the player
    * @return relation count
    */
   public int getRelationCountForPlayer(Player player) {
      return getRelationsForPlayer(player).size();
   }
   
   /**
    * Remove relation between two players
    * @param player1 the first player
    * @param player2 the second player
    */
   public void removeRelationBetweenPlayers(Player player1, Player player2) {
      try {
         UUID player1Id = player1.getUniqueId();
         UUID player2Id = player2.getUniqueId();
         
         // Remove direct relation
         this.playerRelations.remove(player1Id);
         
         // Remove reverse relation
         this.playerRelations.entrySet().removeIf(entry -> 
            entry.getKey().equals(player2Id) && entry.getValue().equals(player1Id));
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to remove relation between players: " + e.getMessage());
      }
   }
   
   /**
    * Get relation info
    * @param player the player
    * @return relation info
    */
   public String getRelationInfo(Player player) {
      Set<UUID> relations = getRelationsForPlayer(player);
      return String.format("Player %s has %d relations: %s", 
         player.getName(), 
         relations.size(), 
         relations.toString());
   }
   
   @Override
   public String toString() {
      return getRelationStatistics();
   }
}
