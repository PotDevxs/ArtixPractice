package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class DocumentEndEvent extends Event {
   private final boolean explicit;

   public DocumentEndEvent(Mark startMark, Mark endMark, boolean explicit) {
      super(startMark, endMark);
      this.explicit = explicit;
   }

   public boolean getExplicit() {
      return this.explicit;
   }

   public Event.ID getEventId() {
      return Event.ID.DocumentEnd;
   }
}
