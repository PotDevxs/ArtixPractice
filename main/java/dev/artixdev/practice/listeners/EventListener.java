package dev.artixdev.practice.listeners;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.events.PlayerDataLoadEvent;
import dev.artixdev.practice.events.PlayerDataSaveEvent;

/**
 * Event Listener
 * Handles custom events and player events
 */
public class EventListener implements Listener {
   
   private final Main plugin;
   
   /**
    * Constructor
    */
   public EventListener() {
      this.plugin = Main.getInstance();
   }
   
   /**
    * Handle player data load event
    * @param event the event
    */
   @EventHandler
   public void onPlayerDataLoad(PlayerDataLoadEvent event) {
      // Handle player data load
      String name = event.getPlayer().map(Player::getName).orElse("unknown");
      plugin.getLogger().info("Player data loaded: " + name);
   }
   
   /**
    * Handle player data save event
    * @param event the event
    */
   @EventHandler
   public void onPlayerDataSave(PlayerDataSaveEvent event) {
      // Handle player data save
      String name = event.getPlayer().map(Player::getName).orElse("unknown");
      plugin.getLogger().info("Player data saved: " + name);
   }
   
   /**
    * Handle player quit event
    * @param event the event
    */
   @EventHandler(priority = EventPriority.MONITOR)
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      UUID playerUUID = player.getUniqueId();
      
      // Get player manager
      dev.artixdev.practice.managers.PlayerManager playerManager = plugin.getPlayerManager();
      
      if (playerManager != null) {
         // Remove player from cooldowns
         playerManager.removePlayerProfile(playerUUID);
         
         // Log player quit
         plugin.getLogger().info("Player quit: " + player.getName() + " (" + playerUUID.toString().substring(0, 8) + ")");
      }
   }
   
   /**
    * Handle custom event
    * @param event the event
    */
   @EventHandler
   public void onCustomEvent(dev.artixdev.practice.events.CustomEvent event) {
      // Handle custom event
      plugin.getLogger().info("Custom event fired: " + event.getEventName());
   }
   
   /**
    * Handle match event
    * @param event the event
    */
   @EventHandler
   public void onMatchEvent(dev.artixdev.practice.events.MatchEvent event) {
      // Handle match event
      plugin.getLogger().info("Match event fired: " + event.getMatchId());
   }
   
   /**
    * Handle arena event
    * @param event the event
    */
   @EventHandler
   public void onArenaEvent(dev.artixdev.practice.events.ArenaEvent event) {
      // Handle arena event
      plugin.getLogger().info("Arena event fired: " + event.getArenaName());
   }
   
   /**
    * Handle queue event
    * @param event the event
    */
   @EventHandler
   public void onQueueEvent(dev.artixdev.practice.events.QueueEvent event) {
      // Handle queue event
      plugin.getLogger().info("Queue event fired: " + event.getQueueName());
   }
   
   /**
    * Handle kit event
    * @param event the event
    */
   @EventHandler
   public void onKitEvent(dev.artixdev.practice.events.KitEvent event) {
      // Handle kit event
      plugin.getLogger().info("Kit event fired: " + event.getKitName());
   }
   
   /**
    * Handle bot event
    * @param event the event
    */
   @EventHandler
   public void onBotEvent(dev.artixdev.practice.events.BotEvent event) {
      // Handle bot event
      plugin.getLogger().info("Bot event fired: " + event.getBotName());
   }
   
   /**
    * Handle statistics event
    * @param event the event
    */
   @EventHandler
   public void onStatisticsEvent(dev.artixdev.practice.events.StatisticsEvent event) {
      // Handle statistics event
      plugin.getLogger().info("Statistics event fired: " + event.getPlayerName());
   }
   
   /**
    * Handle leaderboard event
    * @param event the event
    */
   @EventHandler
   public void onLeaderboardEvent(dev.artixdev.practice.events.LeaderboardEvent event) {
      // Handle leaderboard event
      plugin.getLogger().info("Leaderboard event fired: " + event.getLeaderboardType());
   }
   
   /**
    * Handle tournament event
    * @param event the event
    */
   @EventHandler
   public void onTournamentEvent(dev.artixdev.practice.events.TournamentEvent event) {
      // Handle tournament event
      plugin.getLogger().info("Tournament event fired: " + event.getTournamentName());
   }
}
