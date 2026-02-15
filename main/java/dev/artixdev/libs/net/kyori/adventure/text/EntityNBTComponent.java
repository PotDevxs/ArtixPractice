package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface EntityNBTComponent extends NBTComponent<EntityNBTComponent, EntityNBTComponent.Builder>, ScopedComponent<EntityNBTComponent> {
   @NotNull
   String selector();

   @Contract(
      pure = true
   )
   @NotNull
   EntityNBTComponent selector(@NotNull String var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("selector", this.selector())), NBTComponent.super.examinableProperties());
   }

   public interface Builder extends NBTComponentBuilder<EntityNBTComponent, EntityNBTComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      EntityNBTComponent.Builder selector(@NotNull String var1);
   }
}
