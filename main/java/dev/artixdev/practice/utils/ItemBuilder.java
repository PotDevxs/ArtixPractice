package dev.artixdev.practice.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Item Builder
 * Utility class for building custom items
 */
public class ItemBuilder {
   
   private static final Logger logger = LogManager.getLogger(ItemBuilder.class);
   
   private final ItemStack itemStack;
   
   /**
    * Constructor
    * @param material the material
    */
   public ItemBuilder(XMaterial material) {
      this.itemStack = material.parseItem();
      ItemFactory itemFactory = Bukkit.getItemFactory();
      Material materialType = this.itemStack.getType();
      ItemMeta itemMeta = itemFactory.getItemMeta(materialType);
      this.itemStack.setItemMeta(itemMeta);
   }
   
   /**
    * Constructor
    * @param material the material
    */
   public ItemBuilder(Material material) {
      this.itemStack = new ItemStack(material);
   }
   
   /**
    * Constructor
    * @param itemStack the item stack
    */
   public ItemBuilder(ItemStack itemStack) {
      this.itemStack = itemStack.clone();
   }
   
   /**
    * Set item name
    * @param name the name
    * @return this builder
    */
   public ItemBuilder name(String name) {
      if (name == null) {
         return this;
      }
      
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
         this.itemStack.setItemMeta(itemMeta);
      }
      
