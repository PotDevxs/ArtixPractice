package dev.artixdev.practice.menus;

import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ModernMenuStyle;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Settings Menu – estilo moderno: tooltips com título | descrição | estado atual | "Click to change."
 */
public class SettingsMenu extends PaginatedMenu {

   @Override
   public String getPrePaginatedTitle(Player player) {
      return Messages.get("MENU.SETTINGS_TITLE");
   }

   @Override
   public Map<Integer, Button> getAllPagesButtons(Player player) {
      Map<Integer, Button> buttons = new java.util.HashMap<>();
      addSettingsButtons(buttons, player);
      return buttons;
   }

   private void addSettingsButtons(Map<Integer, Button> buttons, Player player) {
      buttons.put(0, new SettingButton("Ping Range",
         "Would you like to enable ping difference limit",
         "between the players you queue with?",
         "Unrestricted", XMaterial.COMPASS));
      buttons.put(1, new SettingButton("Allow Spectators",
         "Allow other players to spectate your",
         "matches when enabled.",
         "Enabled", XMaterial.ENDER_EYE));
      buttons.put(2, new SettingButton("Match Requests",
         "Receive duel and party requests from",
         "other players.",
         "Enabled", XMaterial.PAPER));
      buttons.put(3, new SettingButton("Private Messages",
         "Receive private messages from",
         "players who are not in your party.",
         "Enabled", XMaterial.WRITABLE_BOOK));
      buttons.put(4, new SettingButton("Scoreboard",
         "Show or hide the scoreboard",
         "during matches and in lobby.",
         "Visible", XMaterial.OAK_SIGN));
      buttons.put(5, new SettingButton("Kill Effects",
         "Show your selected kill effect when",
         "you eliminate an opponent.",
         "Enabled", XMaterial.BLAZE_POWDER));
   }

   @Override
   public int getSize() {
      return 45;
   }

   private static class SettingButton extends Button {
      private final String title;
      private final String line1;
      private final String line2;
      private final String currentValue;
      private final XMaterial icon;

      SettingButton(String title, String line1, String line2, String currentValue, XMaterial icon) {
         this.title = title;
         this.line1 = line1;
         this.line2 = line2;
         this.currentValue = currentValue;
         this.icon = icon;
      }

      @Override
      public ItemStack getButtonItem(Player player) {
         List<String> lore = ModernMenuStyle.tooltip(title,
            new String[]{line1, line2},
            currentValue,
            "Click to change.");
         return new ItemBuilder(icon)
            .name(ChatUtils.colorize("&c&l" + title))
            .lore(lore)
            .build();
      }

      @Override
      public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
         player.sendMessage(Messages.get("MENU.SETTINGS_TOGGLE_NOT_BOUND", "setting", title));
      }
   }
}
