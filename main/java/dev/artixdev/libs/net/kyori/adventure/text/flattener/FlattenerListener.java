package dev.artixdev.libs.net.kyori.adventure.text.flattener;

import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FlattenerListener {
   default void pushStyle(@NotNull Style style) {
   }

   void component(@NotNull String var1);

   default void popStyle(@NotNull Style style) {
   }
}
