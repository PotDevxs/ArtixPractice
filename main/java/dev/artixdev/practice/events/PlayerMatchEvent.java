package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.enums.KitType;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerMatchEvent extends BaseEvent {
    private final Optional<UUID> playerId;
    private Optional<Player> player;
    private final Match match;
    private final KitType kitType;
    private final boolean winner;
    private final boolean ranked;
    private final int deaths;

    public PlayerMatchEvent(Player player, Match match) {
        Objects.requireNonNull(player);
        this.player = Optional.of(player);
        this.playerId = Optional.of(player.getUniqueId());
        this.match = match;
        this.kitType = match != null ? match.getKitType() : null;
        this.winner = false;
        this.ranked = false;
        this.deaths = 0;
    }

    public PlayerMatchEvent(UUID playerId, Match match) {
        this.playerId = Optional.ofNullable(playerId);
        this.match = match;
        this.kitType = match != null ? match.getKitType() : null;
        this.winner = false;
        this.ranked = false;
        this.deaths = 0;
    }

    public PlayerMatchEvent(Match match) {
        this.playerId = Optional.empty();
        this.match = match;
        this.kitType = match != null ? match.getKitType() : null;
        this.winner = false;
        this.ranked = false;
        this.deaths = 0;
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

    public KitType getKitType() {
        return kitType;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isRanked() {
        return ranked;
    }

    public int getDeaths() {
        return deaths;
    }
}