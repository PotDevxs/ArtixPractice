package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import java.util.Map;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public final class DocumentStartEvent extends Event {
   private final boolean explicit;
   private final DumperOptions.Version version;
   private final Map<String, String> tags;

   public DocumentStartEvent(Mark startMark, Mark endMark, boolean explicit, DumperOptions.Version version, Map<String, String> tags) {
      super(startMark, endMark);
      this.explicit = explicit;
      this.version = version;
      this.tags = tags;
   }

   public boolean getExplicit() {
      return this.explicit;
   }

   public DumperOptions.Version getVersion() {
      return this.version;
   }

   public Map<String, String> getTags() {
      return this.tags;
   }

   public Event.ID getEventId() {
      return Event.ID.DocumentStart;
   }
}
