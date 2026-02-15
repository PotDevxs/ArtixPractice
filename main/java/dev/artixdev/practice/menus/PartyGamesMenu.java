package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.PartyMenus;

import java.util.Map;

public class PartyGamesMenu extends PaginatedMenu {
    private static final String[] MENU_CONSTANTS = {"PartyGamesMenu"};
    public static final boolean DEBUG_MODE = false;
    private static final String[] TITLE_CONSTANTS = {"&bParty Games"};
    public static final int MENU_VERSION = 1;

    public PartyGamesMenu() {
        super();
        this.setAutoUpdate(true);
        this.setUpdateAfterClick(true);
        this.setBordered(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&bParty Games";
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for party games menu
        return new java.util.HashMap<>();
    }

    static {
        TITLE_CONSTANTS[0] = "&bParty Games";
    }
}
