package dev.artixdev.libs.net.kyori.adventure.internal;

import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.string.StringExaminer;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class Internals {
   private Internals() {
   }

   @NotNull
   public static String toString(@NotNull Examinable examinable) {
      return (String)examinable.examine(StringExaminer.simpleEscaping());
   }
}
