package dev.artixdev.practice.utils;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.ProjectileType;

/**
 * Projectile Item Manager
 * Manages projectile items and their properties
 */
public class ProjectileItemManager {
   
   private static final ItemStack DEFAULT_ITEM = XMaterial.REDSTONE_BLOCK.parseItem();
   private static final ItemStack SPECIAL_ITEM = XMaterial.RED_DYE.parseItem();
   
   private final Main plugin;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public ProjectileItemManager(Main plugin) {
      this.plugin = plugin;
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
    * Get default projectile item
    * @return default item
    */
   public ItemStack getDefaultProjectileItem() {
      return DEFAULT_ITEM.clone();
   }
   
   /**
    * Get special projectile item
    * @return special item
    */
   public ItemStack getSpecialProjectileItem() {
      return SPECIAL_ITEM.clone();
   }
   
   /**
    * Get projectile item by type
    * @param type the projectile type
    * @return item or null
    */
   public ItemStack getProjectileItem(ProjectileType type) {
      if (type == null) {
         return getDefaultProjectileItem();
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
            return getDefaultProjectileItem();
      }
   }
   
   /**
    * Get projectile type from item
    * @param item the item
    * @return projectile type or null
    */
   public ProjectileType getProjectileType(ItemStack item) {
      if (item == null) {
         return null;
      }
      
      XMaterial material = XMaterial.matchXMaterial(item);
      if (material == null) {
         return null;
      }
      
      switch (material) {
         case ARROW:
            return ProjectileType.ARROW;
         case SPECTRAL_ARROW:
            return ProjectileType.SPECTRAL_ARROW;
         case TIPPED_ARROW:
            return ProjectileType.TIPPED_ARROW;
         case SNOWBALL:
            return ProjectileType.SNOWBALL;
         case EGG:
            return ProjectileType.EGG;
         case BLAZE_POWDER:
            return ProjectileType.BLAZE_POWDER;
         case FIREWORK_ROCKET:
            return ProjectileType.FIREWORK_ROCKET;
         case TRIDENT:
            return ProjectileType.TRIDENT;
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
      
      ProjectileType type = getProjectileType(item);
      return type != null;
   }
   
   /**
    * Get projectile damage from item
    * @param item the item
    * @return damage amount
    */
   public double getProjectileDamage(ItemStack item) {
      if (item == null) {
         return 0.0;
      }
      
      ProjectileType type = getProjectileType(item);
      if (type == null) {
         return 0.0;
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
            return 0.0;
      }
   }
   
   /**
    * Get projectile cooldown from item
    * @param item the item
    * @return cooldown in milliseconds
    */
   public long getProjectileCooldown(ItemStack item) {
      if (item == null) {
         return 0;
      }
      
      ProjectileType type = getProjectileType(item);
      if (type == null) {
         return 0;
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
            return 0;
      }
   }
   
   /**
    * Create projectile item with custom properties
    * @param type the projectile type
    * @param name the custom name
    * @param lore the custom lore
    * @return custom item
    */
   public ItemStack createCustomProjectileItem(ProjectileType type, String name, String... lore) {
      if (type == null) {
         return getDefaultProjectileItem();
      }
      
      ItemStack item = getProjectileItem(type);
      if (item == null) {
         return getDefaultProjectileItem();
      }
      
      if (name != null || (lore != null && lore.length > 0)) {
         dev.artixdev.practice.utils.ItemBuilder builder = new dev.artixdev.practice.utils.ItemBuilder(item);
         if (name != null) builder.name(name);
         if (lore != null && lore.length > 0) {
            java.util.List<String> loreList = new java.util.ArrayList<>();
            for (String line : lore) loreList.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', line != null ? line : ""));
            builder.lore(loreList);
         }
         return builder.build();
      }
      return item;
   }
   
   /**
    * Get projectile item info
    * @param item the item
    * @return item info string
    */
   public String getProjectileItemInfo(ItemStack item) {
      if (item == null) {
         return "Invalid item";
      }
      
      ProjectileType type = getProjectileType(item);
      if (type == null) {
         return "Not a projectile item";
      }
      
      double damage = getProjectileDamage(item);
      long cooldown = getProjectileCooldown(item);
      
      return String.format("Type: %s, Damage: %.1f, Cooldown: %dms", 
         type.getDisplayName(), 
         damage, 
         cooldown);
   }
   
   /**
    * Check if item is special projectile
    * @param item the item
    * @return true if special
    */
   public boolean isSpecialProjectile(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      XMaterial material = XMaterial.matchXMaterial(item);
      if (material == null) {
         return false;
      }
      
      return material == XMaterial.RED_DYE || material == XMaterial.REDSTONE_BLOCK;
   }
   
   /**
    * Get special projectile properties
    * @param item the item
    * @return special properties or null
    */
   public String getSpecialProperties(ItemStack item) {
      if (item == null || !isSpecialProjectile(item)) {
         return null;
      }
      
      XMaterial material = XMaterial.matchXMaterial(item);
      if (material == null) {
         return null;
      }
      
      switch (material) {
         case RED_DYE:
            return "Special projectile with enhanced effects";
         case REDSTONE_BLOCK:
            return "Default projectile item";
         default:
            return null;
      }
   }
}
