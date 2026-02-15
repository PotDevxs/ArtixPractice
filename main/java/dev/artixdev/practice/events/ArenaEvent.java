package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple arena-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the name of the arena involved in the event.
 */
public class ArenaEvent extends BaseEvent {

    private final String arenaName;

    public ArenaEvent(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaName() {
        return arenaName;
    }
}

