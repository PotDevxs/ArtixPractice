package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Config Menu
 * Menu for configuration management
 */
public class ConfigMenu extends Menu {
   
   private final ConfigType configType;
   
   /**
    * Config Type enum
    */
   public enum ConfigType {
      GENERAL,
      ADVANCED,
      MENUS,
      ARENAS,
      KITS
   }
   
   /**
    * Constructor
    * @param configType the config type
    */
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
      
      // TODO: Add buttons for configuration options based on configType
      
      return buttons;
   }
   
   @Override
   public int getSize() {
      return 54; // 6 rows
   }
   
   /**
    * Get config type
    * @return config type
    */
   public ConfigType getConfigType() {
      return configType;
   }
}
