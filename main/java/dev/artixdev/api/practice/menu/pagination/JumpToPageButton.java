package dev.artixdev.api.practice.menu.pagination;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.artixdev.api.practice.menu.Button;

public class JumpToPageButton extends Button {
   private final int page;
   private final PaginatedMenu menu;
   private final boolean current;

   public ItemStack getButtonItem(Player player) {
      ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
      ItemMeta itemMeta = itemStack.getItemMeta();
      itemMeta.setDisplayName(ChatColor.RED + "Page " + this.page);
      if (this.current) {
         itemMeta.setLore(Arrays.asList("", ChatColor.GREEN + "Current page"));
      }

      itemStack.setItemMeta(itemMeta);
      return itemStack;
   }

   public void clicked(Player player, ClickType clickType) {
      this.menu.modPage(player, this.page - this.menu.getPage());
      Button.playNeutral(player);
   }

   public JumpToPageButton(int page, PaginatedMenu menu, boolean current) {
      this.page = page;
      this.menu = menu;
      this.current = current;
   }
}
