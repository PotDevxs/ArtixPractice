package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends BuildableComponent<C, B> {
   @NotNull
   String nbtPath();

   @Contract(
      pure = true
   )
   @NotNull
   C nbtPath(@NotNull String var1);

   boolean interpret();

   @Contract(
      pure = true
   )
   @NotNull
   C interpret(boolean var1);

   @Nullable
   Component separator();

   @NotNull
   C separator(@Nullable ComponentLike var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("nbtPath", this.nbtPath()), ExaminableProperty.of("interpret", this.interpret()), ExaminableProperty.of("separator", (Object)this.separator())), BuildableComponent.super.examinableProperties());
   }
}
