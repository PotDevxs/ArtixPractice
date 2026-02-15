package dev.artixdev.libs.net.kyori.adventure.util;

import java.time.Duration;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface Ticks {
   int TICKS_PER_SECOND = 20;
   long SINGLE_TICK_DURATION_MS = 50L;

   @NotNull
   static Duration duration(long ticks) {
      return Duration.ofMillis(ticks * 50L);
   }
}
