package dev.artixdev.libs.net.kyori.adventure.sound;

import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl implements SoundStop {
   static final SoundStop ALL = new SoundStopImpl((Sound.Source)null) {
      @Nullable
      public Key sound() {
         return null;
      }
   };
   @Nullable
   private final Sound.Source source;

   SoundStopImpl(@Nullable Sound.Source source) {
      this.source = source;
   }

   @Nullable
   public Sound.Source source() {
      return this.source;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof SoundStopImpl)) {
         return false;
      } else {
         SoundStopImpl that = (SoundStopImpl)other;
         return Objects.equals(this.sound(), that.sound()) && Objects.equals(this.source, that.source);
      }
   }

   public int hashCode() {
      int result = Objects.hashCode(this.sound());
      result = 31 * result + Objects.hashCode(this.source);
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("name", (Object)this.sound()), ExaminableProperty.of("source", (Object)this.source));
   }

   public String toString() {
      return Internals.toString(this);
   }
}
