package dev.artixdev.practice.utils;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.google.gson.JsonObject;

/**
 * Item Utils
 * Utility class for item-related operations
 */
public final class ItemUtils {
   
   private static final Logger logger = LogManager.getLogger(ItemUtils.class);
   
   // Predicates for different item types
   public static final Predicate<ItemStack> IS_SWORD = item -> isSword(item);
   public static final Predicate<ItemStack> IS_AXE = item -> isAxe(item);
   public static final Predicate<ItemStack> IS_PICKAXE = item -> isPickaxe(item);
   public static final Predicate<ItemStack> IS_SHOVEL = item -> isShovel(item);
   public static final Predicate<ItemStack> IS_HOE = item -> isHoe(item);
   public static final Predicate<ItemStack> IS_ARMOR = item -> isArmor(item);
   public static final Predicate<ItemStack> IS_TOOL = item -> isTool(item);
   public static final Predicate<ItemStack> IS_WEAPON = item -> isWeapon(item);
   public static final Predicate<ItemStack> IS_BOW = item -> isBow(item);
   public static final Predicate<ItemStack> IS_CROSSBOW = item -> isCrossbow(item);
   public static final Predicate<ItemStack> IS_TRIDENT = item -> isTrident(item);
   public static final Predicate<ItemStack> IS_SHIELD = item -> isShield(item);
   public static final Predicate<ItemStack> IS_POTION = item -> isPotion(item);
   public static final Predicate<ItemStack> IS_FOOD = item -> isFood(item);
   public static final Predicate<ItemStack> IS_BLOCK = item -> isBlock(item);
   public static final Predicate<ItemStack> IS_EMPTY = item -> isEmpty(item);
   
   private static MethodHandle ITEM_META_METHOD_HANDLE;
   
   static {
      try {
         // Initialize method handle for item meta operations
         Method method = ItemMeta.class.getDeclaredMethod("setDisplayName", String.class);
         method.setAccessible(true);
         ITEM_META_METHOD_HANDLE = MethodHandles.lookup().unreflect(method);
      } catch (Exception e) {
         logger.warn("Failed to initialize item meta method handle", e);
      }
   }
   
   /**
    * Check if item is a sword
    * @param item the item
    * @return true if sword
    */
   public static boolean isSword(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_SWORD");
   }
   
   /**
    * Check if item is an axe
    * @param item the item
    * @return true if axe
    */
   public static boolean isAxe(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_AXE");
   }
   
   /**
    * Check if item is a pickaxe
    * @param item the item
    * @return true if pickaxe
    */
   public static boolean isPickaxe(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_PICKAXE");
   }
   
   /**
    * Check if item is a shovel
    * @param item the item
    * @return true if shovel
    */
   public static boolean isShovel(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_SHOVEL");
   }
   
   /**
    * Check if item is a hoe
    * @param item the item
    * @return true if hoe
    */
   public static boolean isHoe(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_HOE");
   }
   
   /**
    * Check if item is armor
    * @param item the item
    * @return true if armor
    */
   public static boolean isArmor(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      String typeName = item.getType().name();
      return typeName.endsWith("_HELMET") || 
             typeName.endsWith("_CHESTPLATE") || 
             typeName.endsWith("_LEGGINGS") || 
             typeName.endsWith("_BOOTS");
   }
   
   /**
    * Check if item is a tool
    * @param item the item
    * @return true if tool
    */
   public static boolean isTool(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      return isSword(item) || isAxe(item) || isPickaxe(item) || 
             isShovel(item) || isHoe(item) || isBow(item) || 
             isCrossbow(item) || isTrident(item) || isShield(item);
   }
   
   /**
    * Check if item is a weapon
    * @param item the item
    * @return true if weapon
    */
   public static boolean isWeapon(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      return isSword(item) || isAxe(item) || isBow(item) || 
             isCrossbow(item) || isTrident(item) || isShield(item);
   }
   
   /**
    * Check if item is a bow
    * @param item the item
    * @return true if bow
    */
   public static boolean isBow(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      return item.getType() == Material.BOW;
   }
   
   /**
    * Check if item is a crossbow
    * @param item the item
    * @return true if crossbow
    */
   public static boolean isCrossbow(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      // CROSSBOW exists only in 1.14+, use XMaterial for cross-version compatibility
      Material crossbowMaterial = XMaterial.CROSSBOW.parseMaterial();
      return crossbowMaterial != null && item.getType() == crossbowMaterial;
   }
   
