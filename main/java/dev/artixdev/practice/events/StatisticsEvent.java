package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple statistics-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the name of the player whose statistics are involved.
 */
public class StatisticsEvent extends BaseEvent {

    private final String playerName;

    public StatisticsEvent(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}

