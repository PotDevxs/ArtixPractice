package dev.artixdev.practice.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Player Listener
 * Handles player-related events
 */
public class PlayerListener extends BaseListener {
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public PlayerListener(Main plugin) {
      super(plugin);
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerJoin(PlayerJoinEvent event) {
      // Load player profile
      plugin.getPlayerManager().loadPlayer(event.getPlayer().getUniqueId());
      
      // Send welcome message
      event.getPlayer().sendMessage(ChatUtils.colorize("&6&lWelcome to Bolt Practice!"));
      event.getPlayer().sendMessage(ChatUtils.colorize("&7Use &f/practice &7to get started!"));
      
      // Teleport to spawn
      plugin.getPlayerManager().teleportToSpawn(event.getPlayer());
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerQuit(PlayerQuitEvent event) {
      // Save player profile
      plugin.getPlayerManager().savePlayer(event.getPlayer().getUniqueId());
      
      // Remove from any active matches
      plugin.getMatchManager().handlePlayerDisconnect(event.getPlayer());
      
      // Remove from queues
      plugin.getQueueManager().removePlayerFromQueue(event.getPlayer());
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      // Teleport to spawn on respawn
      event.setRespawnLocation(plugin.getPlayerManager().getSpawnLocation());
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerTeleport(PlayerTeleportEvent event) {
      // Prevent teleportation during matches
      if (plugin.getMatchManager().isPlayerInMatch(event.getPlayer())) {
         event.setCancelled(true);
         event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot teleport during a match!"));
      }
   }
}