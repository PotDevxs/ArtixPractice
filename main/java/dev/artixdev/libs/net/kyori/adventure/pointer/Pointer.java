package dev.artixdev.libs.net.kyori.adventure.pointer;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface Pointer<V> extends Examinable {
   @NotNull
   static <V> Pointer<V> pointer(@NotNull Class<V> type, @NotNull Key key) {
      return new PointerImpl(type, key);
   }

   @NotNull
   Class<V> type();

   @NotNull
   Key key();

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("type", (Object)this.type()), ExaminableProperty.of("key", (Object)this.key()));
   }
}
