package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple queue-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the name of the queue involved in the event.
 */
public class QueueEvent extends BaseEvent {

    private final String queueName;

    public QueueEvent(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }
}

