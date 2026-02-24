package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.menus.buttons.PartyMemberButton;
import dev.artixdev.practice.models.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        Map<Integer, Button> buttons = new HashMap<>();
        if (team == null) return buttons;
        int slot = 0;
        for (UUID memberId : team.getMembers()) {
            buttons.put(slot++, new PartyMemberButton(memberId, team));
        }
        return buttons;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return PartyMenus.PARTY_MANAGE_TITLE;
    }
}
