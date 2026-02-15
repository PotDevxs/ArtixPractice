package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface TextDecorationAndState extends StyleBuilderApplicable, Examinable {
   @NotNull
   TextDecoration decoration();

   @NotNull
   TextDecoration.State state();

   default void styleApply(@NotNull Style.Builder style) {
      style.decoration(this.decoration(), this.state());
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("decoration", (Object)this.decoration()), ExaminableProperty.of("state", (Object)this.state()));
   }
}
