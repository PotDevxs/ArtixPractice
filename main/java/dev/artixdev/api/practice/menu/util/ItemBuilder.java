package dev.artixdev.api.practice.menu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class ItemBuilder {
   private static final Logger log = LogManager.getLogger(ItemBuilder.class);
   private final ItemStack is;

   public static XMaterial parseFromX(String material, XMaterial backup) {
      if (!XMaterial.supports(9) && material.equals("SPLASH_POTION")) {
         return XMaterial.POTION;
      } else {
         Optional<XMaterial> opt = XMaterial.matchXMaterial(material);
         if (!opt.isPresent()) {
            log.error("Invalid Material specified '{}', please fix this in Menu config!", material);
            return backup;
         } else {
            return (XMaterial)opt.get();
         }
      }
   }

   public ItemBuilder(Material mat) {
      this.is = new ItemStack(mat);
      this.is.setItemMeta(Bukkit.getItemFactory().getItemMeta(this.is.getType()));
   }

   public ItemBuilder(XMaterial mat) {
      this.is = mat.parseItem();
      this.is.setItemMeta(Bukkit.getItemFactory().getItemMeta(this.is.getType()));
   }

   public ItemBuilder(ItemStack is) {
      this.is = is;
      is.setItemMeta(Bukkit.getItemFactory().getItemMeta(is.getType()));
   }

   public ItemBuilder amount(int amount) {
      this.is.setAmount(amount);
      return this;
   }

   public ItemBuilder color(Color color) {
      ItemMeta meta = this.getItemMeta();
      if (this.is.getType() != Material.LEATHER_BOOTS && this.is.getType() != Material.LEATHER_CHESTPLATE && this.is.getType() != Material.LEATHER_HELMET && this.is.getType() != Material.LEATHER_LEGGINGS) {
         throw new IllegalArgumentException("color() only applicable for leather armor!");
      } else {
         LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)meta;
         leatherArmorMeta.setColor(color);
         this.is.setItemMeta(meta);
         return this;
      }
   }

   public ItemBuilder name(String name) {
      if (name != null && !name.isEmpty()) {
         ItemMeta meta = this.getItemMeta();
         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
         this.is.setItemMeta(meta);
         return this;
      } else {
         return this;
      }
   }

   public ItemBuilder lore(String string) {
      ItemMeta meta = this.getItemMeta();
      List<String> lore = meta.getLore();
      if (lore == null) {
         lore = new ArrayList();
      }

      ((List)lore).add(ChatColor.translateAlternateColorCodes('&', string));
      meta.setLore((List)lore);
      this.is.setItemMeta(meta);
      return this;
   }

   public ItemBuilder lore(List<String> lore) {
      if (lore != null && !lore.isEmpty()) {
         List<String> toSet = new ArrayList();
         ItemMeta meta = this.is.getItemMeta();
         if (!this.is.hasItemMeta()) {
            return this;
         } else {
            Iterator var4 = lore.iterator();

            while(var4.hasNext()) {
               String string = (String)var4.next();
               toSet.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            meta.setLore(toSet);
            this.is.setItemMeta(meta);
            return this;
         }
      } else {
         return this;
      }
   }

   public ItemBuilder skullOwner(String username) {
      ItemMeta meta = this.getItemMeta();

      try {
         SkullMeta im = (SkullMeta)meta;
         im.setOwner(username);
         this.is.setItemMeta(im);
      } catch (Exception e) {
      }

      return this;
   }

   public ItemBuilder durability(int durability) {
      if (durability == 0) {
         return this;
      } else {
         this.is.getData().setData((byte)durability);
         this.is.setDurability((short)durability);
         return this;
      }
   }

   public ItemBuilder enchantment(Enchantment enchantment, int level) {
      this.is.addUnsafeEnchantment(enchantment, level);
      return this;
   }

   public ItemBuilder enchantment(Enchantment enchantment) {
      this.is.addUnsafeEnchantment(enchantment, 1);
      return this;
   }

   public ItemBuilder enchantment(Map<Enchantment, Integer> map) {
      ItemStack var10001 = this.is;
      map.forEach(var10001::addEnchantment);
      return this;
   }

   public ItemBuilder type(Material material) {
      this.is.setType(material);
      return this;
   }

   public ItemBuilder clearLore() {
      ItemMeta meta = this.getItemMeta();
      meta.setLore(new ArrayList());
      this.is.setItemMeta(meta);
      return this;
   }

   public ItemBuilder clearEnchantments() {
      Iterator var1 = this.is.getEnchantments().keySet().iterator();

      while(var1.hasNext()) {
         Enchantment e = (Enchantment)var1.next();
         this.is.removeEnchantment(e);
      }

      return this;
   }

   public ItemBuilder clearFlags() {
      ItemMeta meta = this.getItemMeta();
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_PLACED_ON});
      this.is.setItemMeta(meta);
      return this;
   }

   public ItemStack build() {
      return this.is;
   }

   private ItemMeta getItemMeta() {
      return this.is.hasItemMeta() ? this.is.getItemMeta() : Bukkit.getItemFactory().getItemMeta(this.is.getType());
   }
}
