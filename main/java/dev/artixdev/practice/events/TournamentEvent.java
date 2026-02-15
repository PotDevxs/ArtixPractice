package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple tournament-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the tournament name involved in the event.
 */
public class TournamentEvent extends BaseEvent {

    private final String tournamentName;

    public TournamentEvent(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentName() {
        return tournamentName;
    }
}

