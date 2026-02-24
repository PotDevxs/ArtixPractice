package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.ConfigMenu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class ConfigTypeButton extends Button {
    private final boolean enabled;

    public ConfigTypeButton(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.BOOK).name(ChatUtils.colorize("&6Config Type"))
            .lore(Collections.singletonList(ChatUtils.colorize(enabled ? "&7Click to open" : "&cDisabled"))).build();
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
