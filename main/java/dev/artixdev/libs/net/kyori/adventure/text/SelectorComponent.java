package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface SelectorComponent extends BuildableComponent<SelectorComponent, SelectorComponent.Builder>, ScopedComponent<SelectorComponent> {
   @NotNull
   String pattern();

   @Contract(
      pure = true
   )
   @NotNull
   SelectorComponent pattern(@NotNull String var1);

   @Nullable
   Component separator();

   @NotNull
   SelectorComponent separator(@Nullable ComponentLike var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("pattern", this.pattern()), ExaminableProperty.of("separator", (Object)this.separator())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<SelectorComponent, SelectorComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      SelectorComponent.Builder pattern(@NotNull String var1);

      @Contract("_ -> this")
      @NotNull
      SelectorComponent.Builder separator(@Nullable ComponentLike var1);
   }
}
