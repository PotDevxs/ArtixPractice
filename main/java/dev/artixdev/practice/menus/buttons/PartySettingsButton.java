package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.menus.PartySettingsMenu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class PartySettingsButton extends Button {
    public static final int BUTTON_VERSION = 1;
    public static final boolean DEBUG_MODE = false;

    @Override
    public void clicked(Player player, ClickType clickType) {
        MenuHandler.getInstance().openMenu(new PartySettingsMenu(), player);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(XMaterial.COMPARATOR)
            .name(ChatUtils.colorize("&eParty Settings"))
            .lore(Collections.singletonList(ChatUtils.colorize("&7Click to open party settings")))
            .build();
    }
}
