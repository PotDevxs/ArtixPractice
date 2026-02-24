package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class ConfigOptionButton extends Button {
    private final boolean enabled;
    private final String optionName;
    private final String optionLore;

    public ConfigOptionButton(boolean enabled) {
        this(enabled, "Config Option", enabled ? "&aEnabled" : "&cDisabled");
    }

    public ConfigOptionButton(boolean enabled, String optionName, String optionLore) {
        this.enabled = enabled;
        this.optionName = optionName != null ? optionName : "Option";
        this.optionLore = optionLore != null ? optionLore : (enabled ? "&aOn" : "&cOff");
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        XMaterial mat = enabled ? XMaterial.LIME_DYE : XMaterial.GRAY_DYE;
        if (mat.parseMaterial() == null) mat = XMaterial.PAPER;
        return new ItemBuilder(mat).name(ChatUtils.colorize("&f" + optionName))
            .lore(Collections.singletonList(ChatUtils.colorize(optionLore))).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(ChatUtils.colorize("&7Option: &f" + optionName + " &7(" + (enabled ? "&aOn" : "&cOff") + "&7)"));
    }

    public boolean isEnabled() {
        return enabled;
    }
}
