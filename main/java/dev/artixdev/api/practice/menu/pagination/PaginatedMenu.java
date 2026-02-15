package dev.artixdev.api.practice.menu.pagination;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;

public abstract class PaginatedMenu extends Menu {
   private int page = 1;

   public PaginatedMenu() {
      this.setUpdateAfterClick(false);
   }

   public String getTitle(Player player) {
      return this.getPrePaginatedTitle(player) + (this.page > 1 ? " &7(Page " + this.page + ")" : "");
   }

   public final void modPage(Player player, int mod) {
      this.page += mod;
      this.getButtons().clear();
      MenuHandler.getInstance().openMenu(this, player);
   }

   public final int getPages(Player player) {
      int buttonAmount = this.getAllPagesButtons(player).size();
      return buttonAmount == 0 ? 1 : (int)Math.ceil((double)buttonAmount / (double)this.getMaxItemsPerPage(player));
   }

   public Map<Integer, Button> getButtons(Player player) {
      int minIndex = (this.page - 1) * this.getMaxItemsPerPage(player);
      int maxIndex = this.page * this.getMaxItemsPerPage(player);
      Map<Integer, Button> buttons = new HashMap();
      if (this.getPages(player) > 1 && this.page > 1) {
         buttons.put(0, new PageButton(-1, this));
      }

      if (this.getPages(player) > 1 && this.page < this.getPages(player)) {
         buttons.put(8, new PageButton(1, this));
      }

      Map global;
      int added;
      if (this.isBordered()) {
         global = this.getAllPagesButtons(player);
         int index = 1;
         added = 0;
         int currentSlot = 0;
         Iterator var9 = global.entrySet().iterator();

         while(var9.hasNext()) {
            Entry<Integer, Button> entry = (Entry)var9.next();
            int current = index++;
            switch(currentSlot) {
            case 16:
            case 25:
            case 34:
            case 43:
            case 52:
               index += 2;
               added += 2;
               current += 2;
            default:
               if (current > minIndex && current <= maxIndex + added) {
                  current -= (int)((double)this.getMaxItemsPerPage(player) * (double)(this.page - 1)) - 9;
                  currentSlot = current;
                  buttons.put(current, entry.getValue());
               }
            }
         }
      } else {
         Iterator var12 = this.getAllPagesButtons(player).entrySet().iterator();

         while(var12.hasNext()) {
            Entry<Integer, Button> entry = (Entry)var12.next();
            added = (Integer)entry.getKey();
            if (added >= minIndex && added < maxIndex) {
               added -= (int)((double)this.getMaxItemsPerPage(player) * (double)(this.page - 1)) - 9;
               buttons.put(added, entry.getValue());
            }
         }
      }

      global = this.getGlobalButtons(player);
      if (global != null) {
         buttons.putAll(global);
      }

      return buttons;
   }

   public int getMaxItemsPerPage(Player player) {
      return this.isBordered() ? (this.getSize() == 54 ? 37 : 28) : 54;
   }

   public Map<Integer, Button> getGlobalButtons(Player player) {
      return null;
   }

   public abstract String getPrePaginatedTitle(Player var1);

   public abstract Map<Integer, Button> getAllPagesButtons(Player var1);

   public int getPage() {
      return this.page;
   }
}
