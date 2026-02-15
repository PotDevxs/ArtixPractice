package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.ConfigMenu;

public class ConfigMenuButton extends Button {
    private final boolean enabled;

    public ConfigMenuButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for config menu button item
        // This would typically show a menu icon
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!enabled) {
            ConfigMenu configMenu = new ConfigMenu(ConfigMenu.ConfigType.GENERAL);
            MenuHandler.getInstance().openMenu(configMenu, player);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
