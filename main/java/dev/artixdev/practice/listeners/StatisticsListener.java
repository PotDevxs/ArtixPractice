package dev.artixdev.practice.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import java.util.UUID;
import dev.artixdev.practice.events.PlayerMatchEvent;
import dev.artixdev.practice.events.MatchEndEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.KitStatistics;

/**
 * Statistics Listener
 * Handles statistics-related events
 */
public class StatisticsListener implements Listener {
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public StatisticsListener(Main plugin) {
      this.plugin = plugin;
   }
   
   /**
    * Handle player match event
    * @param event the player match event
    */
   @EventHandler
   public void onPlayerMatch(PlayerMatchEvent event) {
      try {
         // Update player statistics
         updatePlayerStatistics(event);
         
         // Update kit statistics
         updateKitStatistics(event);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to handle player match event: " + e.getMessage());
      }
   }
   
   /**
    * Handle match end event
    * @param event the match end event
    */
   @EventHandler
   public void onMatchEnd(MatchEndEvent event) {
      try {
         // Update final statistics
         updateFinalStatistics(event);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to handle match end event: " + e.getMessage());
      }
   }
   
   /**
    * Update player statistics
    * @param event the player match event
    */
   private void updatePlayerStatistics(PlayerMatchEvent event) {
      try {
      UUID playerId = event.getPlayerId().orElse(null);
      if (playerId == null) {
         return;
      }
      PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(playerId);
         if (profile == null) {
            return;
         }
         
         // Update based on match result
         if (event.isWinner()) {
            profile.addWin();
            profile.setWinStreak(profile.getWinStreak() + 1);
         } else {
            profile.addLoss();
            profile.setWinStreak(0);
         }
         
         // Update kills and deaths
         profile.addKill();
         if (event.getDeaths() > 0) {
            for (int i = 0; i < event.getDeaths(); i++) {
               profile.addDeath();
            }
         }
         
         // Save profile
         plugin.getPlayerManager().savePlayerProfile(profile);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update player statistics: " + e.getMessage());
      }
   }
   
   /**
    * Update kit statistics
    * @param event the player match event
    */
   private void updateKitStatistics(PlayerMatchEvent event) {
      try {
         // Get kit statistics for the player
         UUID playerId = event.getPlayerId().orElse(null);
         if (playerId == null) {
            return;
         }
         KitStatistics kitStats = getKitStatistics(playerId, event.getKitType());
         if (kitStats == null) {
            return;
         }
         
         // Update based on match result
         if (event.isWinner()) {
            kitStats.addWin();
            kitStats.setWinstreak(kitStats.getWinstreak() + 1);
            
            if (event.isRanked()) {
               kitStats.addRankedWin();
               kitStats.setRankedWinstreak(kitStats.getRankedWinstreak() + 1);
            }
         } else {
            kitStats.addLoss();
            kitStats.setWinstreak(0);
            
            if (event.isRanked()) {
               kitStats.addRankedLoss();
               kitStats.setRankedWinstreak(0);
            }
         }
         
         // Update kills and deaths
         kitStats.addKill();
         if (event.getDeaths() > 0) {
            for (int i = 0; i < event.getDeaths(); i++) {
               kitStats.addDeath();
            }
         }
         
         // Save kit statistics
         saveKitStatistics(kitStats);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update kit statistics: " + e.getMessage());
      }
   }
   
   /**
    * Update final statistics
    * @param event the match end event
    */
   private void updateFinalStatistics(dev.artixdev.practice.events.MatchEndEvent event) {
      try {
         // Update ELO for all players
         for (java.util.Map.Entry<java.util.UUID, dev.artixdev.practice.models.MatchResult> entry : event.getPlayerResults().entrySet()) {
            updatePlayerElo(entry.getKey(), entry.getValue());
         }
         
         // Update leaderboards
         // plugin.getLeaderboardManager().updateLeaderboards(); // Uncomment when method exists
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update final statistics: " + e.getMessage());
      }
   }
   
   /**
    * Update player ELO
    * @param playerId the player ID
    * @param result the match result
    */
   private void updatePlayerElo(java.util.UUID playerId, dev.artixdev.practice.models.MatchResult result) {
      try {
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(playerId);
         if (profile == null) {
            return;
         }
         
         // Calculate ELO change
         int eloChange = calculateEloChange(profile.getElo(), result.getOpponentElo(), result.isWinner());
         
         // Update ELO
         profile.setElo(profile.getElo() + eloChange);
         
         // Save profile
         plugin.getPlayerManager().savePlayerProfile(profile);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update player ELO: " + e.getMessage());
      }
   }
   
   /**
    * Calculate ELO change
    * @param playerElo the player's ELO
    * @param opponentElo the opponent's ELO
    * @param isWinner true if player won
    * @return ELO change
    */
   private int calculateEloChange(int playerElo, int opponentElo, boolean isWinner) {
      // Simple ELO calculation
      double expectedScore = 1.0 / (1.0 + Math.pow(10, (opponentElo - playerElo) / 400.0));
      double actualScore = isWinner ? 1.0 : 0.0;
      int kFactor = 32; // K-factor for ELO calculation
      
      return (int) Math.round(kFactor * (actualScore - expectedScore));
   }
   
   /**
    * Get kit statistics
    * @param playerId the player ID
    * @param kitType the kit type
    * @return kit statistics
    */
   private KitStatistics getKitStatistics(java.util.UUID playerId, dev.artixdev.practice.enums.KitType kitType) {
      try {
         // This would be implemented based on your kit statistics storage
         return new KitStatistics();
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to get kit statistics: " + e.getMessage());
         return null;
      }
   }
   
   /**
    * Save kit statistics
    * @param kitStats the kit statistics
    */
   private void saveKitStatistics(KitStatistics kitStats) {
      try {
         // This would be implemented based on your kit statistics storage
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to save kit statistics: " + e.getMessage());
      }
   }
}
