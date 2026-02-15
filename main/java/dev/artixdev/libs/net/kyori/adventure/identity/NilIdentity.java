package dev.artixdev.libs.net.kyori.adventure.identity;

import java.util.UUID;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class NilIdentity implements Identity {
   static final UUID NIL_UUID = new UUID(0L, 0L);
   static final Identity INSTANCE = new NilIdentity();

   @NotNull
   public UUID uuid() {
      return NIL_UUID;
   }

   public String toString() {
      return "Identity.nil()";
   }

   public boolean equals(@Nullable Object that) {
      return this == that;
   }

   public int hashCode() {
      return 0;
   }
}
