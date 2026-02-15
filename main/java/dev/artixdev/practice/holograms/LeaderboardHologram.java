package dev.artixdev.practice.holograms;

import dev.artixdev.practice.enums.LeaderboardType;

/**
 * LeaderboardHologram
 * Hologram for displaying global leaderboards
 */
public class LeaderboardHologram extends AbstractHologram {
    
    public LeaderboardHologram(LeaderboardType leaderboardType) {
        super(leaderboardType);
        this.setHologramType(dev.artixdev.practice.enums.HologramType.GLOBAL_LEADERBOARD);
    }
}
