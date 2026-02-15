package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class MappingStartEvent extends CollectionStartEvent {
   public MappingStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, DumperOptions.FlowStyle flowStyle) {
      super(anchor, tag, implicit, startMark, endMark, flowStyle);
   }

   /** @deprecated */
   @Deprecated
   public MappingStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, Boolean flowStyle) {
      this(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
   }

   public Event.ID getEventId() {
      return Event.ID.MappingStart;
   }
}
