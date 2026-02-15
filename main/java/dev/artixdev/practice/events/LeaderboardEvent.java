package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple leaderboard-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the type/name of the leaderboard involved.
 */
public class LeaderboardEvent extends BaseEvent {

    private final String leaderboardType;

    public LeaderboardEvent(String leaderboardType) {
        this.leaderboardType = leaderboardType;
    }

    public String getLeaderboardType() {
        return leaderboardType;
    }
}

