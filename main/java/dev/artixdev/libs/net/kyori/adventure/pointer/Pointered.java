package dev.artixdev.libs.net.kyori.adventure.pointer;

import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.UnknownNullability;

public interface Pointered {
   @NotNull
   default <T> Optional<T> get(@NotNull Pointer<T> pointer) {
      return this.pointers().get(pointer);
   }

   @Contract("_, null -> _; _, !null -> !null")
   @Nullable
   default <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
      return this.pointers().getOrDefault(pointer, defaultValue);
   }

   @UnknownNullability
   default <T> T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
      return this.pointers().getOrDefaultFrom(pointer, defaultValue);
   }

   @NotNull
   default Pointers pointers() {
      return Pointers.empty();
   }
}
