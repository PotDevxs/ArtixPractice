package dev.artixdev.practice.events;

import dev.artixdev.practice.utils.events.BaseEvent;

/**
 * Simple bot-related event used by {@link dev.artixdev.practice.listeners.EventListener}.
 * Holds the name of the bot involved in the event.
 */
public class BotEvent extends BaseEvent {

    private final String botName;

    public BotEvent(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }
}

