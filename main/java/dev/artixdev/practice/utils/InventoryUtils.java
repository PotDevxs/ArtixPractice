package dev.artixdev.practice.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;

/**
 * Inventory Utils
 * Utility class for inventory-related operations
 */
public final class InventoryUtils {
   
   /**
    * Convert armor contents to inventory contents
    * @param armorContents the armor contents
    * @return inventory contents
    */
   public static ItemStack[] convertArmorToInventory(ItemStack[] armorContents) {
      ItemStack[] inventoryContents = new ItemStack[36];
      
      try {
         // Copy armor contents to inventory slots
         System.arraycopy(armorContents, 0, inventoryContents, 27, 9);
         System.arraycopy(armorContents, 9, inventoryContents, 0, 27);
         return inventoryContents;
      } catch (Exception e) {
         return new ItemStack[36];
      }
   }
   
   /**
    * Convert inventory contents to armor contents
    * @param inventoryContents the inventory contents
    * @return armor contents
    */
   public static ItemStack[] convertInventoryToArmor(ItemStack[] inventoryContents) {
      ItemStack[] armorContents = new ItemStack[36];
      
      try {
         // Copy inventory contents to armor slots
         System.arraycopy(inventoryContents, 0, armorContents, 9, 27);
         System.arraycopy(inventoryContents, 27, armorContents, 0, 9);
         return armorContents;
      } catch (Exception e) {
         return new ItemStack[36];
      }
   }
   
   /**
    * Get armor contents from inventory
    * @param inventoryContents the inventory contents
    * @return armor contents
    */
   public static ItemStack[] getArmorContents(ItemStack[] inventoryContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return new ItemStack[4];
      }
      
      ItemStack[] armor = new ItemStack[4];
      armor[0] = inventoryContents[39]; // Helmet
      armor[1] = inventoryContents[38]; // Chestplate
      armor[2] = inventoryContents[37]; // Leggings
      armor[3] = inventoryContents[36]; // Boots
      
