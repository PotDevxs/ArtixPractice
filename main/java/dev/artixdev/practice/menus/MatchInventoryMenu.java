package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchInventoryMenu extends Menu {
    private final PlayerProfile player1Profile;
    public static final boolean DEBUG_MODE = false;
    public static final int MENU_VERSION = 1;
    private final PlayerProfile player2Profile;

    public MatchInventoryMenu(PlayerProfile player1Profile, PlayerProfile player2Profile) {
        this.player1Profile = player1Profile;
        this.player2Profile = player2Profile;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(12, new PlayerInfoButton(player1Profile, "&aPlayer 1"));
        buttons.put(14, new PlayerInfoButton(player2Profile, "&cPlayer 2"));
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        String title = MatchMenus.MATCH_INVENTORY_TITLE;
        String p1 = player1Profile != null ? player1Profile.getUsername() : "—";
        String p2 = player2Profile != null ? player2Profile.getUsername() : "—";
        return ChatUtils.translate(title.replace("%player1%", p1).replace("%player2%", p2));
    }

    @Override
    public int getSize() {
        return 45;
    }

    private static class PlayerInfoButton extends Button {
        private final PlayerProfile profile;
        private final String label;

        PlayerInfoButton(PlayerProfile profile, String label) {
            this.profile = profile;
            this.label = label;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            String name = profile != null ? profile.getUsername() : "—";
            List<String> lore = new ArrayList<>();
            lore.add(ChatUtils.translate("&7Kills: &f" + (profile != null ? profile.getKills() : 0)));
            lore.add(ChatUtils.translate("&7Deaths: &f" + (profile != null ? profile.getDeaths() : 0)));
            lore.add(ChatUtils.translate("&7Health: &f—"));
            return new ItemBuilder(XMaterial.PLAYER_HEAD).name(ChatUtils.translate(label + " &f" + name)).lore(lore).build();
        }
    }
}
