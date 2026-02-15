package dev.artixdev.practice.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when a match is starting. Currently only exposes the primary player.
 * Extend this event with more context (match id, kit, etc.) as needed.
 */
public class MatchStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public MatchStartEvent(Player player) {
        this.player = player;
    }

    /**
     * Player involved in this match start. May be null if not applicable.
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

