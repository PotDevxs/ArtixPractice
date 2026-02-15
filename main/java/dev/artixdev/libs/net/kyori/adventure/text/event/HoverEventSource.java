package dev.artixdev.libs.net.kyori.adventure.text.event;

import java.util.function.UnaryOperator;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface HoverEventSource<V> {
   @Nullable
   static <V> HoverEvent<V> unbox(@Nullable HoverEventSource<V> source) {
      return source != null ? source.asHoverEvent() : null;
   }

   @NotNull
   default HoverEvent<V> asHoverEvent() {
      return this.asHoverEvent(UnaryOperator.identity());
   }

   @NotNull
   HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> var1);
}
