package dev.artixdev.libs.net.kyori.adventure.pointer;

import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.UnknownNullability;

public interface Pointers extends Buildable<Pointers, Pointers.Builder> {
   @Contract(
      pure = true
   )
   @NotNull
   static Pointers empty() {
      return PointersImpl.EMPTY;
   }

   @Contract(
      pure = true
   )
   @NotNull
   static Pointers.Builder builder() {
      return new PointersImpl.BuilderImpl();
   }

   @NotNull
   <T> Optional<T> get(@NotNull Pointer<T> var1);

   @Contract("_, null -> _; _, !null -> !null")
   @Nullable
   default <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
      return this.get(pointer).orElse(defaultValue);
   }

   @UnknownNullability
   default <T> T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
      return this.get(pointer).orElseGet(defaultValue);
   }

   <T> boolean supports(@NotNull Pointer<T> var1);

   public interface Builder extends AbstractBuilder<Pointers>, Buildable.Builder<Pointers> {
      @Contract("_, _ -> this")
      @NotNull
      default <T> Pointers.Builder withStatic(@NotNull Pointer<T> pointer, @Nullable T value) {
         return this.withDynamic(pointer, () -> {
            return value;
         });
      }

      @Contract("_, _ -> this")
      @NotNull
      <T> Pointers.Builder withDynamic(@NotNull Pointer<T> var1, @NotNull Supplier<T> var2);
   }
}