   /**
    * Check if item is a trident
    * @param item the item
    * @return true if trident
    */
   public static boolean isTrident(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      // TRIDENT exists only in 1.13+, use XMaterial for cross-version compatibility
      Material tridentMaterial = XMaterial.TRIDENT.parseMaterial();
      return tridentMaterial != null && item.getType() == tridentMaterial;
   }
   
   /**
    * Check if item is a shield
    * @param item the item
    * @return true if shield
    */
   public static boolean isShield(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      // SHIELD exists only in 1.9+, use XMaterial for cross-version compatibility
      Material shieldMaterial = XMaterial.SHIELD.parseMaterial();
      return shieldMaterial != null && item.getType() == shieldMaterial;
   }
   
   /**
    * Check if item is a potion
    * @param item the item
    * @return true if potion
    */
   public static boolean isPotion(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      // Use XMaterial for cross-version compatibility
      Material potionMaterial = XMaterial.POTION.parseMaterial();
      Material splashPotionMaterial = XMaterial.SPLASH_POTION.parseMaterial();
      Material lingeringPotionMaterial = XMaterial.LINGERING_POTION.parseMaterial();
      
      Material itemType = item.getType();
      return (potionMaterial != null && itemType == potionMaterial) ||
             (splashPotionMaterial != null && itemType == splashPotionMaterial) ||
             (lingeringPotionMaterial != null && itemType == lingeringPotionMaterial);
   }
   
   /**
    * Check if item is food
    * @param item the item
    * @return true if food
    */
   public static boolean isFood(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      return item.getType().isEdible();
   }
   
   /**
    * Check if item is a block
    * @param item the item
    * @return true if block
    */
   public static boolean isBlock(ItemStack item) {
      if (item == null || item.getType() == Material.AIR) {
         return false;
      }
      
      return item.getType().isBlock();
   }
   
   /**
    * Check if item is empty
    * @param item the item
    * @return true if empty
    */
   public static boolean isEmpty(ItemStack item) {
      return item == null || item.getType() == Material.AIR || item.getAmount() <= 0;
   }
   
   /**
    * Create item from XMaterial
    * @param material the material
    * @return item stack
    */
   public static ItemStack createItem(XMaterial material) {
      if (material == null) {
         return new ItemStack(Material.AIR);
      }
      
      return material.parseItem();
   }
   
   /**
    * Create item from XMaterial with amount
    * @param material the material
    * @param amount the amount
    * @return item stack
    */
   public static ItemStack createItem(XMaterial material, int amount) {
      if (material == null) {
         return new ItemStack(Material.AIR);
      }
      
      ItemStack item = material.parseItem();
      if (item != null) {
         item.setAmount(amount);
      }
      return item;
   }
   
