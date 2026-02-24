package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public class ProfileButton extends Button {

    public ProfileButton() {
        super();
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.PLAYER_HEAD)
            .name(ChatUtils.colorize("&e" + (player != null ? player.getName() : "Profile")))
            .lore(ChatUtils.colorize("&7Click to open your profile"))
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        dev.artixdev.practice.menus.ProfileMenu menu = new dev.artixdev.practice.menus.ProfileMenu();
        dev.artixdev.api.practice.menu.MenuHandler.getInstance().openMenu(menu, player);
    }
}
