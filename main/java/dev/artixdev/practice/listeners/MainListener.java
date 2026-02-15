package dev.artixdev.practice.listeners;

import java.util.Map;
import java.util.UUID;
import me.drizzy.api.nms.INMSImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.PluginManager;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Main Listener
 * Main event listener for the practice plugin
 */
public final class MainListener implements Listener {
   
   private static final Logger logger = LogManager.getLogger(MainListener.class);
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public MainListener(Main plugin) {
      this.plugin = plugin;
   }
   
   @EventHandler(priority = EventPriority.MONITOR)
   public void onJoin(PlayerJoinEvent event) {
      event.setJoinMessage(null);
      Player player = event.getPlayer();
      
      // Load player profile
      plugin.getPlayerManager().loadPlayer(player.getUniqueId());
      
      // Send welcome message
      player.sendMessage(ChatUtils.colorize("&6&lWelcome to Bolt Practice!"));
      player.sendMessage(ChatUtils.colorize("&7Use &f/practice &7to get started!"));
      
      // Teleport to spawn
      plugin.getPlayerManager().teleportToSpawn(player);
      
      logger.info("Player joined: " + player.getName());
   }
   
   @EventHandler(priority = EventPriority.MONITOR)
   public void onQuit(PlayerQuitEvent event) {
      event.setQuitMessage(null);
      Player player = event.getPlayer();
      
      // Save player profile
      plugin.getPlayerManager().savePlayer(player.getUniqueId());
      
      // Remove from any active matches
      plugin.getMatchManager().handlePlayerDisconnect(player);
      
      // Remove from queues
      plugin.getQueueManager().removePlayerFromQueue(player);
      
      logger.info("Player left: " + player.getName());
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockBreak(BlockBreakEvent event) {
      Player player = event.getPlayer();
      
      // Prevent block breaking in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlock().getLocation())) {
         event.setCancelled(true);
         player.sendMessage(ChatUtils.colorize("&cYou cannot break blocks in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      
      // Prevent block placing in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlock().getLocation())) {
         event.setCancelled(true);
         player.sendMessage(ChatUtils.colorize("&cYou cannot place blocks in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onEntityDamage(EntityDamageEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player) event.getEntity();
         
         // Prevent damage in certain areas
         if (plugin.getArenaManager().isInArena(player.getLocation())) {
            // Allow damage in arenas during matches
            if (!plugin.getMatchManager().isPlayerInMatch(player)) {
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onFoodLevelChange(FoodLevelChangeEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player) event.getEntity();
         
         // Prevent hunger in practice areas
         if (plugin.getArenaManager().isInArena(player.getLocation())) {
            event.setCancelled(true);
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerDeath(PlayerDeathEvent event) {
      Player player = event.getEntity();
      
      // Handle player death in matches
      if (plugin.getMatchManager().isPlayerInMatch(player)) {
         plugin.getMatchManager().handlePlayerDeath(player);
         event.setKeepInventory(true);
         event.getDrops().clear();
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onProjectileLaunch(ProjectileLaunchEvent event) {
      // Handle projectile launching
      if (event.getEntity().getShooter() instanceof Player) {
         Player shooter = (Player) event.getEntity().getShooter();
         
         // Check if player can launch projectiles
         if (!plugin.getArenaManager().isInArena(shooter.getLocation())) {
            event.setCancelled(true);
            shooter.sendMessage(ChatUtils.colorize("&cYou can only use projectiles in arenas!"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
      Player player = event.getPlayer();
      
      // Prevent bucket usage in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlockClicked().getLocation())) {
         event.setCancelled(true);
         player.sendMessage(ChatUtils.colorize("&cYou cannot use buckets in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
      Player player = event.getPlayer();
      
      // Handle world change
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         // Player entered arena world
         player.sendMessage(ChatUtils.colorize("&aYou entered an arena world!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      Player player = event.getPlayer();
      String command = event.getMessage().toLowerCase();
      
      // Block certain commands in practice areas
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         if (command.startsWith("/spawn") || 
             command.startsWith("/home") || 
             command.startsWith("/tpa") ||
             command.startsWith("/warp")) {
            event.setCancelled(true);
            player.sendMessage(ChatUtils.colorize("&cYou cannot use this command in arenas!"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerDropItem(PlayerDropItemEvent event) {
      Player player = event.getPlayer();
      
      // Prevent item dropping in practice areas
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         event.setCancelled(true);
         player.sendMessage(ChatUtils.colorize("&cYou cannot drop items in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      Player player = event.getPlayer();
      
      // Prevent certain entity interactions
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         // Allow interactions in matches
         if (!plugin.getMatchManager().isPlayerInMatch(player)) {
            event.setCancelled(true);
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerInteract(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      
      // Prevent certain block interactions
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         // Allow interactions in matches
         if (!plugin.getMatchManager().isPlayerInMatch(player)) {
            if (event.getAction().toString().contains("RIGHT_CLICK")) {
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerItemDamage(PlayerItemDamageEvent event) {
      Player player = event.getPlayer();
      
      // Prevent item damage in practice areas
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerPickupItem(PlayerPickupItemEvent event) {
      Player player = event.getPlayer();
      
      // Prevent item pickup in practice areas
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerTeleport(PlayerTeleportEvent event) {
      Player player = event.getPlayer();
      
      // Prevent teleportation during matches
      if (plugin.getMatchManager().isPlayerInMatch(player)) {
         if (event.getCause() != TeleportCause.UNKNOWN) {
            event.setCancelled(true);
            player.sendMessage(ChatUtils.colorize("&cYou cannot teleport during a match!"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
      // Handle pre-login events
      if (event.getLoginResult() != Result.ALLOWED) {
         return;
      }
      
      // Check if player is banned or has restrictions
      // TODO: Implement ban checking
      
      logger.info("Player pre-login: " + event.getName());
   }
   
   /**
    * Initialize listener
    */
   public void initialize() {
      PluginManager pluginManager = Bukkit.getPluginManager();
      pluginManager.registerEvents(this, plugin);
      
      logger.info("MainListener initialized");
   }
   
   /**
    * Shutdown listener
    */
   public void shutdown() {
      // Cleanup any resources
      logger.info("MainListener shutdown");
   }
}
