package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.events;

import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.Mark;

public abstract class NodeEvent extends Event {
   private final String anchor;

   public NodeEvent(String anchor, Mark startMark, Mark endMark) {
      super(startMark, endMark);
      this.anchor = anchor;
   }

   public String getAnchor() {
      return this.anchor;
   }

   protected String getArguments() {
      return "anchor=" + this.anchor;
   }
}
