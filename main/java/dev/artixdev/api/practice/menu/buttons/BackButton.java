package dev.artixdev.api.practice.menu.buttons;

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

public class BackButton extends Button {
   private final Menu back;

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.BACK_BUTTON_MATERIAL, XMaterial.PLAYER_HEAD));
      itemBuilder.name(InternalMenuConfig.BACK_BUTTON_NAME);
      itemBuilder.durability(InternalMenuConfig.BACK_BUTTON_DURABILITY);
      itemBuilder.lore(InternalMenuConfig.BACK_BUTTON_LORE);
      ItemStack itemStack = itemBuilder.build();
      if (itemStack.hasItemMeta() && itemStack.getItemMeta() instanceof SkullMeta) {
         itemStack.setItemMeta(SkullUtils.applySkin(itemStack.getItemMeta(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ=="));
      }

      return itemStack;
   }

   public void clicked(Player player, ClickType clickType) {
      Button.playNeutral(player);
      MenuHandler.getInstance().openMenu(this.back, player);
   }

   public BackButton(Menu back) {
      this.back = back;
   }
}
