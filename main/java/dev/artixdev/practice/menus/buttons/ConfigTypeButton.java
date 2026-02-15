package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.ConfigMenu;

public class ConfigTypeButton extends Button {
    private final boolean enabled;

    public ConfigTypeButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for config type button item
        // This would typically show configuration type options
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!enabled) {
            ConfigMenu configMenu = new ConfigMenu(ConfigMenu.ConfigType.ADVANCED);
            MenuHandler.getInstance().openMenu(configMenu, player);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
