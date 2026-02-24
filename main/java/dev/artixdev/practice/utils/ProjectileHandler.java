package dev.artixdev.practice.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.cryptomorin.xseries.XSound;
import dev.artixdev.practice.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Projectile Handler
 * Handles projectile-related functionality
 */
public class ProjectileHandler {

   private static final int DEFAULT_COOLDOWN_MS = 1100;
   private static final ItemStack DEFAULT_ITEM = XMaterial.BLAZE_POWDER.parseItem();
   private final Map<UUID, Long> shooterCooldowns = new HashMap<>();
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileHandler(Main plugin) {
      this.plugin = plugin;
   }
   
   /**
    * Check if projectile is allowed
    * @param projectile the projectile
    * @return true if allowed
    */
   public boolean isProjectileAllowed(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check if projectile is in a practice arena
      if (plugin.getArenaManager().isInArena(projectile.getLocation())) {
         return true;
      }
      
      // Check if projectile is in a match
      if (plugin.getMatchManager().isLocationInMatch(projectile.getLocation())) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Get projectile cooldown
    * @return cooldown in milliseconds
    */
   public int getProjectileCooldown() {
      return DEFAULT_COOLDOWN_MS;
   }
   
   /**
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return DEFAULT_ITEM.clone();
   }
   
   /**
    * Check if projectile is in cooldown
    * @param projectile the projectile
    * @return true if in cooldown
    */
   public boolean isInCooldown(Projectile projectile) {
      if (projectile == null || !(projectile.getShooter() instanceof Entity)) return false;
      UUID shooterId = ((Entity) projectile.getShooter()).getUniqueId();
      Long last = shooterCooldowns.get(shooterId);
      return last != null && (System.currentTimeMillis() - last) < DEFAULT_COOLDOWN_MS;
   }
   
   public void handleProjectileLaunch(Projectile projectile) {
      if (projectile == null) return;
      if (!isProjectileAllowed(projectile)) {
         projectile.remove();
         return;
      }
      if (projectile.getShooter() instanceof Entity) {
         shooterCooldowns.put(((Entity) projectile.getShooter()).getUniqueId(), System.currentTimeMillis());
      }
      applyProjectileEffects(projectile);
   }
   
   /**
    * Handle projectile hit
    * @param projectile the projectile
    */
   public void handleProjectileHit(Projectile projectile) {
      if (projectile == null) {
         return;
      }
      
      // Apply hit effects
      applyHitEffects(projectile);
   }
   
   /**
    * Apply projectile effects
    * @param projectile the projectile
    */
   private void applyProjectileEffects(Projectile projectile) {
      if (projectile == null) return;
      Sound s = XSound.ENTITY_ARROW_SHOOT.parseSound();
      if (s != null) projectile.getWorld().playSound(projectile.getLocation(), s, 0.25f, 1.2f);
   }

   private void applyHitEffects(Projectile projectile) {
      if (projectile == null) return;
      Sound s = XSound.ENTITY_ARROW_HIT.parseSound();
      if (s != null) projectile.getWorld().playSound(projectile.getLocation(), s, 0.4f, 1.0f);
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
      
      // Remove if not in practice area
      if (!isProjectileAllowed(projectile)) {
         return true;
      }
      
      // Remove if in cooldown
      if (isInCooldown(projectile)) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Get projectile type from item
    * @param item the item
    * @return projectile type or null
    */
   public String getProjectileType(ItemStack item) {
      if (item == null) {
         return null;
      }
      
      XMaterial material = XMaterial.matchXMaterial(item);
      if (material == null) {
         return null;
      }
      
      switch (material) {
         case BLAZE_POWDER:
            return "blaze_powder";
         case SNOWBALL:
            return "snowball";
         case EGG:
            return "egg";
         case ARROW:
            return "arrow";
         case SPECTRAL_ARROW:
            return "spectral_arrow";
         case TIPPED_ARROW:
            return "tipped_arrow";
         case FIREWORK_ROCKET:
            return "firework_rocket";
         case TRIDENT:
            return "trident";
         default:
            return null;
      }
   }
   
   /**
    * Check if item can be used as projectile
    * @param item the item
    * @return true if can be used
    */
   public boolean canUseAsProjectile(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      String projectileType = getProjectileType(item);
      return projectileType != null;
   }
   
   /**
    * Get projectile damage
    * @param projectile the projectile
    * @return damage amount
    */
   public double getProjectileDamage(Projectile projectile) {
      if (projectile == null) return 0.0;
      if (projectile instanceof Arrow) return 2.0;
      switch (projectile.getType().name()) {
         case "SNOWBALL":
         case "EGG": return 0.0;
         case "FIREBALL": return 6.0;
         case "TRIDENT": return 8.0;
         default: return 1.0;
      }
   }

   public double getProjectileKnockback(Projectile projectile) {
      if (projectile == null) return 0.0;
      if (projectile instanceof Arrow) return 1.0;
      if (projectile.getType().name().equals("TRIDENT")) return 1.2;
      return 1.0;
   }
}
