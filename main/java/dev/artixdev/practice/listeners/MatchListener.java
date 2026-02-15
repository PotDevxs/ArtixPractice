package dev.artixdev.practice.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import dev.artixdev.practice.events.MatchStartEvent;
import dev.artixdev.practice.events.MatchEndEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.MatchResult;
import dev.artixdev.practice.utils.NotificationManager;
import dev.artixdev.practice.managers.LeaderboardManager;

/**
 * Match Listener
 * Handles match-related events
 */
public class MatchListener implements Listener {
   
   private final Main plugin;
   private final NotificationManager notificationManager;
   private final LeaderboardManager leaderboardManager;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public MatchListener(Main plugin) {
      this.plugin = plugin;
      this.notificationManager = new NotificationManager(plugin);
      this.leaderboardManager = new LeaderboardManager(plugin);
   }
   
   /**
    * Handle match start event
    * @param event the match start event
    */
   @EventHandler
   public void onMatchStart(MatchStartEvent event) {
      try {
         // Get player
         Player player = event.getPlayer();
         if (player == null) {
            return;
         }
         
         // Update player state
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile != null) {
            profile.setState(dev.artixdev.practice.enums.PlayerState.FIGHTING);
         }
         
         // Send match start notification
         notificationManager.sendMessage(player, "Match started! Good luck!");
         
         // Update scoreboard and nametags
         updatePlayerDisplay(player);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to handle match start event: " + e.getMessage());
      }
   }
   
   /**
    * Handle match end event
    * @param event the match end event
    */
   @EventHandler
   public void onMatchEnd(MatchEndEvent event) {
      try {
         // Get winner and loser
         Player winner = event.getWinner();
         Player loser = event.getLoser();
         
         if (winner != null) {
            handleMatchEndForPlayer(winner, true, event);
         }
         
         if (loser != null) {
            handleMatchEndForPlayer(loser, false, event);
         }
         
         // Update all leaderboards asynchronously
         leaderboardManager.updateAllLeaderboards();
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to handle match end event: " + e.getMessage());
      }
   }
   
   /**
    * Handle match end for a specific player
    * @param player the player
    * @param isWinner true if player won
    * @param event the match end event
    */
   private void handleMatchEndForPlayer(Player player, boolean isWinner, MatchEndEvent event) {
      try {
         // Get player profile
         PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile == null) {
            return;
         }
         
         // Update player state
         profile.setState(dev.artixdev.practice.enums.PlayerState.LOBBY);
         
         // Update statistics
         if (isWinner) {
            profile.addWin();
            profile.setWinStreak(profile.getWinStreak() + 1);
            notificationManager.sendSuccess(player, "You won the match!");
         } else {
            profile.addLoss();
            profile.setWinStreak(0);
            notificationManager.sendMessage(player, "You lost the match. Better luck next time!");
         }
         
         // Update ELO if ranked
         if (event.isRanked()) {
            updatePlayerElo(player, profile, event);
         }
         
         // Save profile
         plugin.getPlayerManager().savePlayerProfile(profile);
         
         // Update display
         updatePlayerDisplay(player);
         
         // Teleport to spawn
         plugin.getPlayerManager().teleportToSpawn(player);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to handle match end for player: " + e.getMessage());
      }
   }
   
   /**
    * Update player ELO
    * @param player the Bukkit player (online)
    * @param profile the player profile
    * @param event the match end event
    */
   private void updatePlayerElo(Player player, PlayerProfile profile, MatchEndEvent event) {
      try {
         // Look up this player's result in the MatchEndEvent
         MatchResult result = event.getPlayerResults().get(profile.getUniqueId());
         if (result == null) {
            return;
         }

         // Calculate ELO change based on result
         int eloChange = calculateEloChange(profile.getElo(), result.getOpponentElo(), result.isWinner());
         
         // Update ELO
         profile.setElo(profile.getElo() + eloChange);
         
         // Send ELO change notification
         if (eloChange > 0) {
            notificationManager.sendSuccess(player, 
               String.format("ELO increased by %d! New ELO: %d", eloChange, profile.getElo()));
         } else if (eloChange < 0) {
            notificationManager.sendWarning(player, 
               String.format("ELO decreased by %d. New ELO: %d", Math.abs(eloChange), profile.getElo()));
         }
         
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
    * Update player display
    * @param player the player
    */
   private void updatePlayerDisplay(Player player) {
      try {
         // Update scoreboard
         plugin.getScoreboardManager().updateScoreboard(player);
         
         // Update tablist (which internally updates name tags / display as needed)
         plugin.getTablistManager().updateTablist(player);
         
      } catch (Exception e) {
         plugin.getLogger().warning("Failed to update player display: " + e.getMessage());
      }
   }
}
