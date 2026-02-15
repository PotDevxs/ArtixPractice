package dev.artixdev.libs.net.kyori.adventure.permission;

import dev.artixdev.libs.net.kyori.adventure.util.TriState;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class PermissionCheckers {
   static final PermissionChecker NOT_SET;
   static final PermissionChecker FALSE;
   static final PermissionChecker TRUE;

   private PermissionCheckers() {
   }

   static {
      NOT_SET = new PermissionCheckers.Always(TriState.NOT_SET);
      FALSE = new PermissionCheckers.Always(TriState.FALSE);
      TRUE = new PermissionCheckers.Always(TriState.TRUE);
   }

   private static final class Always implements PermissionChecker {
      private final TriState value;

      private Always(TriState value) {
         this.value = value;
      }

      @NotNull
      public TriState value(@NotNull String permission) {
         return this.value;
      }

      public String toString() {
         return PermissionChecker.class.getSimpleName() + ".always(" + this.value + ")";
      }

      public boolean equals(@Nullable Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            PermissionCheckers.Always always = (PermissionCheckers.Always)other;
            return this.value == always.value;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      // $FF: synthetic method
      Always(TriState x0, Object x1) {
         this(x0);
      }
   }
}
