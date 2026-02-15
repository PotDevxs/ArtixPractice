package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.resolver;

import java.util.regex.Pattern;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.nodes.Tag;

final class ResolverTuple {
   private final Tag tag;
   private final Pattern regexp;
   private final int limit;

   public ResolverTuple(Tag tag, Pattern regexp, int limit) {
      this.tag = tag;
      this.regexp = regexp;
      this.limit = limit;
   }

   public Tag getTag() {
      return this.tag;
   }

   public Pattern getRegexp() {
      return this.regexp;
   }

   public int getLimit() {
      return this.limit;
   }

   public String toString() {
      return "Tuple tag=" + this.tag + " regexp=" + this.regexp + " limit=" + this.limit;
   }
}
