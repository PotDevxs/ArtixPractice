package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.menus.buttons.PartyMemberButton;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        Map<Integer, Button> buttons = new HashMap<>();
        if (team == null) return buttons;
        int slot = 0;
        for (UUID memberId : team.getMembers()) {
            if (slot >= 45) break;
            buttons.put(slot++, new PartyMemberButton(memberId, team));
        }
        buttons.put(40, new OpenPartyPaginatedButton(team));
        return buttons;
    }

    private static class OpenPartyPaginatedButton extends Button {
        private final Team team;
        OpenPartyPaginatedButton(Team team) { this.team = team; }
        @Override
        public org.bukkit.inventory.ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.BOOK).name(ChatUtils.colorize("&eView all members")).build();
        }
        @Override
        public void clicked(Player player, org.bukkit.event.inventory.ClickType clickType) {
            MenuHandler.getInstance().openMenu(new PartyManagePaginatedMenu(team), player);
        }
    }
}
