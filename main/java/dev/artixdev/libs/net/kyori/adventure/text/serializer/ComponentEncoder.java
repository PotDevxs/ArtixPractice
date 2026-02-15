package dev.artixdev.libs.net.kyori.adventure.text.serializer;

import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface ComponentEncoder<I extends Component, R> {
   @NotNull
   R serialize(@NotNull I var1);

   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default R serializeOrNull(@Nullable I component) {
      return this.serializeOr(component, (R) null);
   }

   @Contract(
      value = "!null, _ -> !null; null, _ -> param2",
      pure = true
   )
   @Nullable
   default R serializeOr(@Nullable I component, @Nullable R fallback) {
      return component == null ? fallback : this.serialize(component);
   }
}
