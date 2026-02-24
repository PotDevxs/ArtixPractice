package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.HashMap;
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
        Map<Integer, Button> buttons = new HashMap<>();
        if (player == null) return buttons;
        Team team = Main.getInstance() != null && Main.getInstance().getPartyManager() != null
            ? Main.getInstance().getPartyManager().getPlayerParty(player.getUniqueId()) : null;
        if (team == null) {
            buttons.put(13, new PartyGamesNoPartyButton());
            return buttons;
        }
        buttons.put(11, new PartyGameModeButton("&a2v2", "&7Queue 2v2 with your party"));
        buttons.put(13, new PartyGameModeButton("&eFFA", "&7Free-for-all in arena"));
        buttons.put(15, new PartyGameModeButton("&bPractice", "&7Spar with party members"));
        return buttons;
    }

    private static class PartyGamesNoPartyButton extends Button {
        @Override
        public org.bukkit.inventory.ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.BARRIER).name(ChatUtils.colorize("&cNo Party"))
                .lore(java.util.Collections.singletonList(ChatUtils.colorize("&7Create or join a party to play party games."))).build();
        }
    }

    private static class PartyGameModeButton extends Button {
        private final String name;
        private final String loreLine;
        PartyGameModeButton(String name, String loreLine) {
            this.name = name;
            this.loreLine = loreLine;
        }
        @Override
        public org.bukkit.inventory.ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.DIAMOND_SWORD).name(ChatUtils.colorize(name))
                .lore(java.util.Collections.singletonList(ChatUtils.colorize(loreLine))).build();
        }
        @Override
        public void clicked(Player player, org.bukkit.event.inventory.ClickType clickType) {
            player.sendMessage(ChatUtils.colorize("&7Party game: " + name + " &7(configure queue/arena)."));
        }
    }

    static {
        TITLE_CONSTANTS[0] = "&bParty Games";
    }
}
