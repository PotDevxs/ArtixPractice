package dev.artixdev.libs.net.kyori.adventure.text.format;

import dev.artixdev.libs.net.kyori.adventure.text.ComponentBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentBuilderApplicable;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface StyleBuilderApplicable extends ComponentBuilderApplicable {
   @Contract(
      mutates = "param"
   )
   void styleApply(@NotNull Style.Builder var1);

   default void componentBuilderApply(@NotNull ComponentBuilder<?, ?> component) {
      component.style(this::styleApply);
   }
}
