package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class StreamEndEvent extends Event {
   public StreamEndEvent(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Event.ID getEventId() {
      return Event.ID.StreamEnd;
   }
}
