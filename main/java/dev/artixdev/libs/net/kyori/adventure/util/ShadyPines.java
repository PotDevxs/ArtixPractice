package dev.artixdev.libs.net.kyori.adventure.util;

import java.util.Set;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public final class ShadyPines {
   private ShadyPines() {
   }

   /** @deprecated */
   @Deprecated
   @SafeVarargs
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   public static <E extends Enum<E>> Set<E> enumSet(Class<E> type, @NotNull E... constants) {
      return MonkeyBars.enumSet(type, constants);
   }

   public static boolean equals(double a, double b) {
      return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
   }

   public static boolean equals(float a, float b) {
      return Float.floatToIntBits(a) == Float.floatToIntBits(b);
   }
}
