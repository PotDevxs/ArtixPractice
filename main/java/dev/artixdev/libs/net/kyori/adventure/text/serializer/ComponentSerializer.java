package dev.artixdev.libs.net.kyori.adventure.text.serializer;

import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface ComponentSerializer<I extends Component, O extends Component, R> extends ComponentEncoder<I, R> {
   @NotNull
   O deserialize(@NotNull R var1);

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default O deseializeOrNull(@Nullable R input) {
      return this.deserializeOrNull(input);
   }

   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default O deserializeOrNull(@Nullable R input) {
      return this.deserializeOr(input, (O) null);
   }

   @Contract(
      value = "!null, _ -> !null; null, _ -> param2",
      pure = true
   )
   @Nullable
   default O deserializeOr(@Nullable R input, @Nullable O fallback) {
      return input == null ? fallback : this.deserialize(input);
   }

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
