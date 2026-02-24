package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class ConfigButton extends Button {
    private final boolean enabled;

    public ConfigButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.PAPER).name(ChatUtils.colorize("&eConfig Option"))
            .lore(Collections.singletonList(ChatUtils.colorize(enabled ? "&aEnabled" : "&cDisabled"))).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(ChatUtils.colorize("&7Config: " + (enabled ? "&aOn" : "&cOff")));
    }

    public boolean isEnabled() {
        return enabled;
    }
}
