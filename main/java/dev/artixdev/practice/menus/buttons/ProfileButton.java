package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;

public class ProfileButton extends Button {

    public ProfileButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for profile button item
        // This would typically show player profile information
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        // Implementation for profile button click
        // This would typically open a profile menu
    }
}
