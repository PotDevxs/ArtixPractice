package dev.artixdev.practice.interfaces;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.models.ItemData;

/**
 * Item Interface
 * Interface for item-related operations
 */
public interface ItemInterface {
   
   /**
    * Get item data
    * @param itemData the item data
    * @return list of strings
    */
   List<String> getItemData(ItemData itemData);
   
   /**
    * Get item stack
    * @return item stack
    */
   ItemStack getItemStack();
   
   /**
    * Get item name
    * @return item name
    */
   String getItemName();
   
   /**
    * Get item lore
    * @return item lore
    */
   List<String> getItemLore();
   
   /**
    * Get item amount
    * @return item amount
    */
   int getItemAmount();
   
   /**
    * Get item durability
    * @return item durability
    */
   short getItemDurability();
   
   /**
    * Check if item is unbreakable
    * @return true if unbreakable
    */
   boolean isUnbreakable();
   
   /**
    * Check if item has enchantments
    * @return true if has enchantments
    */
   boolean hasEnchantments();
   
   /**
    * Get item enchantments
    * @return enchantments map
    */
   java.util.Map<org.bukkit.enchantments.Enchantment, Integer> getEnchantments();
   
   /**
    * Check if item has display name
    * @return true if has display name
    */
   boolean hasDisplayName();
   
   /**
    * Check if item has lore
    * @return true if has lore
    */
   boolean hasLore();
   
   /**
    * Get item flags
    * @return item flags
    */
   java.util.Set<org.bukkit.inventory.ItemFlag> getItemFlags();
   
   /**
    * Check if item is similar to another
    * @param other the other item
    * @return true if similar
    */
   boolean isSimilar(ItemStack other);
   
   /**
    * Clone item
    * @return cloned item
    */
   ItemStack cloneItem();
   
   /**
    * Get item type
    * @return item type
    */
   org.bukkit.Material getItemType();
   
   /**
    * Check if item is air
    * @return true if air
    */
   boolean isAir();
   
   /**
    * Check if item is null
    * @return true if null
    */
   boolean isNull();
   
   /**
    * Get item max stack size
    * @return max stack size
    */
   int getMaxStackSize();
   
   /**
    * Check if item can be stacked
    * @param other the other item
    * @return true if can be stacked
    */
   boolean canStack(ItemStack other);
   
   /**
    * Get item value
    * @return item value
    */
   double getItemValue();
   
   /**
    * Get item weight
    * @return item weight
    */
   double getItemWeight();
   
   /**
    * Check if item is tool
    * @return true if tool
    */
   boolean isTool();
   
   /**
    * Check if item is weapon
    * @return true if weapon
    */
   boolean isWeapon();
   
   /**
    * Check if item is armor
    * @return true if armor
    */
   boolean isArmor();
   
   /**
    * Check if item is food
    * @return true if food
    */
   boolean isFood();
   
   /**
    * Check if item is block
    * @return true if block
    */
   boolean isBlock();
   
   /**
    * Get item rarity
    * @return item rarity
    */
   String getItemRarity();
   
   /**
    * Get item description
    * @return item description
    */
   String getItemDescription();
   
   /**
    * Get item category
    * @return item category
    */
   String getItemCategory();
   
   /**
    * Check if item is custom
    * @return true if custom
    */
   boolean isCustom();
   
   /**
    * Get custom model data
    * @return custom model data
    */
   int getCustomModelData();
   
   /**
    * Check if item has custom model data
    * @return true if has custom model data
    */
   boolean hasCustomModelData();
   
   /**
    * Get item nbt data
    * @return nbt data
    */
   String getNBTData();
   
   /**
    * Check if item has nbt data
    * @return true if has nbt data
    */
   boolean hasNBTData();
   
   /**
    * Get item metadata
    * @return item metadata
    */
   org.bukkit.inventory.meta.ItemMeta getItemMeta();
   
   /**
    * Set item metadata
    * @param meta the metadata
    */
   void setItemMeta(org.bukkit.inventory.meta.ItemMeta meta);
   
   /**
    * Update item
    */
   void updateItem();
   
   /**
    * Refresh item
    */
   void refreshItem();
   
   /**
    * Validate item
    * @return true if valid
    */
   boolean validateItem();
   
   /**
    * Get item hash
    * @return item hash
    */
   int getItemHash();
   
   /**
    * Compare items
    * @param other the other item
    * @return comparison result
    */
   int compareItems(ItemStack other);
   
   /**
    * Get item string representation
    * @return string representation
    */
   String getItemString();
   
   /**
    * Parse item from string
    * @param string the string
    * @return item stack
    */
   ItemStack parseItem(String string);
   
   /**
    * Serialize item
    * @return serialized item
    */
   String serializeItem();
   
   /**
    * Deserialize item
    * @param data the data
    * @return item stack
    */
   ItemStack deserializeItem(String data);
}
