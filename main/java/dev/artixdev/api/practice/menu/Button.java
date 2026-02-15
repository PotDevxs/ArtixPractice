package dev.artixdev.api.practice.menu;

import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.artixdev.api.practice.menu.util.CC;
import dev.artixdev.libs.com.cryptomorin.xseries.XSound;

public abstract class Button {
   public static Button placeholder(final Material material, final byte data, final String... title) {
      return new Button() {
         public ItemStack getButtonItem(Player player) {
            ItemStack it = new ItemStack(material, 1, (short)data);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName(String.join(" ", title));
            it.setItemMeta(meta);
            return it;
         }
      };
   }

   public static Button placeholder(final Material material, final byte data, final String title, final List<String> lore) {
      return new Button() {
         public ItemStack getButtonItem(Player player) {
            ItemStack it = new ItemStack(material, 1, (short)data);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName(title);
            meta.setLore(lore);
            it.setItemMeta(meta);
            return it;
         }
      };
   }

   public static Button placeholder(final ItemStack itemStack) {
      return new Button() {
         public ItemStack getButtonItem(Player player) {
            return itemStack;
         }
      };
   }

   public static Button placeholder(final ItemStack itemStack, final String... title) {
      return new Button() {
         public ItemStack getButtonItem(Player player) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(CC.translate(String.join(" ", title)));
            itemStack.setItemMeta(meta);
            return itemStack;
         }
      };
   }

   public static Button placeholder(final Material itemStack) {
      return new Button() {
         public ItemStack getButtonItem(Player player) {
            return new ItemStack(itemStack);
         }
      };
   }

   public static void playFail(Player player) {
      player.playSound(player.getLocation(), XSound.BLOCK_GRASS_BREAK.parseSound(), 20.0F, 0.1F);
   }

   public static void playSuccess(Player player) {
      player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 20.0F, 15.0F);
   }

   public static void playNeutral(Player player) {
      player.playSound(player.getLocation(), XSound.UI_BUTTON_CLICK.parseSound(), 20.0F, 1.0F);
   }

   public abstract ItemStack getButtonItem(Player var1);

   public void clicked(Player player, ClickType clickType) {
   }

   public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
   }

   public boolean shouldCancel(Player player, ClickType clickType) {
      return true;
   }

   public boolean shouldUpdate(Player player, ClickType clickType) {
      return false;
   }
}
