package dev.artixdev.practice.utils;

import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Projectile Tracker
 * Tracks and manages projectiles in practice matches
 */
public class ProjectileTracker {
   
   private static final double DEFAULT_DAMAGE = 0.0;
   private static final int DEFAULT_COOLDOWN = 0;
   
   private final Main plugin;
   private final Map<UUID, ProjectileData> trackedProjectiles;
   private final Map<UUID, Long> cooldowns;
   
   /**
    * Projectile Data
    */
   public static class ProjectileData {
      private final ProjectileType type;
      private final double damage;
      private final long launchTime;
      private final UUID shooterId;
      
      public ProjectileData(ProjectileType type, double damage, long launchTime, UUID shooterId) {
         this.type = type;
         this.damage = damage;
         this.launchTime = launchTime;
         this.shooterId = shooterId;
      }
      
      public ProjectileType getType() {
         return type;
      }
      
      public double getDamage() {
         return damage;
      }
      
      public long getLaunchTime() {
         return launchTime;
      }
      
      public UUID getShooterId() {
         return shooterId;
      }
   }
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileTracker(Main plugin) {
      this.plugin = plugin;
      this.trackedProjectiles = new HashMap<>();
      this.cooldowns = new HashMap<>();
   }
   
   /**
    * Track projectile
    * @param projectile the projectile
    * @param type the projectile type
    * @param damage the damage amount
    * @param shooterId the shooter UUID
    */
   public void trackProjectile(Projectile projectile, ProjectileType type, double damage, UUID shooterId) {
      if (projectile == null || type == null) {
         return;
      }
      
      UUID projectileId = projectile.getUniqueId();
      long launchTime = System.currentTimeMillis();
      
      ProjectileData data = new ProjectileData(type, damage, launchTime, shooterId);
      trackedProjectiles.put(projectileId, data);
      
      // Set cooldown for shooter
      if (shooterId != null) {
         cooldowns.put(shooterId, launchTime);
      }
   }
   
   /**
    * Track projectile with default damage
    * @param projectile the projectile
    * @param type the projectile type
    * @param shooterId the shooter UUID
    */
   public void trackProjectile(Projectile projectile, ProjectileType type, UUID shooterId) {
      trackProjectile(projectile, type, DEFAULT_DAMAGE, shooterId);
   }
   
   /**
    * Stop tracking projectile
    * @param projectile the projectile
    */
   public void stopTracking(Projectile projectile) {
      if (projectile == null) {
         return;
      }
      
      trackedProjectiles.remove(projectile.getUniqueId());
   }
   
   /**
    * Get projectile data
    * @param projectile the projectile
    * @return projectile data or null
    */
   public ProjectileData getProjectileData(Projectile projectile) {
      if (projectile == null) {
         return null;
      }
      
      return trackedProjectiles.get(projectile.getUniqueId());
   }
   
   /**
    * Check if projectile is tracked
    * @param projectile the projectile
    * @return true if tracked
    */
   public boolean isTracked(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      return trackedProjectiles.containsKey(projectile.getUniqueId());
   }
   
   /**
    * Check if shooter is in cooldown
    * @param shooterId the shooter UUID
    * @param cooldownMs the cooldown in milliseconds
    * @return true if in cooldown
    */
   public boolean isInCooldown(UUID shooterId, long cooldownMs) {
      if (shooterId == null) {
         return false;
      }
      
      Long lastLaunch = cooldowns.get(shooterId);
      if (lastLaunch == null) {
         return false;
      }
      
      long currentTime = System.currentTimeMillis();
      return (currentTime - lastLaunch) < cooldownMs;
   }
   
   /**
    * Check if shooter is in cooldown with default time
    * @param shooterId the shooter UUID
    * @return true if in cooldown
    */
   public boolean isInCooldown(UUID shooterId) {
      return isInCooldown(shooterId, DEFAULT_COOLDOWN);
   }
   
