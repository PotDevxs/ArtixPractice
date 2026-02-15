package dev.artixdev.api.practice.menu.pagination;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.buttons.BackButton;

public class ViewAllPagesMenu extends Menu {
   public PaginatedMenu menu;

   public ViewAllPagesMenu(PaginatedMenu menu) {
      this.menu = menu;
   }

   public String getTitle(Player player) {
      return "&cJump to page";
   }

   public Map<Integer, Button> getButtons(Player player) {
      HashMap<Integer, Button> buttons = new HashMap();
      buttons.put(0, new BackButton(this.menu));
      int index = 10;

      for(int i = 1; i <= this.menu.getPages(player); ++i) {
         buttons.put(index++, new JumpToPageButton(i, this.menu, this.menu.getPage() == i));
         if ((index - 8) % 9 == 0) {
            index += 2;
         }
      }

      return buttons;
   }

   public boolean isAutoUpdate() {
      return true;
   }

   public PaginatedMenu getMenu() {
      return this.menu;
   }
}