   /**
    * Create item from XMaterial with name
    * @param material the material
    * @param name the name
    * @return item stack
    */
   public static ItemStack createItem(XMaterial material, String name) {
      if (material == null) {
         return new ItemStack(Material.AIR);
      }
      
      ItemStack item = material.parseItem();
      if (item != null && name != null) {
         ItemMeta meta = item.getItemMeta();
         if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
         }
      }
      return item;
   }
   
   /**
    * Create item from XMaterial with name and lore
    * @param material the material
    * @param name the name
    * @param lore the lore
    * @return item stack
    */
   public static ItemStack createItem(XMaterial material, String name, String... lore) {
      if (material == null) {
         return new ItemStack(Material.AIR);
      }
      
      ItemStack item = material.parseItem();
      if (item != null) {
         ItemMeta meta = item.getItemMeta();
         if (meta != null) {
            if (name != null) {
               meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
            if (lore != null && lore.length > 0) {
               List<String> loreList = new ArrayList<>();
               for (String line : lore) {
                  if (line != null) {
                     loreList.add(ChatColor.translateAlternateColorCodes('&', line));
                  }
               }
               meta.setLore(loreList);
            }
            item.setItemMeta(meta);
         }
      }
      return item;
   }
   
   /**
    * Set item name
    * @param item the item
    * @param name the name
    * @return modified item
    */
   public static ItemStack setName(ItemStack item, String name) {
      if (item == null || name == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Set item lore
    * @param item the item
    * @param lore the lore
    * @return modified item
    */
   public static ItemStack setLore(ItemStack item, String... lore) {
      if (item == null || lore == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         List<String> loreList = new ArrayList<>();
         for (String line : lore) {
            if (line != null) {
               loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
         }
         meta.setLore(loreList);
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Set item lore
    * @param item the item
    * @param lore the lore list
    * @return modified item
    */
   public static ItemStack setLore(ItemStack item, List<String> lore) {
      if (item == null || lore == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         List<String> loreList = new ArrayList<>();
         for (String line : lore) {
            if (line != null) {
               loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
         }
         meta.setLore(loreList);
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Add item flags
    * @param item the item
    * @param flags the flags
    * @return modified item
    */
   public static ItemStack addFlags(ItemStack item, ItemFlag... flags) {
      if (item == null || flags == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.addItemFlags(flags);
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Remove item flags
    * @param item the item
    * @param flags the flags
    * @return modified item
    */
   public static ItemStack removeFlags(ItemStack item, ItemFlag... flags) {
      if (item == null || flags == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.removeItemFlags(flags);
         item.setItemMeta(meta);
      }
      return item;
   }
   
   /**
    * Set item unbreakable
    * @param item the item
    * @param unbreakable the unbreakable status
    * @return modified item
    */
   public static ItemStack setUnbreakable(ItemStack item, boolean unbreakable) {
      if (item == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         // setUnbreakable() exists only in 1.11+, use reflection for cross-version compatibility
         try {
            Method setUnbreakableMethod = ItemMeta.class.getMethod("setUnbreakable", boolean.class);
            setUnbreakableMethod.invoke(meta, unbreakable);
            item.setItemMeta(meta);
         } catch (NoSuchMethodException e) {
            // Method doesn't exist in this version (1.8.8), skip silently
         } catch (Exception e) {
            logger.warn("Failed to set unbreakable status for item", e);
         }
      }
      return item;
   }
   
   /**
    * Set item custom model data
    * @param item the item
    * @param customModelData the custom model data
    * @return modified item
    */
   public static ItemStack setCustomModelData(ItemStack item, int customModelData) {
      if (item == null) {
         return item;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         // setCustomModelData() exists only in 1.14+, use reflection for cross-version compatibility
         try {
            Method setCustomModelDataMethod = ItemMeta.class.getMethod("setCustomModelData", Integer.class);
            setCustomModelDataMethod.invoke(meta, customModelData);
            item.setItemMeta(meta);
         } catch (NoSuchMethodException e) {
            // Method doesn't exist in this version (1.8.8), skip silently
         } catch (Exception e) {
            logger.warn("Failed to set custom model data for item", e);
         }
      }
      return item;
   }
   
   /**
    * Check if items are similar
    * @param item1 the first item
    * @param item2 the second item
    * @return true if similar
    */
   public static boolean isSimilar(ItemStack item1, ItemStack item2) {
      if (item1 == null && item2 == null) {
         return true;
      }
      if (item1 == null || item2 == null) {
         return false;
      }
      
      return item1.isSimilar(item2);
   }
   
   /**
    * Get item display name
    * @param item the item
    * @return display name or null
    */
   public static String getDisplayName(ItemStack item) {
      if (item == null) {
         return null;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null && meta.hasDisplayName()) {
         return meta.getDisplayName();
      }
      
      return item.getType().name();
   }
   
   /**
    * Get item lore
    * @param item the item
    * @return lore list or null
    */
   public static List<String> getLore(ItemStack item) {
      if (item == null) {
         return null;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta != null && meta.hasLore()) {
         return meta.getLore();
      }
      
      return null;
   }
   
   /**
    * Check if item has display name
    * @param item the item
    * @return true if has display name
    */
   public static boolean hasDisplayName(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      ItemMeta meta = item.getItemMeta();
      return meta != null && meta.hasDisplayName();
   }
   
   /**
    * Check if item has lore
    * @param item the item
    * @return true if has lore
    */
   public static boolean hasLore(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      ItemMeta meta = item.getItemMeta();
      return meta != null && meta.hasLore();
   }
   
   /**
    * Check if item is unbreakable
    * @param item the item
    * @return true if unbreakable
    */
   public static boolean isUnbreakable(ItemStack item) {
      if (item == null) {
         return false;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return false;
      }
      
      // isUnbreakable() exists only in 1.11+, use reflection for cross-version compatibility
      try {
         Method isUnbreakableMethod = ItemMeta.class.getMethod("isUnbreakable");
         return (Boolean) isUnbreakableMethod.invoke(meta);
      } catch (NoSuchMethodException e) {
         // Method doesn't exist in this version (1.8.8), return false
         return false;
      } catch (Exception e) {
         logger.warn("Failed to check unbreakable status for item", e);
         return false;
      }
   }
   
   /**
    * Get item custom model data
    * @param item the item
    * @return custom model data or 0
    */
   public static int getCustomModelData(ItemStack item) {
      if (item == null) {
         return 0;
      }
      
      ItemMeta meta = item.getItemMeta();
      if (meta == null) {
         return 0;
      }
      
      // hasCustomModelData() and getCustomModelData() exist only in 1.14+, use reflection for cross-version compatibility
      try {
         Method hasCustomModelDataMethod = ItemMeta.class.getMethod("hasCustomModelData");
         if ((Boolean) hasCustomModelDataMethod.invoke(meta)) {
            Method getCustomModelDataMethod = ItemMeta.class.getMethod("getCustomModelData");
            return (Integer) getCustomModelDataMethod.invoke(meta);
         }
      } catch (NoSuchMethodException e) {
         // Methods don't exist in this version (1.8.8), return 0
         return 0;
      } catch (Exception e) {
         logger.warn("Failed to get custom model data for item", e);
         return 0;
      }
      
      return 0;
   }
   
   /**
    * Parse XMaterial from string
    * @param name the name
    * @param fallback the fallback material
    * @return XMaterial or fallback
    */
   public static XMaterial parseFromX(String name, XMaterial fallback) {
      if (name == null || name.isEmpty()) {
         return fallback;
      }
      
      try {
         return XMaterial.matchXMaterial(name).orElse(fallback);
      } catch (Exception e) {
         return fallback;
      }
   }
   
   /**
    * Clear inventory
    * @param inventory the inventory
    */
   public static void clearInventory(Inventory inventory) {
      if (inventory == null) {
         return;
      }
      
      inventory.clear();
   }
   
   /**
    * Clear inventory
    * @param player the player
    */
   public static void clearInventory(Player player) {
      if (player == null) {
         return;
      }
      
      player.getInventory().clear();
      player.getInventory().setArmorContents(null);
      player.updateInventory();
   }
   
   /**
    * Give item to player
    * @param player the player
    * @param item the item
    * @return true if successful
    */
   public static boolean giveItem(Player player, ItemStack item) {
      if (player == null || item == null) {
         return false;
      }
      
      try {
         player.getInventory().addItem(item);
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   /**
    * Remove item from player
    * @param player the player
    * @param item the item
    * @return true if successful
    */
   public static boolean removeItem(Player player, ItemStack item) {
      if (player == null || item == null) {
         return false;
      }
      
      try {
         player.getInventory().removeItem(item);
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   /**
    * Check if player has item
    * @param player the player
    * @param item the item
    * @return true if has item
    */
   public static boolean hasItem(Player player, ItemStack item) {
      if (player == null || item == null) {
         return false;
      }
      
      return player.getInventory().contains(item);
   }
   
   /**
    * Get item count in inventory
    * @param player the player
    * @param item the item
    * @return item count
    */
   public static int getItemCount(Player player, ItemStack item) {
      if (player == null || item == null) {
         return 0;
      }
      
      int count = 0;
      for (ItemStack inventoryItem : player.getInventory().getContents()) {
         if (inventoryItem != null && inventoryItem.isSimilar(item)) {
            count += inventoryItem.getAmount();
         }
      }
      return count;
   }

   public static String serializeItemStack(Object displayIcon) {
    if (displayIcon == null || !(displayIcon instanceof ItemStack)) {
       return "{}";
    }
    return JsonUtils.toJson(displayIcon);
   }

   public static String serializeItemStacks(Object contents) {
    if (contents == null || !(contents instanceof ItemStack[])) {
       return "[]";
    }
    return JsonUtils.toJson(contents);
   }

   public static ItemStack deserializeItemStack(JsonObject itemObject) {
    if (itemObject == null) return null;
    try {
       return JsonUtils.fromJson(itemObject.toString(), ItemStack.class);
    } catch (Exception e) {
       logger.warn("Failed to deserialize ItemStack", e);
       return null;
    }
   }

   public static ItemStack[] deserializeItemStacks(String asString) {
    if (asString == null || asString.isEmpty()) return new ItemStack[0];
    try {
       return JsonUtils.fromJson(asString, new com.google.gson.reflect.TypeToken<ItemStack[]>(){});
    } catch (Exception e) {
       logger.warn("Failed to deserialize ItemStack array", e);
       return new ItemStack[0];
    }
   }
}
