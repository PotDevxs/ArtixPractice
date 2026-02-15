package dev.artixdev.api.practice.menu.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.bukkit.InternalMenuConfig;
import dev.artixdev.api.practice.menu.util.ItemBuilder;
import dev.artixdev.api.practice.menu.util.TypeCallback;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class ConfirmationButton extends Button {
   private final boolean confirm;
   private final TypeCallback<Boolean> callback;
   private final boolean closeAfterResponse;
   private final Menu menu;

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(ItemBuilder.parseFromX(this.confirm ? InternalMenuConfig.CONFIRMATION_BUTTON_MATERIAL : InternalMenuConfig.CANCEL_BUTTON_MATERIAL, this.confirm ? XMaterial.LIME_WOOL : XMaterial.RED_WOOL));
      itemBuilder.name(this.confirm ? InternalMenuConfig.CONFIRMATION_BUTTON_NAME : InternalMenuConfig.CANCEL_BUTTON_NAME);
      itemBuilder.lore(this.confirm ? InternalMenuConfig.CONFIRMATION_BUTTON_LORE : InternalMenuConfig.CANCEL_BUTTON_LORE);
      itemBuilder.durability(this.confirm ? InternalMenuConfig.CONFIRMATION_BUTTON_DURABILITY : InternalMenuConfig.CANCEL_BUTTON_DURABILITY);
      return itemBuilder.build();
   }

   public void clicked(Player player, ClickType clickType) {
      if (this.confirm) {
         Button.playSuccess(player);
      } else {
         Button.playFail(player);
      }

      if (this.closeAfterResponse) {
         this.menu.setClosedByMenu(true);
         player.closeInventory();
      }

      this.callback.callback(this.confirm);
   }

   public ConfirmationButton(boolean confirm, TypeCallback<Boolean> callback, boolean closeAfterResponse, Menu menu) {
      this.confirm = confirm;
      this.callback = callback;
      this.closeAfterResponse = closeAfterResponse;
      this.menu = menu;
   }
}
