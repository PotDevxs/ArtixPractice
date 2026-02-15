package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.ProfileMenu;

public class ProfileMenuButton extends Button {

    public ProfileMenuButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for profile menu button item
        // This would typically show a menu icon
        return null; // Placeholder
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        ProfileMenu profileMenu = new ProfileMenu();
        MenuHandler.getInstance().openMenu(profileMenu, player);
    }
}
