package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

public class ConfigButton extends Button {
    private final boolean enabled;

    public ConfigButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for config button item
        // This would typically show configuration options
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        // Implementation for config button click
        // This would typically toggle configuration settings
    }

    public boolean isEnabled() {
        return enabled;
    }
}
