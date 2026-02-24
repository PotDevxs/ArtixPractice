package dev.artixdev.practice.utils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Projectile Manager
 * Manages all projectile-related functionality
 */
public class ProjectileManager {
   
   private static final double DEFAULT_DAMAGE = 0.0;
   private static final int DEFAULT_COOLDOWN = 0;
   
   private final Main plugin;
   private final ProjectileValidator validator;
   private final ProjectileTracker tracker;
   private final ProjectilePhysics physics;
   private final Map<UUID, Long> playerCooldowns;
   private final Map<UUID, ProjectileType> playerLastType;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileManager(Main plugin) {
      this.plugin = plugin;
      this.validator = new ProjectileValidator(plugin);
      this.tracker = new ProjectileTracker(plugin);
      this.physics = new ProjectilePhysics(plugin);
      this.playerCooldowns = new HashMap<>();
      this.playerLastType = new HashMap<>();
   }
   
   /**
    * Launch projectile
    * @param shooter the shooter
    * @param type the projectile type
    * @param damage the damage amount
    * @param velocity the velocity
    */
   public void launchProjectile(org.bukkit.entity.Player shooter, ProjectileType type, double damage, org.bukkit.util.Vector velocity) {
      if (shooter == null || type == null || velocity == null) {
         return;
      }
      
      // Check cooldown
      if (isInCooldown(shooter)) {
         shooter.sendMessage(dev.artixdev.practice.utils.Messages.get("COMBAT.COOLDOWN"));
         return;
      }
      
      // Create projectile
      Projectile projectile = createProjectile(shooter, type);
      if (projectile == null) {
         return;
      }
      
      // Set velocity
      projectile.setVelocity(velocity);
      
      // Track projectile
      tracker.trackProjectile(projectile, type, damage, shooter.getUniqueId());
      
      // Set cooldown
      setCooldown(shooter, type);
   }
   
   /**
    * Create projectile
    * @param shooter the shooter
    * @param type the projectile type
    * @return projectile or null
    */
   private Projectile createProjectile(org.bukkit.entity.Player shooter, ProjectileType type) {
      if (shooter == null || type == null) {
         return null;
      }
      
      org.bukkit.Location location = shooter.getEyeLocation();
      org.bukkit.util.Vector direction = location.getDirection();
      
      switch (type) {
         case ARROW:
            return shooter.launchProjectile(org.bukkit.entity.Arrow.class, direction);
         case SPECTRAL_ARROW:
            // SpectralArrow exists only on 1.9+, use Arrow on 1.8.8
            return shooter.launchProjectile(org.bukkit.entity.Arrow.class, direction);
         case TIPPED_ARROW:
            return shooter.launchProjectile(org.bukkit.entity.Arrow.class, direction);
         case SNOWBALL:
            return shooter.launchProjectile(org.bukkit.entity.Snowball.class, direction);
         case EGG:
            return shooter.launchProjectile(org.bukkit.entity.Egg.class, direction);
         case FIREWORK_ROCKET:
            // Firework doesn't implement Projectile, spawn it manually
            org.bukkit.entity.Firework firework = (org.bukkit.entity.Firework) location.getWorld()
               .spawnEntity(location, EntityType.FIREWORK);
            if (firework != null) {
               firework.setVelocity(direction);
            }
            // Return null since Firework is not a Projectile
            return null;
         case TRIDENT:
            // Trident exists only on 1.13+, fall back to Arrow on older versions
            return shooter.launchProjectile(org.bukkit.entity.Arrow.class, direction);
         default:
            return null;
      }
   }
   
   /**
    * Check if player is in cooldown
    * @param player the player
    * @return true if in cooldown
    */
   public boolean isInCooldown(org.bukkit.entity.Player player) {
      if (player == null) {
         return false;
      }
      
      UUID playerId = player.getUniqueId();
      Long lastLaunch = playerCooldowns.get(playerId);
      if (lastLaunch == null) {
         return false;
      }
      
      long currentTime = System.currentTimeMillis();
      long cooldown = getProjectileCooldown(player);
      return (currentTime - lastLaunch) < cooldown;
   }
   
