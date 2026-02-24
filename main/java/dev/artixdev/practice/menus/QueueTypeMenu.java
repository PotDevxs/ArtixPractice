package dev.artixdev.practice.menus;

import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Queue Type Menu
 * Menu for selecting queue types (ranked or unranked kits).
 */
public class QueueTypeMenu extends Menu {

   private final boolean isRanked;

   public QueueTypeMenu(boolean isRanked) {
      this.isRanked = isRanked;
      this.setPlaceholder(true);
   }

   @Override
   public int getSize() {
      return QueueMenus.QUEUE_TYPE_SIZE;
   }

   @Override
   public String getTitle(Player player) {
      return QueueMenus.QUEUE_TYPE_TITLE;
   }

   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      if (isRanked) {
         addRankedQueueButtons(buttons, player);
      } else {
         addUnrankedQueueButtons(buttons, player);
      }
      return buttons;
   }

   private void addRankedQueueButtons(Map<Integer, Button> buttons, Player player) {
      java.util.List<KitType> kits = Main.getInstance().getKitManager().getKits();
      int slot = 0;
      for (KitType kitType : kits) {
         if (kitType == KitType.CUSTOM) continue;
         if (slot >= QueueMenus.QUEUE_TYPE_SIZE) break;
         buttons.put(slot++, new QueueTypeButton(kitType, true));
      }
   }

   private void addUnrankedQueueButtons(Map<Integer, Button> buttons, Player player) {
      java.util.List<KitType> kits = Main.getInstance().getKitManager().getKits();
      int slot = 0;
      for (KitType kitType : kits) {
         if (kitType == KitType.CUSTOM) continue;
         if (slot >= QueueMenus.QUEUE_TYPE_SIZE) break;
         buttons.put(slot++, new QueueTypeButton(kitType, false));
      }
   }

   public boolean isRanked() {
      return isRanked;
   }

   private static class QueueTypeButton extends Button {
      private final KitType kitType;
      private final boolean ranked;

      QueueTypeButton(KitType kitType, boolean ranked) {
         this.kitType = kitType;
         this.ranked = ranked;
      }

      @Override
      public ItemStack getButtonItem(Player player) {
         ItemStack base = kitType.getIcon() != null ? kitType.getIcon().clone() : new ItemStack(org.bukkit.Material.DIAMOND_SWORD);
         org.bukkit.inventory.meta.ItemMeta meta = base.getItemMeta();
         if (meta != null) {
            meta.setDisplayName(ChatUtils.colorize("&f" + kitType.getDisplayName()));
            java.util.List<String> lore = new java.util.ArrayList<>();
            lore.add(ChatUtils.colorize("&7" + (ranked ? "Ranked" : "Unranked") + " queue"));
            meta.setLore(lore);
            base.setItemMeta(meta);
         }
         return base;
      }

      @Override
      public void clicked(Player player, ClickType clickType) {
         EventType eventType = ranked ? EventType.RANKED : EventType.UNRANKED;
         if (Main.getInstance().getQueueManager().addPlayerToQueue(player, eventType, kitType)) {
            player.sendMessage(ChatUtils.colorize("&aJoined " + (ranked ? "ranked" : "unranked") + " queue: &f" + kitType.getDisplayName()));
            player.closeInventory();
         }
      }
   }
}
