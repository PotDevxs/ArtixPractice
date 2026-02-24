package dev.artixdev.practice.expansions.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Division;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Rank;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * PlaceholderAPI expansion for Artix Practice.
 * Placeholders use the prefix {@code %artix_...%}.
 * Stats: %artix_stats_level%, %artix_stats_globalelo%, %artix_stats_kills%, etc.
 */
public class ArtixExpansions extends PlaceholderExpansion {

    private static final String IDENTIFIER = "artix";
    private static final String AUTHOR = "Faastyzin";
    private static final String VERSION = "22.9";

    @NotNull
    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @NotNull
    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Nullable
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params == null || params.isEmpty()) return null;
        String[] parts = params.split("_", 2);
        if (parts.length == 0) return null;
        String category = parts[0].toLowerCase();
        String key = parts.length > 1 ? parts[1].toLowerCase() : "";
        switch (category) {
            case "stats":
                return handleStatsPlaceholders(player, key);
            case "match":
                return handleMatchPlaceholders(player, key);
            default:
                return null;
        }
    }

    /**
     * Handles stats-related placeholders (e.g. %artix_stats_level%, %artix_stats_globalelo%).
     */
    @Nullable
    public String handleStatsPlaceholders(Player player, String key) {
        if (player == null) return null;
        Main main = Main.getInstance();
        PlayerProfile profile = main.getPlayerManager() != null ? main.getPlayerManager().getPlayerProfile(player.getUniqueId()) : null;
        if (profile == null) {
            return getDefaultStatsValue(key);
        }

        switch (key) {
            case "level": {
                int level = profile.getLevel();
                if (main.getConfigManager() != null && main.getConfigManager().getLevelsConfig() != null
                    && main.getConfigManager().getLevelsConfig().isUseXpForLevel()) {
                    level = main.getConfigManager().getLevelsConfig().getLevelFromXp(profile.getExperience());
                }
                return String.valueOf(level);
            }
            case "globalelo":
                return String.valueOf(profile.getElo());
            case "kills":
                return String.valueOf(profile.getKills());
            case "deaths":
                return String.valueOf(profile.getDeaths());
            case "wins":
                return String.valueOf(profile.getWins());
            case "losses":
                return String.valueOf(profile.getLosses());
            case "unranked-wins":
                return String.valueOf(profile.getUnrankedWins());
            case "unranked-losses":
                return String.valueOf(profile.getUnrankedLosses());
            case "ranked-wins":
                return String.valueOf(profile.getRankedWins());
            case "ranked-losses":
                return String.valueOf(profile.getRankedLosses());
            case "total-matches":
                return String.valueOf(profile.getWins() + profile.getLosses());
            case "winstreak":
                return String.valueOf(profile.getWinStreak());
            case "highestwinstreak":
                return String.valueOf(profile.getBestWinStreak());
            case "wlr":
                return String.format("%.2f", profile.getWLRatio());
            case "kdr":
                return String.format("%.2f", profile.getKDRatio());
            case "tournaments-hosted":
                return String.valueOf(profile.getTournamentsHosted());
            case "events-hosted":
                return String.valueOf(profile.getEventsHosted());
            case "event-wins":
                return String.valueOf(profile.getEventWins());
            case "tournament-wins":
                return String.valueOf(profile.getTournamentWins());
            case "experience":
                return String.valueOf(profile.getExperience());
            case "coins":
                return String.valueOf(profile.getCoins());
            case "event-tokens":
                return String.valueOf(profile.getEventTokens());
            case "required-wins":
                return String.valueOf(profile.getRequiredWinsForRanked());
            case "division":
                return getDivisionDisplayName(main, profile.getElo());
            case "division-color":
                return getDivisionColorCode(main, profile.getElo());
            case "division-logo":
                return getDivisionLogo(main, profile.getElo());
            case "next-division":
                return getNextDivisionDisplayName(main, profile.getElo());
            case "division-percentage":
                return getDivisionPercentage(main, profile.getElo());
            case "division-progress":
                return getDivisionProgressBar(main, profile.getElo());
            default:
                return null;
        }
    }

    private String getDefaultStatsValue(String key) {
        switch (key) {
            case "level":
            case "globalelo":
            case "kills":
            case "deaths":
            case "wins":
            case "losses":
            case "unranked-wins":
            case "unranked-losses":
            case "ranked-wins":
            case "ranked-losses":
            case "total-matches":
            case "winstreak":
            case "highestwinstreak":
            case "tournaments-hosted":
            case "events-hosted":
            case "event-wins":
            case "tournament-wins":
            case "experience":
            case "coins":
            case "event-tokens":
            case "required-wins":
                return "0";
            case "wlr":
            case "kdr":
                return "0.00";
            case "division":
                return "&7Unranked";
            case "division-color":
                return "&7";
            case "division-logo":
                return "•";
            case "next-division":
                return "&7—";
            case "division-percentage":
                return "0%";
            case "division-progress":
                return "░░░░░░░░░░";
            default:
                return null;
        }
    }

    private String getDivisionDisplayName(Main main, int elo) {
        Division d = getDivision(main, elo);
        if (d == null) return "&7Unranked";
        return colorToCode(d.getColor()) + d.getDisplayName();
    }

    private String getDivisionColorCode(Main main, int elo) {
        Division d = getDivision(main, elo);
        if (d == null) return "&7";
        return colorToCode(d.getColor());
    }

    private String getDivisionLogo(Main main, int elo) {
        Division d = getDivision(main, elo);
        if (d == null) return "•";
        return colorToCode(d.getColor()) + "⭐";
    }

    private String getNextDivisionDisplayName(Main main, int elo) {
        Rank next = getNextRank(main, elo);
        if (next == null) return "&7—";
        return colorToCode(next.getColor()) + next.getDisplayName();
    }

    private String getDivisionPercentage(Main main, int elo) {
        Rank current = null;
        if (main.getRankManager() != null && main.getRankManager().getAllRanks() != null && !main.getRankManager().getAllRanks().isEmpty()) {
            try {
                current = main.getRankManager().getRankByElo(elo);
            } catch (Exception ignored) { }
        }
        Rank next = getNextRank(main, elo);
        if (current == null || next == null) return "0%";
        int currentElo = current.getEloNeeded();
        int nextElo = next.getEloNeeded();
        int range = nextElo - currentElo;
        if (range <= 0) return "100%";
        int pct = Math.min(100, Math.max(0, ((elo - currentElo) * 100) / range));
        return pct + "%";
    }

    private String getDivisionProgressBar(Main main, int elo) {
        Rank current = null;
        if (main.getRankManager() != null && main.getRankManager().getAllRanks() != null && !main.getRankManager().getAllRanks().isEmpty()) {
            try {
                current = main.getRankManager().getRankByElo(elo);
            } catch (Exception ignored) { }
        }
        Rank next = getNextRank(main, elo);
        if (current == null || next == null) return "&8░░░░░░░░░░";
        int currentElo = current.getEloNeeded();
        int nextElo = next.getEloNeeded();
        int range = nextElo - currentElo;
        if (range <= 0) return "&a██████████";
        int currentInRange = elo - currentElo;
        int pct = Math.min(100, Math.max(0, (currentInRange * 100) / range));
        int len = 10;
        int filled = (pct * len) / 100;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filled; i++) sb.append("&a█");
        for (int i = filled; i < len; i++) sb.append("&8░");
        return sb.toString();
    }

    private Division getDivision(Main main, int elo) {
        if (main.getRankManager() == null) return null;
        List<Rank> ranks = main.getRankManager().getAllRanks();
        if (ranks == null || ranks.isEmpty()) return null;
        try {
            return main.getRankManager().getDivision(elo);
        } catch (Exception e) {
            return null;
        }
    }

    private Rank getNextRank(Main main, int elo) {
        if (main.getRankManager() == null) return null;
        List<Rank> ranks = main.getRankManager().getAllRanks();
        if (ranks == null || ranks.isEmpty()) return null;
        Rank current;
        try {
            current = main.getRankManager().getRankByElo(elo);
        } catch (Exception e) {
            return null;
        }
        if (current == null) return null;
        int nextPriority = current.getPriority() + 1;
        for (Rank r : ranks) {
            if (r.getPriority() == nextPriority) return r;
        }
        return null;
    }

    private static String colorToCode(ChatColor color) {
        if (color == null) return "&7";
        return "&" + color.getChar();
    }

    /**
     * Handles match-related placeholders (e.g. %artix_match_kit%).
     */
    @Nullable
    public String handleMatchPlaceholders(Player player, String key) {
        if (player == null || Main.getInstance().getMatchManager() == null) return null;
        dev.artixdev.practice.models.Match match = Main.getInstance().getMatchManager().getMatchByPlayer(player);
        if (match == null) return null;
        switch (key.toLowerCase()) {
            case "kit":
                return match.getKitType() != null ? match.getKitType().getName() : "—";
            case "duration":
                long sec = (System.currentTimeMillis() - match.getStartTime()) / 1000;
                return String.format("%d:%02d", sec / 60, sec % 60);
            default:
                return null;
        }
    }
}
