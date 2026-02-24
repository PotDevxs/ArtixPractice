package dev.artixdev.practice.utils;

import org.bukkit.Effect;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.Sound;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.cryptomorin.xseries.XSound;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

/**
 * Projectile Special Item
 * Manages special projectile items and their behavior
 */
public class ProjectileSpecialItem {
   
   private static final ItemStack SPECIAL_ITEM = XMaterial.CAULDRON.parseItem();
   private static final int DEFAULT_COOLDOWN = 1100; // 1.1 seconds
   
   private final Main plugin;
   private final ProjectileType projectileType;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    * @param projectileType the projectile type
    */
   public ProjectileSpecialItem(Main plugin, ProjectileType projectileType) {
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
    * Get special projectile item
    * @return special item
    */
   public ItemStack getSpecialProjectileItem() {
      return SPECIAL_ITEM.clone();
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
    * Check if item is special projectile item
    * @param item the item
    * @return true if special
    */
   public boolean isSpecialProjectileItem(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      XMaterial material = XMaterial.matchXMaterial(item);
      return material == XMaterial.CAULDRON;
   }
   
   /**
    * Get special item properties
    * @return special properties
    */
   public String getSpecialProperties() {
      return "Special projectile item with enhanced effects";
   }
   
   /**
    * Get projectile info
    * @return projectile info string
    */
   public String getProjectileInfo() {
      if (projectileType == null) {
         return "Unknown projectile type";
      }
      
      return String.format("Type: %s, Damage: %.1f, Cooldown: %dms, Special: %s", 
         projectileType.getDisplayName(), 
         getProjectileDamage(), 
         getProjectileCooldown(),
         getSpecialProperties());
   }
   
   /**
    * Check if projectile can be launched
    * @param projectile the projectile
    * @return true if can be launched
    */
   public boolean canLaunchProjectile(Projectile projectile) {
      if (projectile == null) {
         return false;
      }
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return false;
      }
      
      // Check if projectile has valid type
      if (projectileType == null) {
         return false;
      }
      
      return true;
   }
   
   /**
    * Handle projectile launch
    * @param projectile the projectile
    */
   public void handleProjectileLaunch(Projectile projectile) {
      if (projectile == null) {
         return;
      }
      
      // Check if projectile can be launched
      if (!canLaunchProjectile(projectile)) {
         return;
      }
      
      // Apply special effects
      applySpecialEffects(projectile);
   }
   
   /**
    * Apply special effects to projectile
    * @param projectile the projectile
    */
   private void applySpecialEffects(Projectile projectile) {
      if (projectile == null) return;
      Sound s = XSound.ENTITY_ARROW_SHOOT.parseSound();
      if (s != null) projectile.getWorld().playSound(projectile.getLocation(), s, 0.25f, 1.2f);
      projectile.getWorld().playEffect(projectile.getLocation(), Effect.SMOKE, 0);
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
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return;
      }
      
      // Apply special hit effects
      applySpecialHitEffects(projectile, target);
   }
   
   /**
    * Apply special hit effects
    * @param projectile the projectile
    * @param target the target
    */
   private void applySpecialHitEffects(Projectile projectile, org.bukkit.entity.Entity target) {
      if (projectile == null || target == null) return;
      Sound s = XSound.ENTITY_ARROW_HIT.parseSound();
      if (s != null) target.getWorld().playSound(target.getLocation(), s, 0.4f, 1.0f);
      target.getWorld().playEffect(target.getLocation(), Effect.CRIT, 0);
      if (target instanceof org.bukkit.entity.LivingEntity) {
         Vector knockback = target.getLocation().toVector().subtract(projectile.getLocation().toVector()).normalize().multiply(0.15);
         target.setVelocity(target.getVelocity().add(knockback));
      }
   }
   
   /**
    * Get special item display name
    * @return display name
    */
   public String getSpecialItemDisplayName() {
      return "Special Projectile Item";
   }
   
   /**
    * Get special item lore
    * @return lore array
    */
   public String[] getSpecialItemLore() {
      return new String[]{
         "Special projectile item",
         "Enhanced effects and behavior",
         "Use in practice matches"
      };
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
      
      // Check if projectile is valid
      if (!isValidProjectile(projectile)) {
         return true;
      }
      
      // Check if projectile is dead
      if (projectile.isDead()) {
         return true;
      }
      
      return false;
   }
   
   /**
    * Get projectile summary
    * @return projectile summary
    */
   public String getSummary() {
      if (projectileType == null) {
         return "ProjectileSpecialItem{type=null, damage=0.0, cooldown=1100ms, special=true}";
      }
      
      return String.format("ProjectileSpecialItem{type=%s, damage=%.1f, cooldown=%dms, special=true}", 
         projectileType.getDisplayName(), 
         getProjectileDamage(), 
         getProjectileCooldown());
   }
}
