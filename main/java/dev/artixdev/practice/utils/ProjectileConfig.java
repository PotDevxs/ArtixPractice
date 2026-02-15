package dev.artixdev.practice.utils;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

/**
 * Projectile Configuration
 * Manages projectile configuration and settings
 */
public class ProjectileConfig {
   
   private static final int DEFAULT_COOLDOWN = 1100; // 1.1 seconds
   private static final double DEFAULT_DAMAGE = 0.0;
   
   private final Main plugin;
   private double damage;
   private int cooldown;
   private ItemStack projectileItem;
   private ProjectileType projectileType;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileConfig(Main plugin) {
      this.plugin = plugin;
      this.damage = DEFAULT_DAMAGE;
      this.cooldown = DEFAULT_COOLDOWN;
      this.projectileItem = null;
      this.projectileType = null;
   }
   
   /**
    * Constructor with projectile type
    * @param plugin the plugin instance
    * @param projectileType the projectile type
    */
   public ProjectileConfig(Main plugin, ProjectileType projectileType) {
      this.plugin = plugin;
      this.projectileType = projectileType;
      this.damage = getDefaultDamage(projectileType);
      this.cooldown = getDefaultCooldown(projectileType);
      this.projectileItem = getDefaultItem(projectileType);
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
      
      // Check if projectile is in practice area
      if (!isInPracticeArea(projectile)) {
         return false;
      }
      
      return true;
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
      
      // Check if in arena
      if (plugin.getArenaManager().isInArena(projectile.getLocation())) {
         return true;
      }
      
      // Check if in match
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
      return cooldown;
   }
   
   /**
    * Set projectile cooldown
    * @param cooldown the cooldown
    */
   public void setProjectileCooldown(int cooldown) {
      this.cooldown = cooldown;
   }
   
   /**
    * Get projectile damage
    * @return damage amount
    */
   public double getProjectileDamage() {
      return damage;
   }
   
   /**
    * Set projectile damage
    * @param damage the damage
    */
   public void setProjectileDamage(double damage) {
      this.damage = damage;
   }
   
   /**
    * Get projectile item
    * @return projectile item
    */
   public ItemStack getProjectileItem() {
      return projectileItem;
   }
   
   /**
    * Set projectile item
    * @param projectileItem the projectile item
    */
   public void setProjectileItem(ItemStack projectileItem) {
      this.projectileItem = projectileItem;
   }
   
   /**
    * Get projectile type
    * @return projectile type
    */
   public ProjectileType getProjectileType() {
      return projectileType;
   }
   
   /**
    * Set projectile type
    * @param projectileType the projectile type
    */
   public void setProjectileType(ProjectileType projectileType) {
      this.projectileType = projectileType;
   }
   
   /**
    * Get default damage for projectile type
    * @param type the projectile type
    * @return default damage
    */
   private double getDefaultDamage(ProjectileType type) {
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
    * Get default cooldown for projectile type
    * @param type the projectile type
    * @return default cooldown
    */
   private int getDefaultCooldown(ProjectileType type) {
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
    * Get default item for projectile type
    * @param type the projectile type
    * @return default item
    */
   private ItemStack getDefaultItem(ProjectileType type) {
      if (type == null) {
         return null;
      }
      
      switch (type) {
         case ARROW:
            return XMaterial.ARROW.parseItem();
         case SPECTRAL_ARROW:
            return XMaterial.SPECTRAL_ARROW.parseItem();
         case TIPPED_ARROW:
            return XMaterial.TIPPED_ARROW.parseItem();
         case SNOWBALL:
            return XMaterial.SNOWBALL.parseItem();
         case EGG:
            return XMaterial.EGG.parseItem();
         case BLAZE_POWDER:
            return XMaterial.BLAZE_POWDER.parseItem();
         case FIREWORK_ROCKET:
            return XMaterial.FIREWORK_ROCKET.parseItem();
         case TRIDENT:
            return XMaterial.TRIDENT.parseItem();
         default:
            return null;
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
         damage, 
         cooldown);
   }
   
   /**
    * Check if projectile is configured
    * @return true if configured
    */
   public boolean isConfigured() {
      return projectileType != null && damage >= 0 && cooldown >= 0;
   }
   
   /**
    * Reset to default values
    */
   public void resetToDefaults() {
      if (projectileType != null) {
         this.damage = getDefaultDamage(projectileType);
         this.cooldown = getDefaultCooldown(projectileType);
         this.projectileItem = getDefaultItem(projectileType);
      } else {
         this.damage = DEFAULT_DAMAGE;
         this.cooldown = DEFAULT_COOLDOWN;
         this.projectileItem = null;
      }
   }
   
   /**
    * Clone configuration
    * @return cloned configuration
    */
   public ProjectileConfig clone() {
      ProjectileConfig clone = new ProjectileConfig(plugin, projectileType);
      clone.setProjectileDamage(damage);
      clone.setProjectileCooldown(cooldown);
      clone.setProjectileItem(projectileItem != null ? projectileItem.clone() : null);
      return clone;
   }
   
   /**
    * Check if configuration is valid
    * @return true if valid
    */
   public boolean isValid() {
      return projectileType != null && damage >= 0 && cooldown >= 0;
   }
   
   /**
    * Get configuration summary
    * @return configuration summary
    */
   public String getSummary() {
      if (!isValid()) {
         return "Invalid configuration";
      }
      
      return String.format("ProjectileConfig{type=%s, damage=%.1f, cooldown=%dms, item=%s}", 
         projectileType.getDisplayName(), 
         damage, 
         cooldown,
         projectileItem != null ? projectileItem.getType().name() : "null");
   }
}
