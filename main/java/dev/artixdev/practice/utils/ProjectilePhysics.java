package dev.artixdev.practice.utils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Projectile Physics
 * Handles physics and behavior for projectiles
 */
public class ProjectilePhysics {
   
   private static final int DEFAULT_COOLDOWN = 1100; // 1.1 seconds
   private static final int MAX_TICKS_LIVED = 500; // 25 seconds
   private static final double DEFAULT_DAMAGE = 0.0;
   
   private static final long COOLDOWN_MS = 1000L;

   private final Main plugin;
   private final Map<UUID, ProjectileData> projectileData;
   private final Map<UUID, Long> playerCooldowns;
   
   /**
    * Projectile Data
    */
   public static class ProjectileData {
      private final ProjectileType type;
      private final double damage;
      private final Vector velocity;
      private final long launchTime;
      private final UUID shooterId;
      
      public ProjectileData(ProjectileType type, double damage, Vector velocity, long launchTime, UUID shooterId) {
         this.type = type;
         this.damage = damage;
         this.velocity = velocity;
         this.launchTime = launchTime;
         this.shooterId = shooterId;
      }
      
      public ProjectileType getType() {
         return type;
      }
      
      public double getDamage() {
         return damage;
      }
      
      public Vector getVelocity() {
         return velocity;
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
   public ProjectilePhysics(Main plugin) {
      this.plugin = plugin;
      this.projectileData = new HashMap<>();
      this.playerCooldowns = new HashMap<>();
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
      
      // Remove if dead
      if (projectile.isDead()) {
         return true;
      }
      
      // Remove if lived too long
      if (projectile.getTicksLived() > MAX_TICKS_LIVED) {
         return true;
      }
      
      // Remove if not in practice area
      if (!isInPracticeArea(projectile)) {
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
      return DEFAULT_COOLDOWN;
   }
   
   /**
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return XMaterial.BLAZE_POWDER.parseItem();
   }
   
   /**
    * Launch projectile
    * @param shooter the shooter
    * @param type the projectile type
    * @param damage the damage amount
    * @param velocity the velocity
    */
   public void launchProjectile(Player shooter, ProjectileType type, double damage, Vector velocity) {
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
      
      // Store data
      UUID projectileId = projectile.getUniqueId();
      long launchTime = System.currentTimeMillis();
      ProjectileData data = new ProjectileData(type, damage, velocity, launchTime, shooter.getUniqueId());
      projectileData.put(projectileId, data);
      
      // Set cooldown
      setCooldown(shooter);
   }
   
   /**
    * Create projectile
    * @param shooter the shooter
    * @param type the projectile type
    * @return projectile or null
    */
   private Projectile createProjectile(Player shooter, ProjectileType type) {
      if (shooter == null || type == null) {
         return null;
      }
      
      Location location = shooter.getEyeLocation();
      Vector direction = location.getDirection();
      
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
   private boolean isInCooldown(Player player) {
      if (player == null) return false;
      Long last = playerCooldowns.get(player.getUniqueId());
      return last != null && (System.currentTimeMillis() - last) < COOLDOWN_MS;
   }

   private void setCooldown(Player player) {
      if (player == null) return;
      playerCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
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
      
      ProjectileData data = projectileData.get(projectile.getUniqueId());
      if (data == null) {
         return;
      }
      
      // Apply damage
      if (data.getDamage() > 0 && target instanceof Player) {
         Player player = (Player) target;
         player.damage(data.getDamage());
      }
      
      // Apply type-specific effects
      applyTypeEffects(projectile, target, data.getType());
      
      // Remove projectile data
      projectileData.remove(projectile.getUniqueId());
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
            if (target instanceof Player) {
               Player player = (Player) target;
               PotionEffectType glowing = getGlowingEffectType();
               if (glowing != null) {
                  player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                     glowing, 200, 0, false, false));
               }
            }
            break;
         case TIPPED_ARROW:
            if (target instanceof Player) {
               Player p = (Player) target;
               if (PotionEffectType.WEAKNESS != null) p.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.WEAKNESS, 80, 0, false, false));
            }
            break;
         case FIREWORK_ROCKET:
            try {
               target.getWorld().createExplosion(target.getLocation(), 0.2f, false);
            } catch (IllegalArgumentException e) {
               org.bukkit.Location loc = target.getLocation();
               target.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 0.2f, false, false);
            }
            break;
         case TRIDENT:
            if (target instanceof Player) {
               Player p = (Player) target;
               if (PotionEffectType.SLOW != null) p.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.SLOW, 60, 0, false, false));
            }
            break;
         default:
            // No special effects
            break;
      }
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
      
      return projectileData.get(projectile.getUniqueId());
   }
   
   /**
    * Remove projectile data
    * @param projectile the projectile
    */
   public void removeProjectileData(Projectile projectile) {
      if (projectile == null) {
         return;
      }
      
      projectileData.remove(projectile.getUniqueId());
   }
   
   /**
    * Cleanup old projectile data
    * @param maxAgeMs maximum age in milliseconds
    */
   public void cleanupOldData(long maxAgeMs) {
      long currentTime = System.currentTimeMillis();
      
      projectileData.entrySet().removeIf(entry -> {
         ProjectileData data = entry.getValue();
         return (currentTime - data.getLaunchTime()) > maxAgeMs;
      });
   }
   
   /**
    * Get projectile count
    * @return number of tracked projectiles
    */
   public int getProjectileCount() {
      return projectileData.size();
   }
   
   /**
    * Clear all projectile data
    */
   public void clearAll() {
      projectileData.clear();
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
