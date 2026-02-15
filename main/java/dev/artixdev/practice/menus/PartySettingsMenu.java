package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.PartyMenus;

import java.util.Map;

public class PartySettingsMenu extends Menu {
    public static final int MENU_VERSION = 1;
    public static final boolean DEBUG_MODE = false;

    public PartySettingsMenu() {
        super();
        this.setBordered(true);
    }

    @Override
    public String getTitle(Player player) {
        return PartyMenus.PARTY_SETTINGS_MENU_TITLE;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for party settings menu
        return new java.util.HashMap<>();
    }
}
