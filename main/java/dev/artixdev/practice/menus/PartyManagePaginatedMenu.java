package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.models.Team;

import java.util.Map;

public class PartyManagePaginatedMenu extends PaginatedMenu {
    public static final boolean DEBUG_MODE = false;
    public static final int MENU_VERSION = 1;
    private final Team team;

    public PartyManagePaginatedMenu(Team team) {
        this.team = team;
        this.setBordered(true);
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for paginated party management menu
        return new java.util.HashMap<>();
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return PartyMenus.PARTY_MANAGE_TITLE;
    }
}
