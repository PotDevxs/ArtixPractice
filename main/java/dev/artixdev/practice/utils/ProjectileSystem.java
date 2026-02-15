package dev.artixdev.practice.utils;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

/**
 * Projectile System
 * Main system for managing all projectile functionality
 */
public class ProjectileSystem {
   
   private static final int DEFAULT_COOLDOWN = 1100; // 1.1 seconds
   private static final double DEFAULT_DAMAGE = 0.0;
   
   private final Main plugin;
   private final ProjectileManager manager;
   private final ProjectileValidator validator;
   private final ProjectileTracker tracker;
   private final ProjectilePhysics physics;
   private final ProjectileItemManager itemManager;
   private final ProjectileConfig config;
   private final ProjectileSpecialItem specialItem;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileSystem(Main plugin) {
      this.plugin = plugin;
      this.manager = new ProjectileManager(plugin);
      this.validator = new ProjectileValidator(plugin);
      this.tracker = new ProjectileTracker(plugin);
      this.physics = new ProjectilePhysics(plugin);
      this.itemManager = new ProjectileItemManager(plugin);
      this.config = new ProjectileConfig(plugin);
      this.specialItem = new ProjectileSpecialItem(plugin, ProjectileType.ARROW);
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
      return DEFAULT_COOLDOWN;
   }
   
   /**
    * Get projectile item
    * @return projectile item
    */
   public ItemStack getProjectileItem() {
      return itemManager.getDefaultProjectileItem();
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
      
      // Use manager to launch projectile
      manager.launchProjectile(shooter, type, damage, velocity);
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
      
      // Use manager to handle hit
      manager.handleProjectileHit(projectile, target);
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
      
      // Use manager to check removal
      return manager.shouldRemoveProjectile(projectile);
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
      
      return manager.getProjectileInfo(projectile);
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
      
      return manager.getProjectileDamage(projectile);
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
      
      return manager.getProjectileCooldown(projectile);
   }
   
   /**
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return manager.getDefaultProjectileItem();
   }
   
   /**
    * Cleanup old data
    * @param maxAgeMs maximum age in milliseconds
    */
   public void cleanupOldData(long maxAgeMs) {
      manager.cleanupOldData(maxAgeMs);
   }
   
   /**
    * Get tracked projectile count
    * @return number of tracked projectiles
    */
   public int getTrackedCount() {
      return manager.getTrackedCount();
   }
   
   /**
    * Get physics projectile count
    * @return number of physics projectiles
    */
   public int getPhysicsCount() {
      return manager.getPhysicsCount();
   }
   
   /**
    * Clear all data
    */
   public void clearAll() {
      manager.clearAll();
   }
   
   /**
    * Get manager
    * @return projectile manager
    */
   public ProjectileManager getManager() {
      return manager;
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
   
   /**
    * Get item manager
    * @return projectile item manager
    */
   public ProjectileItemManager getItemManager() {
      return itemManager;
   }
   
   /**
    * Get config
    * @return projectile config
    */
   public ProjectileConfig getConfig() {
      return config;
   }
   
   /**
    * Get special item
    * @return projectile special item
    */
   public ProjectileSpecialItem getSpecialItem() {
      return specialItem;
   }
   
   /**
    * Get system info
    * @return system info string
    */
   public String getSystemInfo() {
      return String.format("ProjectileSystem{tracked=%d, physics=%d, manager=%s}", 
         getTrackedCount(), 
         getPhysicsCount(), 
         manager != null ? "active" : "null");
   }
   
   /**
    * Initialize system
    */
   public void initialize() {
      // Initialize all components
      if (manager != null) {
         // Manager is already initialized
      }
      
      if (validator != null) {
         // Validator is already initialized
      }
      
      if (tracker != null) {
         // Tracker is already initialized
      }
      
      if (physics != null) {
         // Physics is already initialized
      }
      
      if (itemManager != null) {
         // Item manager is already initialized
      }
      
      if (config != null) {
         // Config is already initialized
      }
      
      if (specialItem != null) {
         // Special item is already initialized
      }
   }
   
   /**
    * Shutdown system
    */
   public void shutdown() {
      // Clear all data
      clearAll();
   }
}
