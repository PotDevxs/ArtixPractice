package dev.artixdev.api.practice.menu.pagination;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.api.practice.menu.bukkit.InternalMenuConfig;
import dev.artixdev.api.practice.menu.util.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class PageButton extends Button {
   private final int mod;
   private final PaginatedMenu menu;

   public ItemStack getButtonItem(Player player) {
      ItemBuilder builder;
      ItemStack itemStack;
      if (this.mod > 0) {
         builder = new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.NEXT_PAGE_BUTTON_MATERIAL, XMaterial.PLAYER_HEAD));
         builder.name(InternalMenuConfig.NEXT_PAGE_BUTTON_NAME.replace("<page>", Integer.toString(this.menu.getPage())).replace("<pages>", Integer.toString(this.menu.getPages(player))));
         builder.durability(InternalMenuConfig.NEXT_PAGE_BUTTON_DURABILITY);
         builder.lore(InternalMenuConfig.NEXT_PAGE_BUTTON_LORE);
         itemStack = builder.build();
         if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof SkullMeta) {
            itemStack.setItemMeta(SkullUtils.applySkin(itemStack.getItemMeta(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19"));
         }

         return itemStack;
      } else {
         builder = new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.PREVIOUS_PAGE_BUTTON_MATERIAL, XMaterial.PLAYER_HEAD));
         builder.name(InternalMenuConfig.PREVIOUS_PAGE_BUTTON_NAME.replace("<page>", Integer.toString(this.menu.getPage())).replace("<pages>", Integer.toString(this.menu.getPages(player))));
         builder.durability(InternalMenuConfig.PREVIOUS_PAGE_BUTTON_DURABILITY);
         builder.lore(InternalMenuConfig.PREVIOUS_PAGE_BUTTON_LORE);
         itemStack = builder.build();
         if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof SkullMeta) {
            itemStack.setItemMeta(SkullUtils.applySkin(itemStack.getItemMeta(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0="));
         }

         return itemStack;
      }
   }

   public void clicked(Player player, ClickType clickType) {
      if (clickType == ClickType.RIGHT) {
         Menu menu = new ViewAllPagesMenu(this.menu);
         MenuHandler.getInstance().openMenu(menu, player);
         Button.playNeutral(player);
      } else if (this.hasNext(player)) {
         this.menu.modPage(player, this.mod);
         Button.playNeutral(player);
      } else {
         Button.playFail(player);
      }
   }

   private boolean hasNext(Player player) {
      int pg = this.menu.getPage() + this.mod;
      return pg > 0 && this.menu.getPages(player) >= pg;
   }

   public PageButton(int mod, PaginatedMenu menu) {
      this.mod = mod;
      this.menu = menu;
   }
}
