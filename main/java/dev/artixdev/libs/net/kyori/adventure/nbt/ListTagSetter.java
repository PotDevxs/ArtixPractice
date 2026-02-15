package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ListTagSetter<R, T extends BinaryTag> {
   @NotNull
   R add(T var1);

   @NotNull
   R add(Iterable<? extends T> var1);
}
