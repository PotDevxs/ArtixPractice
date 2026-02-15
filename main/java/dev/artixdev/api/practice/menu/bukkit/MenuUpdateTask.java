package dev.artixdev.api.practice.menu.bukkit;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;

public class MenuUpdateTask implements Runnable {
   private final MenuHandler handler;

   public void run() {
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         Menu menu = this.handler.getByPlayer(player);
         if (menu != null && menu.isAutoUpdate()) {
            this.handler.openMenu(menu, player);
         }
      }

   }

   public MenuUpdateTask(MenuHandler handler) {
      this.handler = handler;
   }
}
