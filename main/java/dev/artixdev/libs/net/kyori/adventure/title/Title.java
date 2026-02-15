package dev.artixdev.libs.net.kyori.adventure.title;

import java.time.Duration;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.util.Ticks;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.UnknownNullability;

@ApiStatus.NonExtendable
public interface Title extends Examinable {
   Title.Times DEFAULT_TIMES = Title.Times.times(Ticks.duration(10L), Ticks.duration(70L), Ticks.duration(20L));

   @NotNull
   static Title title(@NotNull Component title, @NotNull Component subtitle) {
      return title(title, subtitle, DEFAULT_TIMES);
   }

   @NotNull
   static Title title(@NotNull Component title, @NotNull Component subtitle, @Nullable Title.Times times) {
      return new TitleImpl(title, subtitle, times);
   }

   @NotNull
   Component title();

   @NotNull
   Component subtitle();

   @Nullable
   Title.Times times();

   @UnknownNullability
   <T> T part(@NotNull TitlePart<T> var1);

   public interface Times extends Examinable {
      /** @deprecated */
      @Deprecated
      @ApiStatus.ScheduledForRemoval(
         inVersion = "5.0.0"
      )
      @NotNull
      static Title.Times of(@NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
         return times(fadeIn, stay, fadeOut);
      }

      @NotNull
      static Title.Times times(@NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
         return new TitleImpl.TimesImpl(fadeIn, stay, fadeOut);
      }

      @NotNull
      Duration fadeIn();

      @NotNull
      Duration stay();

      @NotNull
      Duration fadeOut();
   }
}
