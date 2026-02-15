package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public abstract class CollectionEndEvent extends Event {
   public CollectionEndEvent(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }
}
