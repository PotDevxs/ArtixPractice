package dev.artixdev.libs.net.kyori.adventure.identity;

import java.util.UUID;
import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class IdentityImpl implements Identity, Examinable {
   private final UUID uuid;

   IdentityImpl(UUID uuid) {
      this.uuid = uuid;
   }

   @NotNull
   public UUID uuid() {
      return this.uuid;
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Identity)) {
         return false;
      } else {
         Identity that = (Identity)other;
         return this.uuid.equals(that.uuid());
      }
   }

   public int hashCode() {
      return this.uuid.hashCode();
   }
}
