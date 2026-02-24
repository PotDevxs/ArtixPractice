package dev.artixdev.libs.it.unimi.dsi.fastutil;

import java.util.Collection;
import java.util.Map;

/**
 * Utility to obtain the size of a {@link Map} or {@link Collection} as a 64-bit value,
 * for use with spliterators and other APIs that take a long size.
 */
public final class Size64 {
   private Size64() {
   }

   public static long sizeOf(Collection<?> c) {
      return c == null ? 0L : c.size();
   }

   public static long sizeOf(Map<?, ?> m) {
      return m == null ? 0L : m.size();
   }
}
