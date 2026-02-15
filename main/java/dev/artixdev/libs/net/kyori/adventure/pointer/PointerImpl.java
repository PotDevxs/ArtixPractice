package dev.artixdev.libs.net.kyori.adventure.pointer;

import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class PointerImpl<T> implements Pointer<T> {
   private final Class<T> type;
   private final Key key;

   PointerImpl(Class<T> type, Key key) {
      this.type = type;
      this.key = key;
   }

   @NotNull
   public Class<T> type() {
      return this.type;
   }

   @NotNull
   public Key key() {
      return this.key;
   }

   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         PointerImpl<?> that = (PointerImpl)other;
         return this.type.equals(that.type) && this.key.equals(that.key);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + this.key.hashCode();
      return result;
   }
}
