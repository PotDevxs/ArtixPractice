package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Match;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerKitEvent extends BaseEvent {
    private Optional<Player> player;
    private final Match match;
    private final Optional<UUID> playerId;

    public PlayerKitEvent(Player player, Match match) {
        Objects.requireNonNull(player);
        this.player = Optional.of(player);
        this.playerId = Optional.of(player.getUniqueId());
        this.match = match;
    }

    public PlayerKitEvent(Match match) {
        this.playerId = Optional.empty();
        this.match = match;
    }

    public PlayerKitEvent(UUID playerId, Match match) {
        this.playerId = Optional.of(playerId);
        this.match = match;
    }

    public Optional<Player> getPlayer() {
        return player;
    }

    public Optional<UUID> getPlayerId() {
        return playerId;
    }

    public Match getMatch() {
        return match;
    }
}