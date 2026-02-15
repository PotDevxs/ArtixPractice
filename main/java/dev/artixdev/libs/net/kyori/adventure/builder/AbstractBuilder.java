package dev.artixdev.libs.net.kyori.adventure.builder;

import java.util.function.Consumer;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface AbstractBuilder<R> {
   @Contract(
      mutates = "param1"
   )
   @NotNull
   static <R, B extends AbstractBuilder<R>> R configureAndBuild(@NotNull B builder, @Nullable Consumer<? super B> consumer) {
      if (consumer != null) {
         consumer.accept(builder);
      }

      return builder.build();
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   R build();
}
