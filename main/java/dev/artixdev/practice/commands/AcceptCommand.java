package dev.artixdev.practice.commands;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.NotificationManager;

/**
 * Accept Command
 * Command to accept duel requests
 */
@Register(name = "accept")
public final class AcceptCommand {
   
   private final Main plugin;
   private final NotificationManager notificationManager;
   private final List<UUID> pendingRequests;
   
   /**
    * Constructor
    */
   public AcceptCommand() {
      this.plugin = Main.getInstance();
      this.notificationManager = new NotificationManager(plugin);
      this.pendingRequests = new java.util.ArrayList<>();
   }
   
   /**
    * Accept a duel request
    * @param player the player accepting
    * @param target the target player
    */
   @Command(
      name = "",
      usage = "<target>",
      desc = "Accept a duel request"
   )
   public void acceptDuel(@Sender Player player, Player target) {
      if (target == null) {
         notificationManager.sendError(player, "Target player not found.");
         return;
      }
      
      if (player.equals(target)) {
         notificationManager.sendError(player, "You cannot accept a duel from yourself.");
         return;
      }
      
      if (!canAcceptDuel(player, target)) {
         return;
      }
      
      // Check if both players are online
      if (!player.isOnline() || !target.isOnline()) {
         notificationManager.sendError(player, "One or both players are offline.");
         return;
      }
      
      // Get player profiles
      dev.artixdev.practice.models.PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
      dev.artixdev.practice.models.PlayerProfile targetProfile = plugin.getPlayerManager().getPlayerProfile(target.getUniqueId());
      
      if (playerProfile == null || targetProfile == null) {
         notificationManager.sendError(player, "Player profiles not loaded.");
         return;
      }
      
      // Check if players are in the right state
      if (playerProfile.getState() != dev.artixdev.practice.enums.PlayerState.LOBBY) {
         notificationManager.sendError(player, "You must be in the lobby to accept duels.");
         return;
      }
      
      if (targetProfile.getState() != dev.artixdev.practice.enums.PlayerState.LOBBY) {
         notificationManager.sendError(player, "Target player is not in the lobby.");
         return;
      }
      
      // Check if target has a pending request
      if (!hasPendingRequest(target, player)) {
         notificationManager.sendError(player, "No pending duel request from " + target.getName() + ".");
         return;
      }
      
      // Accept the duel
      acceptDuelRequest(player, target);
   }
   
   /**
    * Check if player can accept duel
    * @param player the player
    * @param target the target
    * @return true if can accept
    */
   private boolean canAcceptDuel(Player player, Player target) {
      // Check if both players are online
      if (!player.isOnline()) {
         notificationManager.sendError(player, "You must be online to accept duels.");
         return false;
      }
      
      if (!target.isOnline()) {
         notificationManager.sendError(player, "Target player is offline.");
         return false;
      }
      
      // Check if target has a pending request
      if (!hasPendingRequest(target, player)) {
         notificationManager.sendError(player, "No pending duel request from " + target.getName() + ".");
         return false;
      }
      
      return true;
   }
   
   /**
    * Check if there's a pending request
    * @param requester the requester
    * @param target the target
    * @return true if has pending request
    */
   private boolean hasPendingRequest(Player requester, Player target) {
      // Check if there's a pending duel request
      // This would typically check a duel manager or request system
      return pendingRequests.contains(requester.getUniqueId());
   }
   
   /**
    * Accept duel request
    * @param player the player accepting
    * @param target the target player
    */
   private void acceptDuelRequest(Player player, Player target) {
      // Remove from pending requests
      pendingRequests.remove(target.getUniqueId());
      
      // Send success messages
      notificationManager.sendSuccess(player, "You accepted the duel request from " + target.getName() + ".");
      notificationManager.sendSuccess(target, player.getName() + " accepted your duel request.");
      
      // Start the duel
      startDuel(player, target);
   }
   
   /**
    * Start duel
    * @param player1 the first player
    * @param player2 the second player
    */
   private void startDuel(Player player1, Player player2) {
      // Start the duel
      // This would typically create a match and teleport players to an arena
      plugin.getLogger().info("Starting duel between " + player1.getName() + " and " + player2.getName());
   }
   
   /**
    * Add pending request
    * @param requester the requester
    * @param target the target
    */
   public void addPendingRequest(Player requester, Player target) {
      pendingRequests.add(requester.getUniqueId());
   }
   
   /**
    * Remove pending request
    * @param requester the requester
    */
   public void removePendingRequest(Player requester) {
      pendingRequests.remove(requester.getUniqueId());
   }
   
   /**
    * Check if has pending request
    * @param player the player
    * @return true if has pending request
    */
   public boolean hasPendingRequest(Player player) {
      return pendingRequests.contains(player.getUniqueId());
   }
   
   /**
    * Get pending requests count
    * @return pending requests count
    */
   public int getPendingRequestsCount() {
      return pendingRequests.size();
   }
}
