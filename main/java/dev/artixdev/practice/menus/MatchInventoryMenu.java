package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Match;

import java.util.Map;

public class MatchInventoryMenu extends Menu {
    private final PlayerProfile player1Profile;
    private static final String[] MENU_CONSTANTS = {"MatchInventoryMenu"};
    public static final boolean DEBUG_MODE = false;
    private static final String[] TITLE_CONSTANTS = {"Match Inventory"};
    public static final int MENU_VERSION = 1;
    private final PlayerProfile player2Profile;

    static {
        TITLE_CONSTANTS[0] = "Match Inventory";
    }

    public MatchInventoryMenu(PlayerProfile player1Profile, PlayerProfile player2Profile) {
        this.player1Profile = player1Profile;
        this.player2Profile = player2Profile;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        // This method was obfuscated and needs implementation
        // Placeholder implementation for match inventory menu buttons
        // Typically shows player inventories, health, food, etc.
        return new java.util.HashMap<>();
    }

    @Override
    public String getTitle(Player player) {
        String title = MatchMenus.MATCH_INVENTORY_TITLE;
        String player1Name = this.player1Profile.getUsername();
        return title.replace("%player1%", player1Name);
    }
}
