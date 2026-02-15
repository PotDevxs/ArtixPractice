package dev.artixdev.practice.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import dev.artixdev.practice.models.MatchResult;
import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Event fired when a match ends. Carries per-player results.
 */
public class MatchEndEvent extends BaseEvent {

    private final Map<UUID, MatchResult> playerResults;

    public MatchEndEvent(Map<UUID, MatchResult> playerResults) {
        this.playerResults = playerResults == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(playerResults);
    }

    /**
     * Per-player results for this match.
     */
    public Map<UUID, MatchResult> getPlayerResults() {
        return playerResults;
    }

    /**
     * Convenience helper to get the winner of the match.
     * This is derived from the first MatchResult entry where isWinner() is true.
     * May return null if no such entry exists or the player is offline.
     */
    public Player getWinner() {
        for (Map.Entry<UUID, MatchResult> entry : playerResults.entrySet()) {
            MatchResult result = entry.getValue();
            if (result != null && result.isWinner()) {
                return Bukkit.getPlayer(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Convenience helper to get the loser of the match.
     * This is derived from the first MatchResult entry where isWinner() is false.
     * May return null if no such entry exists or the player is offline.
     */
    public Player getLoser() {
        for (Map.Entry<UUID, MatchResult> entry : playerResults.entrySet()) {
            MatchResult result = entry.getValue();
            if (result != null && !result.isWinner()) {
                return Bukkit.getPlayer(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Indicates whether this match was ranked.
     * Currently returns false by default; wire this to real data when available.
     */
    public boolean isRanked() {
        return false;
    }
}