      return armor;
   }
   
   /**
    * Set armor contents in inventory
    * @param inventoryContents the inventory contents
    * @param armorContents the armor contents
    * @return modified inventory contents
    */
   public static ItemStack[] setArmorContents(ItemStack[] inventoryContents, ItemStack[] armorContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return inventoryContents;
      }
      
      if (armorContents == null || armorContents.length < 4) {
         return inventoryContents;
      }
      
      inventoryContents[39] = armorContents[0]; // Helmet
      inventoryContents[38] = armorContents[1]; // Chestplate
      inventoryContents[37] = armorContents[2]; // Leggings
      inventoryContents[36] = armorContents[3]; // Boots
      
      return inventoryContents;
   }
   
   /**
    * Get hotbar contents from inventory
    * @param inventoryContents the inventory contents
    * @return hotbar contents
    */
   public static ItemStack[] getHotbarContents(ItemStack[] inventoryContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return new ItemStack[9];
      }
      
      ItemStack[] hotbar = new ItemStack[9];
      System.arraycopy(inventoryContents, 0, hotbar, 0, 9);
      return hotbar;
   }
   
   /**
    * Set hotbar contents in inventory
    * @param inventoryContents the inventory contents
    * @param hotbarContents the hotbar contents
    * @return modified inventory contents
    */
   public static ItemStack[] setHotbarContents(ItemStack[] inventoryContents, ItemStack[] hotbarContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return inventoryContents;
      }
      
      if (hotbarContents == null || hotbarContents.length < 9) {
         return inventoryContents;
      }
      
      System.arraycopy(hotbarContents, 0, inventoryContents, 0, 9);
      return inventoryContents;
   }
   
   /**
    * Get main inventory contents (excluding hotbar and armor)
    * @param inventoryContents the inventory contents
    * @return main inventory contents
    */
   public static ItemStack[] getMainInventoryContents(ItemStack[] inventoryContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return new ItemStack[27];
      }
      
      ItemStack[] mainInventory = new ItemStack[27];
      System.arraycopy(inventoryContents, 9, mainInventory, 0, 27);
      return mainInventory;
   }
   
   /**
    * Set main inventory contents
    * @param inventoryContents the inventory contents
    * @param mainInventoryContents the main inventory contents
    * @return modified inventory contents
    */
   public static ItemStack[] setMainInventoryContents(ItemStack[] inventoryContents, ItemStack[] mainInventoryContents) {
      if (inventoryContents == null || inventoryContents.length < 36) {
         return inventoryContents;
      }
      
      if (mainInventoryContents == null || mainInventoryContents.length < 27) {
         return inventoryContents;
      }
      
      System.arraycopy(mainInventoryContents, 0, inventoryContents, 9, 27);
      return inventoryContents;
   }
   
   /**
    * Clear inventory contents
    * @param inventoryContents the inventory contents
    * @return cleared inventory contents
    */
   public static ItemStack[] clearInventory(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return new ItemStack[36];
      }
      
      ItemStack[] cleared = new ItemStack[36];
      for (int i = 0; i < cleared.length; i++) {
         cleared[i] = new ItemStack(Material.AIR);
      }
      return cleared;
   }
   
   /**
    * Check if inventory is empty
    * @param inventoryContents the inventory contents
    * @return true if empty
    */
   public static boolean isEmpty(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return true;
      }
      
      for (ItemStack item : inventoryContents) {
         if (item != null && item.getType() != Material.AIR) {
            return false;
         }
      }
      return true;
   }
   
   /**
    * Count items in inventory
    * @param inventoryContents the inventory contents
    * @param material the material to count
    * @return item count
    */
   public static int countItems(ItemStack[] inventoryContents, Material material) {
      if (inventoryContents == null || material == null) {
         return 0;
      }
      
      int count = 0;
      for (ItemStack item : inventoryContents) {
         if (item != null && item.getType() == material) {
            count += item.getAmount();
         }
      }
      return count;
   }
   
   /**
    * Count items in inventory
    * @param inventoryContents the inventory contents
    * @param itemStack the item stack to count
    * @return item count
    */
   public static int countItems(ItemStack[] inventoryContents, ItemStack itemStack) {
      if (inventoryContents == null || itemStack == null) {
         return 0;
      }
      
      int count = 0;
      for (ItemStack item : inventoryContents) {
         if (item != null && item.isSimilar(itemStack)) {
            count += item.getAmount();
         }
      }
      return count;
   }
   
   /**
    * Find item in inventory
    * @param inventoryContents the inventory contents
    * @param material the material to find
    * @return slot index or -1 if not found
    */
   public static int findItem(ItemStack[] inventoryContents, Material material) {
      if (inventoryContents == null || material == null) {
         return -1;
      }
      
      for (int i = 0; i < inventoryContents.length; i++) {
         ItemStack item = inventoryContents[i];
         if (item != null && item.getType() == material) {
            return i;
         }
      }
      return -1;
   }
   
   /**
    * Find item in inventory
    * @param inventoryContents the inventory contents
    * @param itemStack the item stack to find
    * @return slot index or -1 if not found
    */
   public static int findItem(ItemStack[] inventoryContents, ItemStack itemStack) {
      if (inventoryContents == null || itemStack == null) {
         return -1;
      }
      
      for (int i = 0; i < inventoryContents.length; i++) {
         ItemStack item = inventoryContents[i];
         if (item != null && item.isSimilar(itemStack)) {
            return i;
         }
      }
      return -1;
   }
   
   /**
    * Add item to inventory
    * @param inventoryContents the inventory contents
    * @param itemStack the item to add
    * @return true if successful
    */
   public static boolean addItem(ItemStack[] inventoryContents, ItemStack itemStack) {
      if (inventoryContents == null || itemStack == null) {
         return false;
      }
      
      // Find empty slot
      int emptySlot = findEmptySlot(inventoryContents);
      if (emptySlot != -1) {
         inventoryContents[emptySlot] = itemStack;
         return true;
      }
      
      // Try to stack with existing items
      for (int i = 0; i < inventoryContents.length; i++) {
         ItemStack item = inventoryContents[i];
         if (item != null && item.isSimilar(itemStack)) {
            int maxStack = item.getMaxStackSize();
            int currentAmount = item.getAmount();
            int addAmount = itemStack.getAmount();
             
            if (currentAmount + addAmount <= maxStack) {
               item.setAmount(currentAmount + addAmount);
               return true;
            }
         }
      }
      
      return false;
   }
   
   /**
    * Remove item from inventory
    * @param inventoryContents the inventory contents
    * @param itemStack the item to remove
    * @return true if successful
    */
   public static boolean removeItem(ItemStack[] inventoryContents, ItemStack itemStack) {
      if (inventoryContents == null || itemStack == null) {
         return false;
      }
      
      int removeAmount = itemStack.getAmount();
      
      for (int i = 0; i < inventoryContents.length; i++) {
         ItemStack item = inventoryContents[i];
         if (item != null && item.isSimilar(itemStack)) {
            int currentAmount = item.getAmount();
             
            if (currentAmount >= removeAmount) {
               item.setAmount(currentAmount - removeAmount);
               if (item.getAmount() <= 0) {
                  inventoryContents[i] = new ItemStack(Material.AIR);
               }
               return true;
            } else {
               removeAmount -= currentAmount;
               inventoryContents[i] = new ItemStack(Material.AIR);
            }
         }
      }
      
      return false;
   }
   
   /**
    * Find empty slot in inventory
    * @param inventoryContents the inventory contents
    * @return slot index or -1 if no empty slot
    */
   public static int findEmptySlot(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return -1;
      }
      
      for (int i = 0; i < inventoryContents.length; i++) {
         ItemStack item = inventoryContents[i];
         if (item == null || item.getType() == Material.AIR) {
            return i;
         }
      }
      return -1;
   }
   
   /**
    * Get inventory size
    * @param inventoryContents the inventory contents
    * @return inventory size
    */
   public static int getInventorySize(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return 0;
      }
      
      return inventoryContents.length;
   }
   
   /**
    * Get occupied slots count
    * @param inventoryContents the inventory contents
    * @return occupied slots count
    */
   public static int getOccupiedSlotsCount(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return 0;
      }
      
      int count = 0;
      for (ItemStack item : inventoryContents) {
         if (item != null && item.getType() != Material.AIR) {
            count++;
         }
      }
      return count;
   }
   
   /**
    * Get free slots count
    * @param inventoryContents the inventory contents
    * @return free slots count
    */
   public static int getFreeSlotsCount(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return 0;
      }
      
      return inventoryContents.length - getOccupiedSlotsCount(inventoryContents);
   }
   
   /**
    * Check if inventory has space for item
    * @param inventoryContents the inventory contents
    * @param itemStack the item stack
    * @return true if has space
    */
   public static boolean hasSpace(ItemStack[] inventoryContents, ItemStack itemStack) {
      if (inventoryContents == null || itemStack == null) {
         return false;
      }
      
      // Check for empty slot
      if (findEmptySlot(inventoryContents) != -1) {
         return true;
      }
      
      // Check for stackable slot
      for (ItemStack item : inventoryContents) {
         if (item != null && item.isSimilar(itemStack)) {
            int maxStack = item.getMaxStackSize();
            int currentAmount = item.getAmount();
            int addAmount = itemStack.getAmount();
             
            if (currentAmount + addAmount <= maxStack) {
               return true;
            }
         }
      }
      
      return false;
   }
   
   /**
    * Get inventory weight (total item count)
    * @param inventoryContents the inventory contents
    * @return inventory weight
    */
   public static int getInventoryWeight(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return 0;
      }
      
      int weight = 0;
      for (ItemStack item : inventoryContents) {
         if (item != null && item.getType() != Material.AIR) {
            weight += item.getAmount();
         }
      }
      return weight;
   }
   
   /**
    * Get inventory value (total item value)
    * @param inventoryContents the inventory contents
    * @return inventory value
    */
   public static double getInventoryValue(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return 0.0;
      }
      
      double value = 0.0;
      for (ItemStack item : inventoryContents) {
         if (item != null && item.getType() != Material.AIR) {
            // Simple value calculation based on material
            value += getItemValue(item);
         }
      }
      return value;
   }
   
   /**
    * Get item value
    * @param item the item
    * @return item value
    */
   private static double getItemValue(ItemStack item) {
      if (item == null) {
         return 0.0;
      }
      
      // Simple value calculation
      Material material = item.getType();
      double baseValue = 1.0;
      
      // Adjust value based on material
      if (material.name().contains("DIAMOND")) {
         baseValue = 10.0;
      } else if (material.name().contains("GOLD")) {
         baseValue = 5.0;
      } else if (material.name().contains("IRON")) {
         baseValue = 3.0;
      } else if (material.name().contains("STONE")) {
         baseValue = 2.0;
      } else if (material.name().contains("WOOD")) {
         baseValue = 1.0;
      }
      
      // Adjust value based on enchantments
      if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
         baseValue *= 1.5;
      }
      
      return baseValue * item.getAmount();
   }
   
   /**
    * Clone inventory contents
    * @param inventoryContents the inventory contents
    * @return cloned inventory contents
    */
   public static ItemStack[] cloneInventory(ItemStack[] inventoryContents) {
      if (inventoryContents == null) {
         return null;
      }
      
      ItemStack[] cloned = new ItemStack[inventoryContents.length];
      for (int i = 0; i < inventoryContents.length; i++) {
         if (inventoryContents[i] != null) {
            cloned[i] = inventoryContents[i].clone();
         }
      }
      return cloned;
   }
   
   /**
    * Compare inventories
    * @param inventory1 the first inventory
    * @param inventory2 the second inventory
    * @return true if equal
    */
   public static boolean compareInventories(ItemStack[] inventory1, ItemStack[] inventory2) {
      if (inventory1 == null && inventory2 == null) {
         return true;
      }
      if (inventory1 == null || inventory2 == null) {
         return false;
      }
      if (inventory1.length != inventory2.length) {
         return false;
      }
      
      for (int i = 0; i < inventory1.length; i++) {
         if (!ItemUtils.isSimilar(inventory1[i], inventory2[i])) {
            return false;
         }
      }
      return true;
   }
}
