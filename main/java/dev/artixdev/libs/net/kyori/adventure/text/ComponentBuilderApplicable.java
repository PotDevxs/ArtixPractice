package dev.artixdev.libs.net.kyori.adventure.text;

import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentBuilderApplicable {
   @Contract(
      mutates = "param"
   )
   void componentBuilderApply(@NotNull ComponentBuilder<?, ?> var1);
}
