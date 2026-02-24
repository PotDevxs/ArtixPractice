package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class MenuButton extends Button {

    private final boolean isEnabled;
    private final boolean isVisible;

    public MenuButton(boolean isEnabled, boolean isVisible) {
        this.isEnabled = isEnabled;
        this.isVisible = isVisible;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!isEnabled) return;
        player.sendMessage(ChatUtils.colorize("&7Menu option clicked."));
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (!isVisible) return new ItemStack(org.bukkit.Material.AIR);
        return new ItemBuilder(XMaterial.PAPER).name(ChatUtils.colorize("&fMenu"))
            .lore(Collections.singletonList(ChatUtils.colorize(isEnabled ? "&aEnabled" : "&cDisabled"))).build();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isVisible() {
        return isVisible;
    }
}