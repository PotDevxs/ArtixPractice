package dev.artixdev.practice.listeners;

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

import java.util.UUID;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.PluginManager;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.Messages;

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

      // Load player profile (cache + storage/FLATFILE)
      dev.artixdev.practice.models.PlayerProfile profile = plugin.getPlayerManager().loadPlayer(player.getUniqueId());

      // Welcome message (fallback if messages missing)
      String w1 = Messages.get("GENERAL.WELCOME_LINE1");
      String w2 = Messages.get("GENERAL.WELCOME_LINE2");
      if (w1 != null && !w1.isEmpty()) player.sendMessage(w1);
      if (w2 != null && !w2.isEmpty()) player.sendMessage(w2);

      // Teleport to spawn
      plugin.getPlayerManager().teleportToSpawn(player);

      // Scoreboard and hotbar for lobby
      if (plugin.getScoreboardManager() != null) {
         plugin.getScoreboardManager().setScoreboard(player, "lobby");
      }
      if (plugin.getHotbarManager() != null) {
         plugin.getHotbarManager().setHotbarLayout(player, "LOBBY");
      }

      // Fire custom PlayerJoinEvent so ScoreboardListener / NameTag etc. can run
      if (profile != null) {
         dev.artixdev.practice.events.PlayerJoinEvent customJoin = new dev.artixdev.practice.events.PlayerJoinEvent(profile);
         Bukkit.getPluginManager().callEvent(customJoin);
      }

      // Apply visibility preference: joining player hides others who have visibility disabled
      for (Player other : Bukkit.getOnlinePlayers()) {
         if (other.equals(player)) continue;
         dev.artixdev.practice.models.PlayerProfile op = plugin.getPlayerManager().getPlayerProfile(other.getUniqueId());
         if (op != null && !op.isVisibilityEnabled()) {
            player.hidePlayer(other);
         }
      }

      // Daily reward and achievements (delayed)
      Bukkit.getScheduler().runTaskLater(plugin.getPlugin(), () -> {
         dev.artixdev.practice.models.PlayerProfile p = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (p != null) {
            if (plugin.getDailyRewardManager() != null) {
               plugin.getDailyRewardManager().onPlayerJoin(player, p);
            }
            if (plugin.getAchievementsManager() != null) {
               java.util.Set<String> newly = plugin.getAchievementsManager().checkAndUnlockAll(p);
               for (String id : newly) {
                  dev.artixdev.practice.enums.AchievementType type = dev.artixdev.practice.enums.AchievementType.fromId(id);
                  if (type != null) plugin.getAchievementsManager().notifyUnlock(player, type);
               }
            }
         }
      }, 40L);

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
         player.sendMessage(Messages.get("ARENA.CANNOT_BREAK"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      
      // Prevent block placing in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlock().getLocation())) {
         event.setCancelled(true);
         player.sendMessage(Messages.get("ARENA.CANNOT_PLACE"));
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
            shooter.sendMessage(Messages.get("ARENA.CANNOT_USE_PROJECTILES"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
      Player player = event.getPlayer();
      
      // Prevent bucket usage in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlockClicked().getLocation())) {
         event.setCancelled(true);
         player.sendMessage(Messages.get("ARENA.CANNOT_USE_BUCKETS"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
      Player player = event.getPlayer();
      
      // Handle world change
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         // Player entered arena world
         player.sendMessage(Messages.get("ARENA.ENTERED_ARENA_WORLD"));
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
            player.sendMessage(Messages.get("ARENA.CANNOT_USE_COMMAND"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onPlayerDropItem(PlayerDropItemEvent event) {
      Player player = event.getPlayer();
      
      // Prevent item dropping in practice areas
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         event.setCancelled(true);
         player.sendMessage(Messages.get("ARENA.CANNOT_DROP_ITEMS"));
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
            player.sendMessage(Messages.get("ARENA.CANNOT_TELEPORT_DURING_MATCH"));
         }
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
      // Handle pre-login events
      if (event.getLoginResult() != Result.ALLOWED) {
         return;
      }
      
      if (!isAllowedToJoin(event.getUniqueId(), event.getName())) {
         event.disallow(Result.KICK_BANNED, org.bukkit.ChatColor.RED + "You are not allowed to join this server.");
         return;
      }
      logger.info("Player pre-login: " + event.getName());
   }

   /** Returns false to disallow (e.g. banned). Plug in BanManager or storage check here. */
   private boolean isAllowedToJoin(UUID uniqueId, String name) {
      return true;
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
