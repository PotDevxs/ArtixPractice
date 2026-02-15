package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import dev.artixdev.practice.utils.events.BaseEvent;
import dev.artixdev.practice.models.Match;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataSaveEvent extends BaseEvent {
    private final Optional<Player> player;
    private final Match match;
    private final long startTime;
    private final Optional<UUID> playerId;
    private long endTime;
    private boolean cancelled;

    public PlayerDataSaveEvent(Match match, long startTime, long endTime) {
        this.player = Optional.empty();
        this.playerId = Optional.empty();
        this.match = match;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public PlayerDataSaveEvent(@Nullable Player player, UUID playerId, Match match, long startTime, long endTime) {
        this.player = Optional.ofNullable(player);
        this.playerId = Optional.ofNullable(playerId);
        this.match = match;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Match getMatch() {
        return match;
    }

    public long getStartTime() {
        return startTime;
    }

    public Optional<UUID> getPlayerId() {
        return playerId;
    }

    public Optional<Player> getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}