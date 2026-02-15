package dev.artixdev.libs.net.kyori.adventure.util;

import java.util.function.Consumer;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface Buildable<R, B extends Buildable.Builder<R>> {
   /** @deprecated */
   @Deprecated
   @Contract(
      mutates = "param1"
   )
   @NotNull
   static <R extends Buildable<R, B>, B extends Buildable.Builder<R>> R configureAndBuild(@NotNull B builder, @Nullable Consumer<? super B> consumer) {
      return (R) AbstractBuilder.configureAndBuild(builder, consumer);
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   B toBuilder();

   /** @deprecated */
   @Deprecated
   public interface Builder<R> extends AbstractBuilder<R> {
      @Contract(
         value = "-> new",
         pure = true
      )
      @NotNull
      R build();
   }
}
