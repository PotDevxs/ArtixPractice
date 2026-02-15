package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class AliasEvent extends NodeEvent {
   public AliasEvent(String anchor, Mark startMark, Mark endMark) {
      super(anchor, startMark, endMark);
      if (anchor == null) {
         throw new NullPointerException("anchor is not specified for alias");
      }
   }

   public Event.ID getEventId() {
      return Event.ID.Alias;
   }
}
