package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface MutableStyleSetter<T extends MutableStyleSetter<?>> extends StyleSetter<T> {
   @Contract("_ -> this")
   @NotNull
   default T decorate(@NotNull TextDecoration... decorations) {
      int i = 0;

      for(int length = decorations.length; i < length; ++i) {
         this.decorate((TextDecoration)decorations[i]);
      }

      @SuppressWarnings("unchecked")
      T result = (T) this;
      return result;
   }

   @Contract("_ -> this")
   @NotNull
   default T decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
      Objects.requireNonNull(decorations, "decorations");
      Iterator var2 = decorations.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<TextDecoration, TextDecoration.State> entry = (Entry)var2.next();
         this.decoration((TextDecoration)entry.getKey(), (TextDecoration.State)entry.getValue());
      }

      @SuppressWarnings("unchecked")
      T result = (T) this;
      return result;
   }

   @Contract("_, _ -> this")
   @NotNull
   default T decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
      TextDecoration.State state = TextDecoration.State.byBoolean(flag);
      decorations.forEach((decoration) -> {
         this.decoration(decoration, state);
      });
      @SuppressWarnings("unchecked")
      T result = (T) this;
      return result;
   }
}
