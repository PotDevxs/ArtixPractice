package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Config Menu
 * Menu for configuration management
 */
public class ConfigMenu extends Menu {

   private final ConfigType configType;

   public enum ConfigType {
      GENERAL,
      ADVANCED,
      MENUS,
      ARENAS,
      KITS
   }

   public ConfigMenu(ConfigType configType) {
      this.configType = configType;
      this.setPlaceholder(true);
   }

   @Override
   public String getTitle(Player player) {
      return ChatUtils.colorize("&6&lConfig Menu - " + configType.name());
   }

   @Override
   public Map<Integer, Button> getButtons(Player player) {
      Map<Integer, Button> buttons = new HashMap<>();
      switch (configType) {
         case GENERAL:
            buttons.put(11, new ConfigOptionPlaceholderButton("&eSpawn Location", "&7Set main spawn"));
            buttons.put(13, new ConfigOptionPlaceholderButton("&eLobby", "&7Lobby settings"));
            buttons.put(15, new ConfigOptionPlaceholderButton("&eMessages", "&7Message settings"));
            break;
         case ADVANCED:
            buttons.put(11, new ConfigOptionPlaceholderButton("&ePerformance", "&7Performance options"));
            buttons.put(13, new ConfigOptionPlaceholderButton("&eDatabase", "&7Database settings"));
            break;
         case MENUS:
            buttons.put(11, new ConfigOptionPlaceholderButton("&eMenu Layout", "&7Edit menu layout"));
            buttons.put(13, new ConfigOptionPlaceholderButton("&eHotbar", "&7Hotbar items"));
            break;
         case ARENAS:
            buttons.put(11, new ConfigOptionPlaceholderButton("&eArena List", "&7Manage arenas"));
            buttons.put(13, new ConfigOptionPlaceholderButton("&eCreate Arena", "&7Create new arena"));
            break;
         case KITS:
            buttons.put(11, new ConfigOptionPlaceholderButton("&eKit Editor", "&7Edit kits"));
            buttons.put(13, new ConfigOptionPlaceholderButton("&eLoadouts", "&7Default loadouts"));
            break;
         default:
            break;
      }
      buttons.put(49, new BackConfigButton());
      return buttons;
   }
   
   @Override
   public int getSize() {
      return 54; // 6 rows
   }
   
   public ConfigType getConfigType() {
      return configType;
   }

   private static class ConfigOptionPlaceholderButton extends Button {
      private final String name;
      private final String loreLine;

      ConfigOptionPlaceholderButton(String name, String loreLine) {
         this.name = name;
         this.loreLine = loreLine;
      }

      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.PAPER).name(ChatUtils.colorize(name)).lore(java.util.Collections.singletonList(ChatUtils.colorize(loreLine))).build();
      }

      @Override
      public void clicked(Player player, ClickType clickType) {
         player.sendMessage(ChatUtils.colorize("&7Config option: " + name));
      }
   }

   private static class BackConfigButton extends Button {
      @Override
      public ItemStack getButtonItem(Player player) {
         return new ItemBuilder(XMaterial.ARROW).name(ChatUtils.colorize("&cBack")).build();
      }

      @Override
      public void clicked(Player player, ClickType clickType) {
         player.closeInventory();
      }
   }
}
