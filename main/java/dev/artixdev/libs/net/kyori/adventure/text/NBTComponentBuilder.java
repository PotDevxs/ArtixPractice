package dev.artixdev.libs.net.kyori.adventure.text;

import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface NBTComponentBuilder<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends ComponentBuilder<C, B> {
   @Contract("_ -> this")
   @NotNull
   B nbtPath(@NotNull String var1);

   @Contract("_ -> this")
   @NotNull
   B interpret(boolean var1);

   @Contract("_ -> this")
   @NotNull
   B separator(@Nullable ComponentLike var1);
}
