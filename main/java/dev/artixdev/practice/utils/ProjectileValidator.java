package dev.artixdev.practice.utils;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

/**
 * Projectile Validator
 * Validates and manages projectile behavior
 */
public class ProjectileValidator {
   
   private static final double DEFAULT_DAMAGE = 0.0;
   private static final int DEFAULT_COOLDOWN = 0;
   
   private final Main plugin;
   private double damage;
   private int cooldown;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileValidator(Main plugin) {
      this.plugin = plugin;
      this.damage = DEFAULT_DAMAGE;
      this.cooldown = DEFAULT_COOLDOWN;
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
      
      // Check if projectile has valid type
      if (!hasValidType(projectile)) {
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
    * Check if projectile has valid type
    * @param projectile the projectile
    * @return true if has valid type
    */
   private boolean hasValidType(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check projectile type
      if (projectile instanceof org.bukkit.entity.Arrow) {
         return true;
      }
      // SpectralArrow exists in 1.9+, check by class name for compatibility
      if (isSpectralArrow(projectile)) {
         return true;
      }
      if (projectile instanceof org.bukkit.entity.Snowball) {
         return true;
      }
      if (projectile instanceof org.bukkit.entity.Egg) {
         return true;
      }
      if (projectile instanceof org.bukkit.entity.Firework) {
         return true;
      }
      // Trident exists in 1.13+, check by class name for compatibility
      if (isTrident(projectile)) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Check if projectile is a SpectralArrow (1.9+)
    * Uses reflection for cross-version compatibility
    */
   private boolean isSpectralArrow(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      String className = projectile.getClass().getName();
      return className.equals("org.bukkit.craftbukkit.entity.CraftSpectralArrow") 
          || className.contains("SpectralArrow");
   }
   
   /**
    * Check if projectile is a Trident (1.13+)
    * Uses reflection for cross-version compatibility
    */
   private boolean isTrident(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      String className = projectile.getClass().getName();
      return className.equals("org.bukkit.craftbukkit.entity.CraftTrident") 
          || className.contains("Trident");
   }
   
   /**
    * Get projectile type
    * @param projectile the projectile
    * @return projectile type or null
    */
   public ProjectileType getProjectileType(Projectile projectile) {
      if (projectile == null) {
         return null;
      }
      
      if (projectile instanceof org.bukkit.entity.Arrow) {
         return ProjectileType.ARROW;
      }
      // SpectralArrow exists in 1.9+, check by class name for compatibility
      if (isSpectralArrow(projectile)) {
         return ProjectileType.SPECTRAL_ARROW;
      }
      if (projectile instanceof org.bukkit.entity.Snowball) {
         return ProjectileType.SNOWBALL;
      }
      if (projectile instanceof org.bukkit.entity.Egg) {
         return ProjectileType.EGG;
      }
      if (projectile instanceof org.bukkit.entity.Firework) {
         return ProjectileType.FIREWORK_ROCKET;
      }
      // Trident exists in 1.13+, check by class name for compatibility
      if (isTrident(projectile)) {
         return ProjectileType.TRIDENT;
      }
      
      return null;
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
      
      ProjectileType type = getProjectileType(projectile);
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
    * Get projectile cooldown
    * @param projectile the projectile
    * @return cooldown in milliseconds
    */
   public int getProjectileCooldown(Projectile projectile) {
      if (projectile == null) {
         return DEFAULT_COOLDOWN;
      }
      
      ProjectileType type = getProjectileType(projectile);
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
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return new ItemStack(org.bukkit.Material.BLAZE_POWDER);
   }
   
   /**
    * Set damage
    * @param damage the damage
    */
   public void setDamage(double damage) {
      this.damage = damage;
   }
   
   /**
    * Get damage
    * @return damage
    */
   public double getDamage() {
      return damage;
   }
   
   /**
    * Set cooldown
    * @param cooldown the cooldown
    */
   public void setCooldown(int cooldown) {
      this.cooldown = cooldown;
   }
   
   /**
    * Get cooldown
    * @return cooldown
    */
   public int getCooldown() {
      return cooldown;
   }
   
   /**
    * Validate projectile for launch
    * @param projectile the projectile
    * @return true if valid for launch
    */
   public boolean validateForLaunch(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return false;
      }
      
      // Check if projectile has valid damage
      if (getProjectileDamage(projectile) < 0) {
         return false;
      }
      
      // Check if projectile has valid cooldown
      if (getProjectileCooldown(projectile) < 0) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Validate projectile for hit
    * @param projectile the projectile
    * @return true if valid for hit
    */
   public boolean validateForHit(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return false;
      }
      
      // Check if projectile is not dead
      if (projectile.isDead()) {
         return false;
      }
      
      return true;
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
      
      ProjectileType type = getProjectileType(projectile);
      double damage = getProjectileDamage(projectile);
      int cooldown = getProjectileCooldown(projectile);
      
      return String.format("Type: %s, Damage: %.1f, Cooldown: %dms", 
         type != null ? type.getDisplayName() : "Unknown", 
         damage, 
         cooldown);
   }
}
