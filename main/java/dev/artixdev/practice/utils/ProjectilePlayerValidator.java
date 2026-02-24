package dev.artixdev.practice.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Projectile Player Validator
 * Validates projectile behavior based on player state
 */
public class ProjectilePlayerValidator {
   
   private static final long COOLDOWN_MS = 1100L;
   private final Map<UUID, Long> playerCooldowns = new HashMap<>();
   private final Main plugin;
   private final ProjectileType projectileType;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param projectileType the projectile type
    */
   public ProjectilePlayerValidator(Main plugin, ProjectileType projectileType) {
      this.plugin = plugin;
      this.projectileType = projectileType;
   }
   
   /**
    * Check if projectile is valid
    * @param projectile the projectile
    * @return true if valid
    */
   public boolean isValidProjectile(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check if projectile is dead
      if (projectile.isDead()) {
         return false;
      }
      
      // Check if projectile has valid shooter
      if (!hasValidShooter(projectile)) {
         return false;
      }
      
      // Check if projectile is in practice area
      if (!isInPracticeArea(projectile)) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Check if projectile has valid shooter
    * @param projectile the projectile
    * @return true if has valid shooter
    */
   private boolean hasValidShooter(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      ProjectileSource shooter = projectile.getShooter();
      if (shooter == null) {
         return false;
      }
      
      // Check if shooter is a player
      if (!(shooter instanceof Player)) {
         return false;
      }
      
      Player player = (Player) shooter;
      
      // Check if player is online
      if (!player.isOnline()) {
         return false;
      }
      
      // Check if player is in a valid state
      if (!isPlayerInValidState(player)) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Check if player is in valid state
    * @param player the player
    * @return true if in valid state
    */
   private boolean isPlayerInValidState(Player player) {
      if (player == null) {
         return false;
      }
      
      // Check if player is in a match
      if (plugin.getMatchManager().isPlayerInMatch(player)) {
         return true;
      }
      
      // Check if player is in a queue
      if (plugin.getQueueManager().isPlayerInQueue(player)) {
         return true;
      }
      
      // Check if player is in an arena
      if (plugin.getArenaManager().isInArena(player.getLocation())) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Check if projectile is in practice area
    * @param projectile the projectile
    * @return true if in practice area
    */
   private boolean isInPracticeArea(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      Location location = projectile.getLocation();
      
      // Check if in arena
      if (plugin.getArenaManager().isInArena(location)) {
         return true;
      }
      
      // Check if in match
      if (plugin.getMatchManager().isLocationInMatch(location)) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Get projectile cooldown
    * @return cooldown in milliseconds
    */
   public int getProjectileCooldown() {
      return (int) COOLDOWN_MS;
   }
   
   /**
    * Get projectile item
    * @return projectile item or null
    */
   public ItemStack getProjectileItem() {
      // Return null to indicate no specific item
      return null;
   }
   
   /**
    * Check if player can launch projectile
    * @param player the player
    * @return true if can launch
    */
   public boolean canPlayerLaunchProjectile(Player player) {
      if (player == null) {
         return false;
      }
      
      // Check if player is in valid state
      if (!isPlayerInValidState(player)) {
         return false;
      }
      
      // Check if player has permission
      if (!player.hasPermission("practice.projectile.launch")) {
         return false;
      }
      
      // Check if player is not in cooldown
      if (isPlayerInCooldown(player)) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Check if player is in cooldown
    * @param player the player
    * @return true if in cooldown
    */
   private boolean isPlayerInCooldown(Player player) {
      if (player == null) return false;
      Long last = playerCooldowns.get(player.getUniqueId());
      return last != null && (System.currentTimeMillis() - last) < COOLDOWN_MS;
   }

   /** Call after player launches a projectile to start cooldown. */
   public void setPlayerCooldown(Player player) {
      if (player != null) playerCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
   }
   
   /**
    * Get projectile type
    * @return projectile type
    */
   public ProjectileType getProjectileType() {
      return projectileType;
   }
   
   /**
    * Get projectile damage
    * @return damage amount
    */
   public double getProjectileDamage() {
      if (projectileType == null) {
         return 0.0;
      }
      
      switch (projectileType) {
         case ARROW:
            return 2.0;
         case SPECTRAL_ARROW:
            return 2.0;
         case TIPPED_ARROW:
            return 2.0;
         case SNOWBALL:
            return 0.0;
         case EGG:
            return 0.0;
         case BLAZE_POWDER:
            return 1.0;
         case FIREWORK_ROCKET:
            return 3.0;
         case TRIDENT:
            return 8.0;
         default:
            return 0.0;
      }
   }
   
   /**
    * Get projectile info
    * @return projectile info string
    */
   public String getProjectileInfo() {
      if (projectileType == null) {
         return "Unknown projectile type";
      }
      
      return String.format("Type: %s, Damage: %.1f, Cooldown: %dms", 
         projectileType.getDisplayName(), 
         getProjectileDamage(), 
         getProjectileCooldown());
   }
   
   /**
    * Validate projectile for specific player
    * @param projectile the projectile
    * @param player the player
    * @return true if valid for player
    */
   public boolean validateForPlayer(Projectile projectile, Player player) {
      if (projectile == null || player == null) {
         return false;
      }
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return false;
      }
      
      // Check if player can launch projectile
      if (!canPlayerLaunchProjectile(player)) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Get players in range of projectile
    * @param projectile the projectile
    * @param range the range
    * @return list of players in range
    */
   public List<Player> getPlayersInRange(Projectile projectile, double range) {
      if (projectile == null || range <= 0) {
         return Collections.emptyList();
      }
      
      Location location = projectile.getLocation();
      return location.getWorld().getPlayers().stream()
         .filter(player -> player.getLocation().distance(location) <= range)
         .filter(player -> isPlayerInValidState(player))
         .collect(Collectors.toList());
   }
   
   /**
    * Check if projectile can hit player
    * @param projectile the projectile
    * @param player the player
    * @return true if can hit
    */
   public boolean canHitPlayer(Projectile projectile, Player player) {
      if (projectile == null || player == null) {
         return false;
      }
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return false;
      }
      
      // Check if player is in valid state
      if (!isPlayerInValidState(player)) {
         return false;
      }
      
      // Check if player is not the shooter
      ProjectileSource shooter = projectile.getShooter();
      if (shooter instanceof Player && shooter.equals(player)) {
         return false;
      }
      
      return true;
   }
}
