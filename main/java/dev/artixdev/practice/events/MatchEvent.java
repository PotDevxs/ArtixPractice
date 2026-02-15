package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

import java.util.UUID;

/**
 * Simple match-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds an identifier for the match.
 */
public class MatchEvent extends BaseEvent {

    private final UUID matchId;

    public MatchEvent(UUID matchId) {
        this.matchId = matchId;
    }

    public UUID getMatchId() {
        return matchId;
    }
}

