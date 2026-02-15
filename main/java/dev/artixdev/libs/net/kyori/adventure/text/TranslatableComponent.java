package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.translation.Translatable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent> {
   @NotNull
   String key();

   @Contract(
      pure = true
   )
   @NotNull
   default TranslatableComponent key(@NotNull Translatable translatable) {
      return this.key(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey());
   }

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent key(@NotNull String var1);

   @NotNull
   List<Component> args();

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent args(@NotNull ComponentLike... var1);

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent args(@NotNull List<? extends ComponentLike> var1);

   @Nullable
   String fallback();

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent fallback(@Nullable String var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("key", this.key()), ExaminableProperty.of("args", (Object)this.args()), ExaminableProperty.of("fallback", this.fallback())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> {
      @Contract(
         pure = true
      )
      @NotNull
      default TranslatableComponent.Builder key(@NotNull Translatable translatable) {
         return this.key(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey());
      }

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder key(@NotNull String var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?> var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder args(@NotNull ComponentBuilder<?, ?>... var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder args(@NotNull Component var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder args(@NotNull ComponentLike... var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder args(@NotNull List<? extends ComponentLike> var1);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder fallback(@Nullable String var1);
   }
}
