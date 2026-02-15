package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface TextComponent extends BuildableComponent<TextComponent, TextComponent.Builder>, ScopedComponent<TextComponent> {
   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static TextComponent ofChildren(@NotNull ComponentLike... components) {
      return Component.textOfChildren(components);
   }

   @NotNull
   String content();

   @Contract(
      pure = true
   )
   @NotNull
   TextComponent content(@NotNull String var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("content", this.content())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<TextComponent, TextComponent.Builder> {
      @NotNull
      String content();

      @Contract("_ -> this")
      @NotNull
      TextComponent.Builder content(@NotNull String var1);
   }
}
