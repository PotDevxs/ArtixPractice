package dev.artixdev.libs.net.kyori.adventure.sound;

import java.util.Objects;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface SoundStop extends Examinable {
   @NotNull
   static SoundStop all() {
      return SoundStopImpl.ALL;
   }

   @NotNull
   static SoundStop named(@NotNull final Key sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return sound;
         }
      };
   }

   @NotNull
   static SoundStop named(@NotNull final Sound.Type sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return sound.key();
         }
      };
   }

   @NotNull
   static SoundStop named(@NotNull final Supplier<? extends Sound.Type> sound) {
      Objects.requireNonNull(sound, "sound");
      return new SoundStopImpl((Sound.Source)null) {
         @NotNull
         public Key sound() {
            return ((Sound.Type)sound.get()).key();
         }
      };
   }

   @NotNull
   static SoundStop source(@NotNull Sound.Source source) {
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @Nullable
         public Key sound() {
            return null;
         }
      };
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull final Key sound, @NotNull Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @NotNull
         public Key sound() {
            return sound;
         }
      };
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull Sound.Type sound, @NotNull Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      return namedOnSource(sound.key(), source);
   }

   @NotNull
   static SoundStop namedOnSource(@NotNull final Supplier<? extends Sound.Type> sound, @NotNull Sound.Source source) {
      Objects.requireNonNull(sound, "sound");
      Objects.requireNonNull(source, "source");
      return new SoundStopImpl(source) {
         @NotNull
         public Key sound() {
            return ((Sound.Type)sound.get()).key();
         }
      };
   }

   @Nullable
   Key sound();

   @Nullable
   Sound.Source source();
}
