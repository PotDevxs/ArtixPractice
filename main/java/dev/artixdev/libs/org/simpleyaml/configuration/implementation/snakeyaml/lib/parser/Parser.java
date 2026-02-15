package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.parser;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events.Event;

public interface Parser {
   boolean checkEvent(Event.ID eventId);

   Event peekEvent();

   Event getEvent();
}
