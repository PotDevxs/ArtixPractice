package dev.artixdev.practice.menus.buttons;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.LeaderboardEntry;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.menus.LeaderboardMenu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.StatisticsUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.utils.other.Callback;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardButton extends Button {
    public static final boolean DEBUG_MODE = false;
    private final Callback<LeaderboardEntry> callback;
    private static final String[] BUTTON_CONSTANTS = new String[14];
    private final LeaderboardEntry leaderboardEntry;
    public static final int BUTTON_VERSION = 1;
    private static final String[] BUTTON_MESSAGES = new String[14];

    public LeaderboardButton(LeaderboardEntry leaderboardEntry, Callback<LeaderboardEntry> callback) {
        Preconditions.checkNotNull(leaderboardEntry, "LeaderboardEntry cannot be null");
        this.leaderboardEntry = leaderboardEntry;
        this.callback = callback;
    }

    /** Single-argument constructor for ProfileMenu: opens leaderboard then stats on click. */
    public LeaderboardButton(PlayerProfile profile) {
        this.leaderboardEntry = profile != null ? new LeaderboardEntry(profile, profile.getName()) : null;
        this.callback = null;
    }

    private String formatLeaderboardText(String text) {
        return text == null ? "" : ChatUtils.colorize(text);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (leaderboardEntry == null) return null;
        PlayerProfile profile = leaderboardEntry.getPlayerProfile();
        if (profile == null) return null;
        String name = leaderboardEntry.getPlayerName();
        List<String> lore = new ArrayList<>();
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[8] + ": &f" + profile.getElo()));
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[9] + ": &f" + profile.getWins()));
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[10] + ": &f" + profile.getLosses()));
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[11] + ": &f" + profile.getKills()));
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[12] + ": &f" + profile.getDeaths()));
        lore.add(formatLeaderboardText("&7" + BUTTON_MESSAGES[13] + ": &f" + StatisticsUtils.formatKdr(profile.getKills(), profile.getDeaths())));
        lore.add("");
        lore.add(formatLeaderboardText("&eClick to view profile"));
        return new ItemBuilder(XMaterial.PLAYER_HEAD).name(formatLeaderboardText("&f" + name)).lore(lore).build();
    }

    private String formatLeaderboardText(String format, LeaderboardEntry entry, String text) {
        if (entry == null || entry.getPlayerProfile() == null) return text;
        return text.replace("<player>", entry.getPlayerName())
            .replace("<elo>", String.valueOf(entry.getPlayerProfile().getElo()))
            .replace("<wins>", String.valueOf(entry.getPlayerProfile().getWins()));
    }

    private static String getButtonMessage(int param0, int param1) {
        if (param0 >= 0 && param0 < BUTTON_MESSAGES.length) return BUTTON_MESSAGES[param0];
        return BUTTON_CONSTANTS[0];
    }

    private String formatLeaderboardText(String text, String format) {
        return formatLeaderboardText(text);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (leaderboardEntry == null) return;
        if (callback != null) {
            callback.call(leaderboardEntry);
        } else {
            if (Main.getInstance() != null) {
                MenuHandler.getInstance().openMenu(new LeaderboardMenu(), player);
            }
        }
    }

    static {
        BUTTON_CONSTANTS[0] = "LeaderboardButton";
        BUTTON_CONSTANTS[1] = "Leaderboard";
        BUTTON_CONSTANTS[2] = "Entry";
        BUTTON_CONSTANTS[3] = "Player";
        BUTTON_CONSTANTS[4] = "Rank";
        BUTTON_CONSTANTS[5] = "Value";
        BUTTON_CONSTANTS[6] = "Position";
        BUTTON_CONSTANTS[7] = "Score";
        BUTTON_CONSTANTS[8] = "ELO";
        BUTTON_CONSTANTS[9] = "Wins";
        BUTTON_CONSTANTS[10] = "Losses";
        BUTTON_CONSTANTS[11] = "Kills";
        BUTTON_CONSTANTS[12] = "Deaths";
        BUTTON_CONSTANTS[13] = "KDR";
        
        BUTTON_MESSAGES[0] = "LeaderboardButton";
        BUTTON_MESSAGES[1] = "Leaderboard";
        BUTTON_MESSAGES[2] = "Entry";
        BUTTON_MESSAGES[3] = "Player";
        BUTTON_MESSAGES[4] = "Rank";
        BUTTON_MESSAGES[5] = "Value";
        BUTTON_MESSAGES[6] = "Position";
        BUTTON_MESSAGES[7] = "Score";
        BUTTON_MESSAGES[8] = "ELO";
        BUTTON_MESSAGES[9] = "Wins";
        BUTTON_MESSAGES[10] = "Losses";
        BUTTON_MESSAGES[11] = "Kills";
        BUTTON_MESSAGES[12] = "Deaths";
        BUTTON_MESSAGES[13] = "KDR";
    }
}