package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.models.Team;

import java.util.Map;

public class PartyManageMenu extends Menu {
    public static final int MENU_VERSION = 1;
    public static final boolean DEBUG_MODE = false;
    private final Team team;

    public PartyManageMenu(Team team) {
        this.team = team;
        this.setPlaceholder(true);
    }

    @Override
    public int getSize() {
        return PartyMenus.PARTY_GAMES_SIZE;
    }

    @Override
    public String getTitle(Player player) {
        return PartyMenus.PARTY_GAMES_TITLE;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for party management menu
        return new java.util.HashMap<>();
    }
}
