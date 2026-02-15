package dev.artixdev.api.practice.spigot.util;

public final class MathUtil {
   public static int floor(double var0) {
      int var2 = (int)var0;
      return var0 < (double)var2 ? var2 - 1 : var2;
   }

   private MathUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
