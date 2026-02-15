package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple kit-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the name of the kit involved in the event.
 */
public class KitEvent extends BaseEvent {

    private final String kitName;

    public KitEvent(String kitName) {
        this.kitName = kitName;
    }

    public String getKitName() {
        return kitName;
    }
}

