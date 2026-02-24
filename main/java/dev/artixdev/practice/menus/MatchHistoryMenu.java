package dev.artixdev.practice.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.MatchMenus;
import dev.artixdev.practice.models.MatchHistory;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.menus.buttons.MatchHistoryButton;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.*;

public class MatchHistoryMenu extends PaginatedMenu {
    private static final String[] MENU_CONSTANTS = {"MatchHistoryMenu"};
    public static final int MENU_VERSION = 1;
    public static final boolean DEBUG_MODE = false;
    private PlayerProfile playerProfile;
    private static final String[] TITLE_CONSTANTS = {"&bMatch History"};

    static {
        TITLE_CONSTANTS[0] = "&bMatch History";
    }

    public MatchHistoryMenu(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&bMatch History";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        if (this.playerProfile == null) {
            this.playerProfile = Main.getInstance().getPlayerManager().getPlayerProfile(player.getUniqueId());
        }

        List<MatchHistory> allMatches = getMatchHistoryFromProfile(this.playerProfile);

        if (allMatches.isEmpty()) {
            this.setPlaceholder(true);
            buttons.put(MatchMenus.NO_MATCH_HISTORY_FOUND_SLOT, new NoMatchHistoryButton());
            return buttons;
        } else {
            this.setBordered(true);
            Collections.sort(allMatches);
            Collections.reverse(allMatches);
            
            int index = allMatches.size() - 1;
            for (MatchHistory match : allMatches) {
                buttons.put(buttons.size(), new MatchHistoryButton(this.playerProfile, match, index--));
            }
        }

        return buttons;
    }

    private static List<MatchHistory> getMatchHistoryFromProfile(PlayerProfile profile) {
        if (profile == null) return new ArrayList<>();
        Object stats = profile.getStatistics();
        if (stats instanceof dev.artixdev.practice.models.StatsProfile) {
            dev.artixdev.practice.models.StatsProfile sp = (dev.artixdev.practice.models.StatsProfile) stats;
            List<MatchHistory> out = new ArrayList<>();
            out.addAll(sp.getRankedMatchHistory());
            out.addAll(sp.getUnrankedMatchHistory());
            return out;
        }
        if (stats instanceof dev.artixdev.practice.models.PlayerStatistics) {
            return new ArrayList<>(((dev.artixdev.practice.models.PlayerStatistics) stats).getMatchHistory());
        }
        return new ArrayList<>();
    }

    @Override
    public int getSize() {
        if (this.isPlaceholder()) {
            return MatchMenus.MATCH_HISTORY_SIZE;
        }
        return -1;
    }

    private class NoMatchHistoryButton extends Button {
        public static final int BUTTON_SLOT = 22;
        public static final boolean DEBUG_MODE = false;

        public NoMatchHistoryButton() {
            super();
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            UUID playerUuid = player.getUniqueId();
            UUID profileUuid = Main.getInstance().getPlayerManager().getPlayerProfile(playerUuid).getUniqueId();
            
            boolean isOwnProfile = playerUuid.equals(profileUuid);
            
            ItemBuilder itemBuilder = new ItemBuilder(XMaterial.matchXMaterial(MatchMenus.MATCH_HISTORY_NO_MATCHES_ITEM).orElse(XMaterial.REDSTONE_BLOCK));
            itemBuilder.name(ChatUtils.colorize(MatchMenus.MATCH_HISTORY_NO_MATCHES_NAME));
            
            List<String> lore;
            if (!isOwnProfile) {
                lore = new ArrayList<>();
                for (String line : MatchMenus.MATCH_HISTORY_NO_MATCHES_OTHER_LORE) {
                    lore.add(ChatUtils.colorize(line.replace("<player>", playerProfile != null ? playerProfile.getName() : "Unknown")));
                }
            } else {
                lore = new ArrayList<>();
                for (String line : MatchMenus.MATCH_HISTORY_NO_MATCHES_SELF_LORE) {
                    lore.add(ChatUtils.colorize(line));
                }
            }
            
            itemBuilder.lore(lore);
            
            return itemBuilder.build();
        }
    }
}