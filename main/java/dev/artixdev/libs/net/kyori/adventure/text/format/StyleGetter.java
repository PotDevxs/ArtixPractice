package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.EnumMap;
import java.util.Map;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.event.ClickEvent;
import dev.artixdev.libs.net.kyori.adventure.text.event.HoverEvent;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface StyleGetter {
   @Nullable
   Key font();

   @Nullable
   TextColor color();

   default boolean hasDecoration(@NotNull TextDecoration decoration) {
      return this.decoration(decoration) == TextDecoration.State.TRUE;
   }

   @NotNull
   TextDecoration.State decoration(@NotNull TextDecoration var1);

   @NotNull
   @Unmodifiable
   default Map<TextDecoration, TextDecoration.State> decorations() {
      Map<TextDecoration, TextDecoration.State> decorations = new EnumMap(TextDecoration.class);
      int i = 0;

      for(int length = DecorationMap.DECORATIONS.length; i < length; ++i) {
         TextDecoration decoration = DecorationMap.DECORATIONS[i];
         TextDecoration.State value = this.decoration(decoration);
         decorations.put(decoration, value);
      }

      return decorations;
   }

   @Nullable
   ClickEvent clickEvent();

   @Nullable
   HoverEvent<?> hoverEvent();

   @Nullable
   String insertion();
}
