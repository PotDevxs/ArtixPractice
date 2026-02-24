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
        return new dev.artixdev.practice.utils.ItemBuilder(dev.artixdev.libs.com.cryptomorin.xseries.XMaterial.PLAYER_HEAD)
            .name(dev.artixdev.practice.utils.ChatUtils.colorize("&eProfile Menu"))
            .lore(dev.artixdev.practice.utils.ChatUtils.colorize("&7Click to open"))
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        ProfileMenu profileMenu = new ProfileMenu();
        MenuHandler.getInstance().openMenu(profileMenu, player);
    }
}