   /**
    * Get projectile type from item
    * @param item the item
    * @return projectile type or null
    */
   public ProjectileType getProjectileType(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) return null;
      Material mat = item.getType();
      if (mat == Material.ARROW) return ProjectileType.ARROW;
      if (XMaterial.SNOWBALL.parseMaterial() != null && mat == XMaterial.SNOWBALL.parseMaterial()) return ProjectileType.SNOWBALL;
      if (mat == Material.EGG) return ProjectileType.EGG;
      if (mat == Material.FIREWORK_CHARGE || mat == Material.FIREWORK || mat.name().equals("FIREWORK_ROCKET")) return ProjectileType.FIREWORK_ROCKET;
      if (mat == Material.BLAZE_POWDER) return ProjectileType.BLAZE_POWDER;
      if (XMaterial.TRIDENT.parseMaterial() != null && mat == XMaterial.TRIDENT.parseMaterial()) return ProjectileType.TRIDENT;
      if (XMaterial.SPECTRAL_ARROW.parseMaterial() != null && mat == XMaterial.SPECTRAL_ARROW.parseMaterial()) return ProjectileType.SPECTRAL_ARROW;
      if (XMaterial.TIPPED_ARROW.parseMaterial() != null && mat == XMaterial.TIPPED_ARROW.parseMaterial()) return ProjectileType.TIPPED_ARROW;
      if (mat == Material.ENDER_PEARL) return ProjectileType.ENDER_PEARL;
      if (XMaterial.SPLASH_POTION.parseMaterial() != null && mat == XMaterial.SPLASH_POTION.parseMaterial()) return ProjectileType.SPLASH_POTION;
      if (XMaterial.LINGERING_POTION.parseMaterial() != null && mat == XMaterial.LINGERING_POTION.parseMaterial()) return ProjectileType.LINGERING_POTION;
      return ProjectileType.ARROW;
   }
   
   /**
    * Get projectile damage from type
    * @param type the projectile type
    * @return damage amount
    */
   public double getProjectileDamage(ProjectileType type) {
      if (type == null) {
         return DEFAULT_DAMAGE;
      }
      
      switch (type) {
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
            return DEFAULT_DAMAGE;
      }
   }
   
   /**
    * Get projectile cooldown from type
    * @param type the projectile type
    * @return cooldown in milliseconds
    */
   public long getProjectileCooldown(ProjectileType type) {
      if (type == null) {
         return DEFAULT_COOLDOWN;
      }
      
      switch (type) {
         case ARROW:
            return 1000; // 1 second
         case SPECTRAL_ARROW:
            return 1000;
         case TIPPED_ARROW:
            return 1000;
         case SNOWBALL:
            return 500; // 0.5 seconds
         case EGG:
            return 500;
         case BLAZE_POWDER:
            return 2000; // 2 seconds
         case FIREWORK_ROCKET:
            return 3000; // 3 seconds
         case TRIDENT:
            return 1500; // 1.5 seconds
         default:
            return DEFAULT_COOLDOWN;
      }
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
      
      ProjectileData data = getProjectileData(projectile);
      if (data == null) {
         return;
      }
      
      // Apply damage
      if (data.getDamage() > 0 && target instanceof org.bukkit.entity.Player) {
         org.bukkit.entity.Player player = (org.bukkit.entity.Player) target;
         player.damage(data.getDamage());
      }
      
      // Apply type-specific effects
      applyTypeEffects(projectile, target, data.getType());
      
      // Stop tracking
      stopTracking(projectile);
   }
   
   /**
    * Apply type-specific effects
    * @param projectile the projectile
    * @param target the target
    * @param type the projectile type
    */
   private void applyTypeEffects(Projectile projectile, org.bukkit.entity.Entity target, ProjectileType type) {
      if (projectile == null || target == null || type == null) {
         return;
      }
      
      switch (type) {
         case SPECTRAL_ARROW:
            // Apply glowing effect (1.9+)
            if (target instanceof org.bukkit.entity.Player) {
               org.bukkit.entity.Player player = (org.bukkit.entity.Player) target;
               PotionEffectType glowing = getGlowingEffectType();
               if (glowing != null) {
                  player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                     glowing, 200, 0, false, false));
               }
            }
            break;
         case TIPPED_ARROW:
            if (target instanceof org.bukkit.entity.Player) {
               org.bukkit.entity.Player p = (org.bukkit.entity.Player) target;
               PotionEffectType weak = PotionEffectType.WEAKNESS;
               if (weak != null) p.addPotionEffect(new org.bukkit.potion.PotionEffect(weak, 80, 0, false, false));
            }
            break;
         case FIREWORK_ROCKET:
            try {
               target.getWorld().createExplosion(target.getLocation(), 0.2f, false);
            } catch (IllegalArgumentException e) {
               target.getWorld().createExplosion(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), 0.2f, false, false);
            }
            break;
         case TRIDENT:
            if (target instanceof org.bukkit.entity.Player) {
               org.bukkit.entity.Player p = (org.bukkit.entity.Player) target;
               PotionEffectType slow = PotionEffectType.SLOW;
               if (slow != null) p.addPotionEffect(new org.bukkit.potion.PotionEffect(slow, 60, 0, false, false));
            }
            break;
         default:
            // No special effects
            break;
      }
   }
   
   /**
    * Cleanup old projectiles
    * @param maxAgeMs maximum age in milliseconds
    */
   public void cleanupOldProjectiles(long maxAgeMs) {
      long currentTime = System.currentTimeMillis();
      
      trackedProjectiles.entrySet().removeIf(entry -> {
         ProjectileData data = entry.getValue();
         return (currentTime - data.getLaunchTime()) > maxAgeMs;
      });
   }
   
   /**
    * Cleanup old data (alias for cleanupOldProjectiles)
    * @param maxAgeMs maximum age in milliseconds
    */
   public void cleanupOldData(long maxAgeMs) {
      cleanupOldProjectiles(maxAgeMs);
   }
   
   /**
    * Get tracked projectile count
    * @return number of tracked projectiles
    */
   public int getTrackedCount() {
      return trackedProjectiles.size();
   }
   
   /**
    * Clear all tracked projectiles
    */
   public void clearAll() {
      trackedProjectiles.clear();
      cooldowns.clear();
   }
   
   /**
    * Get GLOWING PotionEffectType (1.9+)
    * Uses reflection for cross-version compatibility
    * @return PotionEffectType.GLOWING or null if not available
    */
   private PotionEffectType getGlowingEffectType() {
      // Try to get by name (works in 1.9+)
      PotionEffectType glowing = PotionEffectType.getByName("GLOWING");
      if (glowing != null) {
         return glowing;
      }
      
      // Try reflection as fallback
      try {
         return (PotionEffectType) PotionEffectType.class.getField("GLOWING").get(null);
      } catch (Exception e) {
         // GLOWING doesn't exist in this version (1.8.8)
         return null;
      }
   }
}
