package dev.artixdev.libs.org.bson.json;

public enum JsonMode {
   /** @deprecated */
   @Deprecated
   STRICT,
   SHELL,
   EXTENDED,
   RELAXED;

   // $FF: synthetic method
   private static JsonMode[] $values() {
      return new JsonMode[]{STRICT, SHELL, EXTENDED, RELAXED};
   }
}
