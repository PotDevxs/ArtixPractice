package dev.artixdev.api.practice.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.menu.bukkit.MenuListener;
import dev.artixdev.api.practice.menu.bukkit.MenuUpdateTask;
import dev.artixdev.api.practice.menu.util.CC;

public class MenuHandler {
   private static MenuHandler instance;
   private final Map<UUID, Menu> openedMenus = new HashMap();
   private final JavaPlugin plugin;

   public void init() {
      instance = this;
      Bukkit.getPluginManager().registerEvents(new MenuListener(this), this.plugin);
      Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, new MenuUpdateTask(this), 20L, 20L);
   }

   public Menu getByPlayer(Player player) {
      return (Menu)this.openedMenus.get(player.getUniqueId());
   }

   public Menu getByPlayer(UUID player) {
      return (Menu)this.openedMenus.get(player);
   }

   public void openMenu(Menu menu, Player player) {
      try {
         menu.setButtons(menu.getButtons(player));
         String title = CC.translate(menu.getTitle(player));
         int oldSize = menu.getSize() == -1 ? menu.size(menu.getButtons()) : menu.getSize();
         if (menu.isBordered()) {
            this.addBorderButtons(menu, oldSize);
         } else if (menu.isPlaceholder()) {
            for(int index = 0; index < oldSize; ++index) {
               menu.getButtons().computeIfAbsent(index, (k) -> {
                  return menu.getPlaceholderButton();
               });
            }
         }

         Inventory inventory = null;
         int size = menu.isBordered() ? menu.size(menu.getButtons()) : (menu.getSize() == -1 ? menu.size(menu.getButtons()) : menu.getSize());
         boolean update = false;
         if (title.length() > 32) {
            title = title.substring(0, 32);
         }

         Menu previousMenu = (Menu)this.openedMenus.get(player.getUniqueId());
         if (previousMenu != null && player.getOpenInventory() != null) {
            int previousSize = player.getOpenInventory().getTopInventory().getSize();
            if (previousSize == size && title.equals(CC.translate(player.getOpenInventory().getTitle()))) {
               inventory = player.getOpenInventory().getTopInventory();
               update = true;
            } else {
               previousMenu.onClose(player);
               previousMenu.setClosedByMenu(true);
               this.openedMenus.remove(player.getUniqueId());
            }
         }

         if (inventory == null) {
            inventory = Bukkit.createInventory(player, size, title);
         }

         if (update) {
            Inventory temporaryInventory = Bukkit.createInventory(player, inventory.getSize(), title);
            Iterator var16 = menu.getButtons().entrySet().iterator();

            while(var16.hasNext()) {
               Entry<Integer, Button> buttonEntry = (Entry)var16.next();
               temporaryInventory.setItem((Integer)buttonEntry.getKey(), ((Button)buttonEntry.getValue()).getButtonItem(player));
            }

            inventory.setContents(temporaryInventory.getContents());
            player.updateInventory();
         } else {
            Iterator var14 = menu.getButtons().entrySet().iterator();

            while(var14.hasNext()) {
               Entry<Integer, Button> buttonEntry = (Entry)var14.next();
               inventory.setItem((Integer)buttonEntry.getKey(), ((Button)buttonEntry.getValue()).getButtonItem(player));
            }

            player.openInventory(inventory);
         }

         this.openedMenus.put(player.getUniqueId(), menu);
         menu.onOpen(player);
      } catch (Exception e) {
         this.getOpenedMenus().remove(player.getUniqueId());
         player.sendMessage(CC.translate("&cThere was an internal exception opening your menu!"));
         e.printStackTrace();
      }

   }

   public void addBorderButtons(Menu menu, int size) {
      Map<Integer, Button> buttons = menu.getButtons();

      int number;
      for(number = 0; number < 9; ++number) {
         buttons.putIfAbsent(number, menu.getBorderButton());
      }

      menu.getButtons().putIfAbsent(9, menu.getBorderButton());
      menu.getButtons().putIfAbsent(17, menu.getBorderButton());
      if (size >= 27) {
         menu.getButtons().putIfAbsent(18, menu.getBorderButton());
         menu.getButtons().putIfAbsent(26, menu.getBorderButton());
      }

      if (size >= 36) {
         menu.getButtons().putIfAbsent(27, menu.getBorderButton());
         menu.getButtons().putIfAbsent(35, menu.getBorderButton());
      }

      if (size >= 45) {
         menu.getButtons().putIfAbsent(36, menu.getBorderButton());
         menu.getButtons().putIfAbsent(44, menu.getBorderButton());
      }

      number = size >= 45 ? 45 : (size >= 36 ? 36 : (size >= 27 ? 27 : 18));

      for(int i = number; i <= number + 8; ++i) {
         menu.getButtons().putIfAbsent(i, menu.getBorderButton());
      }

   }

   public Map<UUID, Menu> getOpenedMenus() {
      return this.openedMenus;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public MenuHandler(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public static MenuHandler getInstance() {
      return instance;
   }
}
