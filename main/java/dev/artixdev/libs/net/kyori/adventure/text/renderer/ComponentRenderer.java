package dev.artixdev.libs.net.kyori.adventure.text.renderer;

import java.util.function.Function;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ComponentRenderer<C> {
   @NotNull
   Component render(@NotNull Component var1, @NotNull C var2);

   default <T> ComponentRenderer<T> mapContext(Function<T, C> transformer) {
      return (component, ctx) -> {
         return this.render(component, transformer.apply(ctx));
      };
   }
}