   /**
    * Get projectile cooldown for player
    * @param player the player
    * @return cooldown in milliseconds
    */
   private long getProjectileCooldown(org.bukkit.entity.Player player) {
      if (player == null) return DEFAULT_COOLDOWN;
      ProjectileType lastType = playerLastType.get(player.getUniqueId());
      return lastType != null ? tracker.getProjectileCooldown(lastType) : 1000L;
   }
   
   /**
    * Set cooldown for player
    * @param player the player
    * @param type the projectile type
    */
   private void setCooldown(org.bukkit.entity.Player player, ProjectileType type) {
      if (player == null || type == null) return;
      UUID playerId = player.getUniqueId();
      playerCooldowns.put(playerId, System.currentTimeMillis());
      playerLastType.put(playerId, type);
   }
   
   /**
    * Handle projectile hit
    * @param projectile the projectile
    * @param target the target
    */
   public void handleProjectileHit(Projectile projectile, org.bukkit.entity.Entity target) {
      if (projectile == null || target == null) {
         return;
      }
      
      // Validate projectile
      if (!validator.validateForHit(projectile)) {
         return;
      }
      
      // Handle hit
      physics.handleProjectileHit(projectile, target);
      tracker.handleProjectileHit(projectile, target);
   }
   
   /**
    * Check if projectile should be removed
    * @param projectile the projectile
    * @return true if should be removed
    */
   public boolean shouldRemoveProjectile(Projectile projectile) {
      if (projectile == null) {
         return true;
      }
      
      // Check validator
      if (!validator.isValidProjectile(projectile)) {
         return true;
      }
      
      // Check physics
      if (physics.shouldRemoveProjectile(projectile)) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Get projectile info
    * @param projectile the projectile
    * @return projectile info string
    */
   public String getProjectileInfo(Projectile projectile) {
      if (projectile == null) {
         return "Invalid projectile";
      }
      
      return validator.getProjectileInfo(projectile);
   }
   
   /**
    * Get projectile damage
    * @param projectile the projectile
    * @return damage amount
    */
   public double getProjectileDamage(Projectile projectile) {
      if (projectile == null) {
         return DEFAULT_DAMAGE;
      }
      
      return validator.getProjectileDamage(projectile);
   }
   
   /**
    * Get projectile cooldown
    * @param projectile the projectile
    * @return cooldown in milliseconds
    */
   public int getProjectileCooldown(Projectile projectile) {
      if (projectile == null) {
         return DEFAULT_COOLDOWN;
      }
      
      return validator.getProjectileCooldown(projectile);
   }
   
   /**
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return validator.getDefaultProjectileItem();
   }
   
   /**
    * Cleanup old data
    * @param maxAgeMs maximum age in milliseconds
    */
   public void cleanupOldData(long maxAgeMs) {
      tracker.cleanupOldData(maxAgeMs);
      physics.cleanupOldData(maxAgeMs);
      
      // Cleanup old cooldowns
      long currentTime = System.currentTimeMillis();
      playerCooldowns.entrySet().removeIf(entry -> {
         long lastLaunch = entry.getValue();
         return (currentTime - lastLaunch) > maxAgeMs;
      });
   }
   
   /**
    * Get tracked projectile count
    * @return number of tracked projectiles
    */
   public int getTrackedCount() {
      return tracker.getTrackedCount();
   }
   
   /**
    * Get physics projectile count
    * @return number of physics projectiles
    */
   public int getPhysicsCount() {
      return physics.getProjectileCount();
   }
   
   /**
    * Clear all data
    */
   public void clearAll() {
      tracker.clearAll();
      physics.clearAll();
      playerCooldowns.clear();
   }
   
   /**
    * Get validator
    * @return projectile validator
    */
   public ProjectileValidator getValidator() {
      return validator;
   }
   
   /**
    * Get tracker
    * @return projectile tracker
    */
   public ProjectileTracker getTracker() {
      return tracker;
   }
   
   /**
    * Get physics
    * @return projectile physics
    */
   public ProjectilePhysics getPhysics() {
      return physics;
   }
}
