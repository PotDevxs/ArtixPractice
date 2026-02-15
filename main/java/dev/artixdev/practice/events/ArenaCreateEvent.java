package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Match;

import java.util.Optional;
import java.util.UUID;

public class ArenaCreateEvent extends BaseEvent {
    private final Optional<UUID> playerId;
    private final boolean success;
    private final Match match;

    public ArenaCreateEvent(Match match, boolean success) {
        this.playerId = Optional.empty();
        this.match = match;
        this.success = success;
    }

    public ArenaCreateEvent(UUID playerId, Match match, boolean success) {
        this.playerId = Optional.ofNullable(playerId);
        this.match = match;
        this.success = success;
    }

    public Optional<UUID> getPlayerId() {
        return playerId;
    }

    public Match getMatch() {
        return match;
    }

    public boolean isSuccess() {
        return success;
    }
}