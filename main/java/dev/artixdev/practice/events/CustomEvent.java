package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Generic custom event wrapper used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Extend or replace this with more specific events as needed.
 */
public class CustomEvent extends BaseEvent {

    private final String eventName;

    public CustomEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}