      return this;
   }
   
   /**
    * Set item lore
    * @param lore the lore
    * @return this builder
    */
   public ItemBuilder lore(String... lore) {
      if (lore == null || lore.length == 0) {
         return this;
      }
      
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         List<String> loreList = new ArrayList<>();
         for (String line : lore) {
            if (line != null) {
               loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
         }
         itemMeta.setLore(loreList);
         this.itemStack.setItemMeta(itemMeta);
      }
      
      return this;
   }
   
   /**
    * Set item lore
    * @param lore the lore list
    * @return this builder
    */
   public ItemBuilder lore(List<String> lore) {
      if (lore == null || lore.isEmpty()) {
         return this;
      }
      
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         List<String> loreList = new ArrayList<>();
         for (String line : lore) {
            if (line != null) {
               loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
         }
         itemMeta.setLore(loreList);
         this.itemStack.setItemMeta(itemMeta);
      }
      
      return this;
   }
   
   /**
    * Set item amount
    * @param amount the amount
    * @return this builder
    */
   public ItemBuilder amount(int amount) {
      if (amount > 0) {
         this.itemStack.setAmount(amount);
      }
      return this;
   }
   
   /**
    * Set item durability
    * @param durability the durability
    * @return this builder
    */
   public ItemBuilder durability(short durability) {
      this.itemStack.setDurability(durability);
      return this;
   }
   
   /**
    * Add enchantment
    * @param enchantment the enchantment
    * @param level the level
    * @return this builder
    */
   public ItemBuilder enchant(Enchantment enchantment, int level) {
      if (enchantment != null && level > 0) {
         this.itemStack.addUnsafeEnchantment(enchantment, level);
      }
      return this;
   }
   
   /**
    * Add enchantments
    * @param enchantments the enchantments map
    * @return this builder
    */
   public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
      if (enchantments != null && !enchantments.isEmpty()) {
         for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null && entry.getValue() > 0) {
               this.itemStack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
         }
      }
      return this;
   }
   
   /**
    * Add item flags
    * @param flags the flags
    * @return this builder
    */
   public ItemBuilder flags(ItemFlag... flags) {
      if (flags != null && flags.length > 0) {
         ItemMeta itemMeta = this.itemStack.getItemMeta();
         if (itemMeta != null) {
            itemMeta.addItemFlags(flags);
            this.itemStack.setItemMeta(itemMeta);
         }
      }
      return this;
   }
   
   /**
    * Set unbreakable
    * @param unbreakable the unbreakable status
    * @return this builder
    */
   public ItemBuilder unbreakable(boolean unbreakable) {
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         // setUnbreakable() exists only in 1.11+, use reflection for cross-version compatibility
         try {
            Method setUnbreakableMethod = ItemMeta.class.getMethod("setUnbreakable", boolean.class);
            setUnbreakableMethod.invoke(itemMeta, unbreakable);
            this.itemStack.setItemMeta(itemMeta);
         } catch (NoSuchMethodException e) {
            // Method doesn't exist in this version (1.8.8), skip silently
         } catch (Exception e) {
            logger.warn("Failed to set unbreakable status for item", e);
         }
      }
      return this;
   }
   
   /**
    * Set leather armor color
    * @param color the color
    * @return this builder
    */
   public ItemBuilder color(Color color) {
      if (color == null) {
         return this;
      }
      
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta instanceof LeatherArmorMeta) {
         LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemMeta;
         leatherMeta.setColor(color);
         this.itemStack.setItemMeta(leatherMeta);
      }
      
      return this;
   }
   
   /**
    * Set leather armor color
    * @param red the red value
    * @param green the green value
    * @param blue the blue value
    * @return this builder
    */
   public ItemBuilder color(int red, int green, int blue) {
      return color(Color.fromRGB(red, green, blue));
   }
   
   /**
    * Set leather armor color
    * @param hex the hex color
    * @return this builder
    */
   public ItemBuilder color(String hex) {
      if (hex == null || hex.isEmpty()) {
         return this;
      }
      
      try {
         if (hex.startsWith("#")) {
            hex = hex.substring(1);
         }
         
         int color = Integer.parseInt(hex, 16);
         return color(Color.fromRGB(color));
      } catch (NumberFormatException e) {
         return this;
      }
   }
   
   /**
    * Set glow effect
    * @return this builder
    */
   public ItemBuilder glow() {
      return enchant(Enchantment.DURABILITY, 1).flags(ItemFlag.HIDE_ENCHANTS);
   }
   
   /**
    * Set custom model data
    * @param customModelData the custom model data
    * @return this builder
    */
   public ItemBuilder customModelData(int customModelData) {
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         // setCustomModelData() exists only in 1.14+, use reflection for cross-version compatibility
         try {
            Method setCustomModelDataMethod = ItemMeta.class.getMethod("setCustomModelData", Integer.class);
            setCustomModelDataMethod.invoke(itemMeta, customModelData);
            this.itemStack.setItemMeta(itemMeta);
         } catch (NoSuchMethodException e) {
            // Method doesn't exist in this version (1.8.8), skip silently
         } catch (Exception e) {
            logger.warn("Failed to set custom model data for item", e);
         }
      }
      return this;
   }
   
   /**
    * Set localized name
    * @param localizedName the localized name
    * @return this builder
    */
   public ItemBuilder localizedName(String localizedName) {
      if (localizedName == null) {
         return this;
      }
      
      ItemMeta itemMeta = this.itemStack.getItemMeta();
      if (itemMeta != null) {
         // setLocalizedName() exists only in 1.13+, use reflection for cross-version compatibility
         try {
            Method setLocalizedNameMethod = ItemMeta.class.getMethod("setLocalizedName", String.class);
            setLocalizedNameMethod.invoke(itemMeta, localizedName);
            this.itemStack.setItemMeta(itemMeta);
         } catch (NoSuchMethodException e) {
            // Method doesn't exist in this version (1.8.8), skip silently
         } catch (Exception e) {
            logger.warn("Failed to set localized name for item", e);
         }
      }
      return this;
   }
   
   /**
    * Build the item
    * @return the built item
    */
   public ItemStack build() {
      return this.itemStack.clone();
   }
   
   /**
    * Get the item stack
    * @return the item stack
    */
   public ItemStack getItemStack() {
      return this.itemStack;
   }
   
   /**
    * Check if item is similar to another
    * @param other the other item
    * @return true if similar
    */
   public boolean isSimilar(ItemStack other) {
      return this.itemStack.isSimilar(other);
   }
   
   /**
    * Check if item is similar to another
    * @param other the other item builder
    * @return true if similar
    */
   public boolean isSimilar(ItemBuilder other) {
      return other != null && this.itemStack.isSimilar(other.itemStack);
   }
   
   /**
    * Clone the item builder
    * @return cloned item builder
    */
   public ItemBuilder clone() {
      return new ItemBuilder(this.itemStack);
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      ItemBuilder that = (ItemBuilder) obj;
      return Objects.equals(itemStack, that.itemStack);
   }
   
   @Override
   public int hashCode() {
      return Objects.hash(itemStack);
   }
   
   @Override
   public String toString() {
      return "ItemBuilder{itemStack=" + itemStack + '}';
   }

   public void setAmount(int displayAmount) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setAmount'");
   }

   public void setName(String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setName'");
   }

   public void setDurability(int mATCH_INVENTORY_POTIONS_DURABILITY) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setDurability'");
   }

   public void addLore(String processedLore) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addLore'");
   }

   public void setLore(List<String> mATCH_INVENTORY_HEALTH_LORE) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setLore'");
   }

   public void setGlow(boolean b) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setGlow'");
   }

   public static ItemMeta setSkullMeta(ItemMeta meta, String username, String skinValue, String skinSignature) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setSkullMeta'");
   }
}
