package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.parser;

import java.util.Map;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.DumperOptions;

class VersionTagsTuple {
   private final DumperOptions.Version version;
   private final Map<String, String> tags;

   public VersionTagsTuple(DumperOptions.Version version, Map<String, String> tags) {
      this.version = version;
      this.tags = tags;
   }

   public DumperOptions.Version getVersion() {
      return this.version;
   }

   public Map<String, String> getTags() {
      return this.tags;
   }

   public String toString() {
      return String.format("VersionTagsTuple<%s, %s>", this.version, this.tags);
   }
}
