package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEventSource;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface StyleSetter<T extends StyleSetter<?>> {
   @NotNull
   T font(@Nullable Key var1);

   @NotNull
   T color(@Nullable TextColor var1);

   @NotNull
   T colorIfAbsent(@Nullable TextColor var1);

   @NotNull
   default T decorate(@NotNull TextDecoration decoration) {
      return this.decoration(decoration, TextDecoration.State.TRUE);
   }

   @NotNull
   default T decorate(@NotNull TextDecoration... decorations) {
      Map<TextDecoration, TextDecoration.State> map = new EnumMap(TextDecoration.class);
      int i = 0;

      for(int length = decorations.length; i < length; ++i) {
         map.put(decorations[i], TextDecoration.State.TRUE);
      }

      return this.decorations(map);
   }

   @NotNull
   default T decoration(@NotNull TextDecoration decoration, boolean flag) {
      return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
   }

   @NotNull
   T decoration(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @NotNull
   T decorationIfAbsent(@NotNull TextDecoration var1, @NotNull TextDecoration.State var2);

   @NotNull
   T decorations(@NotNull Map<TextDecoration, TextDecoration.State> var1);

   @NotNull
   default T decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
      @SuppressWarnings("unchecked")
      Map<TextDecoration, TextDecoration.State> map = (Map<TextDecoration, TextDecoration.State>) decorations.stream().collect(Collectors.toMap(Function.identity(), (decoration) -> {
         return TextDecoration.State.byBoolean(flag);
      }));
      @SuppressWarnings("unchecked")
      T result = (T) this.decorations(map);
      return result;
   }

   @NotNull
   T clickEvent(@Nullable ClickEvent var1);

   @NotNull
   T hoverEvent(@Nullable HoverEventSource<?> var1);

   @NotNull
   T insertion(@Nullable String var1);
}
