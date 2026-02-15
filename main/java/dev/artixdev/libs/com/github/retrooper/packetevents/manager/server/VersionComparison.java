package dev.artixdev.libs.com.github.retrooper.packetevents.manager.server;

public enum VersionComparison {
   EQUALS,
   NEWER_THAN,
   NEWER_THAN_OR_EQUALS,
   OLDER_THAN,
   OLDER_THAN_OR_EQUALS;

   // $FF: synthetic method
   private static VersionComparison[] $values() {
      return new VersionComparison[]{EQUALS, NEWER_THAN, NEWER_THAN_OR_EQUALS, OLDER_THAN, OLDER_THAN_OR_EQUALS};
   }
}
