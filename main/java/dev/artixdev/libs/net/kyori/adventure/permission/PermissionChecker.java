package dev.artixdev.libs.net.kyori.adventure.permission;

import java.util.Objects;
import java.util.function.Predicate;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.pointer.Pointer;
import dev.artixdev.libs.net.kyori.adventure.util.TriState;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface PermissionChecker extends Predicate<String> {
   Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));

   @NotNull
   static PermissionChecker always(@NotNull TriState state) {
      Objects.requireNonNull(state);
      if (state == TriState.TRUE) {
         return PermissionCheckers.TRUE;
      } else {
         return state == TriState.FALSE ? PermissionCheckers.FALSE : PermissionCheckers.NOT_SET;
      }
   }

   @NotNull
   TriState value(@NotNull String var1);

   default boolean test(@NotNull String permission) {
      return this.value(permission) == TriState.TRUE;
   }
}
