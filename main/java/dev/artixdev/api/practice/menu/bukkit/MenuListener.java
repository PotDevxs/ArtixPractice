package dev.artixdev.api.practice.menu.bukkit;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.api.practice.menu.util.CC;
import dev.artixdev.api.practice.menu.util.TtlArrayList;

public class MenuListener implements Listener {
   private final List<UUID> cooldown;
   private final MenuHandler instance;

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   public void onButtonPress(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      Menu openMenu = (Menu)this.instance.getOpenedMenus().get(player.getUniqueId());
      if (openMenu != null) {
         if (event.getSlot() != event.getRawSlot()) {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
               event.setCancelled(true);
            }

         } else {
            if (openMenu.getButtons().containsKey(event.getSlot())) {
               Button button = (Button)openMenu.getButtons().get(event.getSlot());
               boolean cancel = button.shouldCancel(player, event.getClick());
               if (cancel || event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT) {
                  event.setCancelled(cancel);
               } else {
                  event.setCancelled(true);
                  if (event.getCurrentItem() != null) {
                     player.getInventory().addItem(new ItemStack[]{event.getCurrentItem()});
                  }
               }

               if (this.cooldown.contains(player.getUniqueId())) {
                  player.sendMessage(CC.translate("&c&lDo not spam the menu!"));
                  return;
               }

               button.clicked(player, event.getClick());
               button.clicked(player, event.getSlot(), event.getClick(), event.getHotbarButton());
               this.cooldown.add(player.getUniqueId());
               if (this.instance.getOpenedMenus().containsKey(player.getUniqueId())) {
                  Menu newMenu = (Menu)this.instance.getOpenedMenus().get(player.getUniqueId());
                  if (newMenu == openMenu) {
                     boolean buttonUpdate = button.shouldUpdate(player, event.getClick()) || newMenu.isUpdateAfterClick();
                     if (buttonUpdate) {
                        openMenu.setClosedByMenu(true);
                        this.instance.openMenu(openMenu, player);
                     }
                  }
               } else if (button.shouldUpdate(player, event.getClick())) {
                  openMenu.setClosedByMenu(true);
                  this.instance.openMenu(openMenu, player);
               }

               if (event.isCancelled()) {
                  Bukkit.getScheduler().runTaskLater(this.instance.getPlugin(), player::updateInventory, 1L);
               }
            } else {
               if (event.getCurrentItem() != null) {
                  event.setCancelled(true);
               }

               if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                  event.setCancelled(true);
               }
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onInventoryClose(InventoryCloseEvent event) {
      Player player = (Player)event.getPlayer();
      Menu openMenu = (Menu)this.instance.getOpenedMenus().get(player.getUniqueId());
      if (openMenu != null) {
         openMenu.onClose(player);
         this.instance.getOpenedMenus().remove(player.getUniqueId());
      }

   }

   public MenuListener(MenuHandler instance) {
      this.cooldown = new TtlArrayList(TimeUnit.MILLISECONDS, 5L);
      this.instance = instance;
   }
}
