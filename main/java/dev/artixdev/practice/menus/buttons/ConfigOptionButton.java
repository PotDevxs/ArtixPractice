package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

public class ConfigOptionButton extends Button {
    private final boolean enabled;

    public ConfigOptionButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for config option button item
        // This would typically show specific configuration options
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        // Implementation for config option button click
        // This would typically toggle specific configuration settings
    }

    public boolean isEnabled() {
        return enabled;
    }
}
