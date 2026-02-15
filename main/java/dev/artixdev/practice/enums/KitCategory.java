package dev.artixdev.practice.enums;

import java.util.List;
import java.util.Random;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;

/**
 * Kit Category
 * Enum for different kit categories
 */
public enum KitCategory {
   
   COMBAT("Combat", "Combat kits for PvP"),
   BUILD("Build", "Building and construction kits"),
   SPECIAL("Special", "Special event kits"),
   RANKED("Ranked", "Ranked competitive kits"),
   UNRANKED("Unranked", "Casual unranked kits"),
   TOURNAMENT("Tournament", "Tournament specific kits"),
   CUSTOM("Custom", "Custom player kits"),
   EVENT("Event", "Event specific kits"),
   PRACTICE("Practice", "Practice and training kits"),
   FUN("Fun", "Fun and casual kits");
   
   private final String name;
   private final String description;
   private final KitCategoryHandler handler;
   
   KitCategory(String name, String description) {
      this.name = name;
      this.description = description;
      this.handler = new KitCategoryHandler(this);
   }
   
   /**
    * Get name
    * @return name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get description
    * @return description
    */
   public String getDescription() {
      return description;
   }
   
   /**
    * Get handler
    * @return handler
    */
   public KitCategoryHandler getHandler() {
      return handler;
   }
   
   /**
    * Get category by name
    * @param name the name
    * @return category or null
    */
   public static KitCategory getByName(String name) {
      if (name == null) {
         return null;
      }
      
      for (KitCategory category : values()) {
         if (category.getName().equalsIgnoreCase(name)) {
            return category;
         }
      }
      
      return null;
   }
   
   /**
    * Get random category
    * @return random category
    */
   public static KitCategory getRandom() {
      KitCategory[] categories = values();
      Random random = new Random();
      return categories[random.nextInt(categories.length)];
   }
   
   /**
    * Get categories for player
    * @param player the player
    * @return list of available categories
    */
   public static List<KitCategory> getCategoriesForPlayer(Player player) {
      List<KitCategory> categories = new java.util.ArrayList<>();
      
      for (KitCategory category : values()) {
         if (category.getHandler().isAvailableForPlayer(player)) {
            categories.add(category);
         }
      }
      
      return categories;
   }
   
   /**
    * Kit Category Handler
    */
   public static class KitCategoryHandler {
      
      private final KitCategory category;
      
      /**
       * Constructor
       * @param category the category
       */
      public KitCategoryHandler(KitCategory category) {
         this.category = category;
      }
      
      /**
       * Check if category is available for player
       * @param player the player
       * @return true if available
       */
      public boolean isAvailableForPlayer(Player player) {
         if (player == null) {
            return false;
         }
         
         // Check permissions based on category
         switch (category) {
            case RANKED:
               return player.hasPermission("bolt.kit.ranked");
            case TOURNAMENT:
               return player.hasPermission("bolt.kit.tournament");
            case CUSTOM:
               return player.hasPermission("bolt.kit.custom");
            case EVENT:
               return player.hasPermission("bolt.kit.event");
            default:
               return true; // Default categories are available to all
         }
      }
      
      /**
       * Get kits in category
       * @return list of kits
       */
      public List<dev.artixdev.practice.models.Kit> getKitsInCategory() {
         // This would be implemented based on your kit system
         return new java.util.ArrayList<>();
      }
      
      /**
       * Get kit count in category
       * @return kit count
       */
      public int getKitCount() {
         return getKitsInCategory().size();
      }
      
      /**
       * Check if category has kits
       * @return true if has kits
       */
      public boolean hasKits() {
         return getKitCount() > 0;
      }
      
      /**
       * Get category icon
       * @return category icon
       */
      public org.bukkit.inventory.ItemStack getCategoryIcon() {
         // Return appropriate icon based on category
         switch (category) {
            case COMBAT:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_SWORD);
            case BUILD:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.BRICK);
            case SPECIAL:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.NETHER_STAR);
            case RANKED:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT);
            case UNRANKED:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.IRON_INGOT);
            case TOURNAMENT:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.GOLD_INGOT);
            case CUSTOM:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.ANVIL);
            case EVENT:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.FIREWORK);
            case PRACTICE:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.BOW);
            case FUN:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.CAKE);
            default:
               return new org.bukkit.inventory.ItemStack(org.bukkit.Material.BARRIER);
         }
      }
      
      /**
       * Get category color
       * @return category color
       */
      public String getCategoryColor() {
         // Return appropriate color based on category
         switch (category) {
            case COMBAT:
               return "&c";
            case BUILD:
               return "&e";
            case SPECIAL:
               return "&d";
            case RANKED:
               return "&6";
            case UNRANKED:
               return "&7";
            case TOURNAMENT:
               return "&b";
            case CUSTOM:
               return "&a";
            case EVENT:
               return "&5";
            case PRACTICE:
               return "&9";
            case FUN:
               return "&3";
            default:
               return "&f";
         }
      }
      
      /**
       * Get category display name
       * @return display name
       */
      public String getDisplayName() {
         return getCategoryColor() + category.getName();
      }
      
      /**
       * Get category lore
       * @return lore list
       */
      public List<String> getCategoryLore() {
         List<String> lore = new java.util.ArrayList<>();
         lore.add("");
         lore.add("&7" + category.getDescription());
         lore.add("&7Kits: &f" + getKitCount());
         lore.add("");
         lore.add("&eClick to view kits in this category");
         return lore;
      }
   }
}
