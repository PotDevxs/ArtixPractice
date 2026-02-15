package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Match;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataLoadEvent extends BaseEvent {
    private final Match match;
    private final Optional<UUID> playerId;
    private final long timestamp;
    private final Optional<Player> player;

    public PlayerDataLoadEvent(Match match, long timestamp) {
        this.player = Optional.empty();
        this.playerId = Optional.empty();
        this.match = match;
        this.timestamp = timestamp;
    }

    public PlayerDataLoadEvent(@Nullable Player player, UUID playerId, Match match, long timestamp) {
        this.player = Optional.ofNullable(player);
        this.playerId = Optional.ofNullable(playerId);
        this.match = match;
        this.timestamp = timestamp;
    }

    public Match getMatch() {
        return match;
    }

    public Optional<UUID> getPlayerId() {
        return playerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Optional<Player> getPlayer() {
        return player;
    }
}