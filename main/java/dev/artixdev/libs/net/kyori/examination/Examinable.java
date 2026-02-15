package dev.artixdev.libs.net.kyori.examination;

import java.util.stream.Stream;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface Examinable {
   @NotNull
   default String examinableName() {
      return this.getClass().getSimpleName();
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.empty();
   }

   @NotNull
   default <R> R examine(@NotNull Examiner<R> examiner) {
      return examiner.examine(this);
   }
}
