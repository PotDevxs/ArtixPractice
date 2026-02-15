package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

public class MenuButton extends Button {
    
    private final boolean isEnabled;
    private final boolean isVisible;

    public MenuButton(boolean isEnabled, boolean isVisible) {
        this.isEnabled = isEnabled;
        this.isVisible = isVisible;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!isEnabled) {
            return;
        }
        
        // Handle button click logic
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Return the item stack for this button
        return null; // Placeholder
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isVisible() {
        return isVisible;
    }
}