package dev.artixdev.practice.expansions.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

/**
 * PlaceholderAPI expansion for Bolt Practice.
 * Placeholders use the prefix {@code %bolt_...%}.
 */
public class BoltExpansions extends PlaceholderExpansion {

    private static final String IDENTIFIER = "bolt";
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
        if (player == null || params == null || params.isEmpty()) {
            return null;
        }
        String[] parts = params.split("_");
        if (parts.length == 0) {
            return null;
        }
        String category = parts[0].toLowerCase();
        switch (category) {
            case "stats":
                return handleStatsPlaceholders(player, parts);
            case "match":
                return handleMatchPlaceholders(player, parts);
            default:
                return null;
        }
    }

    /**
     * Handles stats-related placeholders (e.g. %bolt_stats_wins%).
     */
    public String handleStatsPlaceholders(Player player, String[] params) {
        // TODO: resolve from profile/statistics
        return null;
    }

    /**
     * Handles match-related placeholders (e.g. %bolt_match_kit%).
     */
    public String handleMatchPlaceholders(Player player, String[] params) {
        // TODO: resolve from current match
        return null;
    }
}
