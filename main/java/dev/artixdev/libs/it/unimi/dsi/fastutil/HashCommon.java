package dev.artixdev.libs.it.unimi.dsi.fastutil;

/** Common code for all hash-based classes. */
public final class HashCommon {

   private static final int INT_PHI = 0x9E3779B9;

   private HashCommon() {}

   private static final long LONG_PHI = 0x9E3779B97F4A7C15L;

   /** Quickly mixes the bits of an integer (golden ratio + xorshift). */
   public static int mix(final int x) {
      final int h = x * INT_PHI;
      return h ^ (h >>> 16);
   }

   /** Quickly mixes the bits of a long (golden ratio + xorshift). */
   public static long mix(final long x) {
      long h = x * LONG_PHI;
      h ^= h >>> 32;
      return h ^ (h >>> 16);
   }

   /** Returns the hash code that would be returned by {@link Long#hashCode()}. */
   public static int long2int(final long l) {
      return (int) (l ^ (l >>> 32));
   }

   /** Returns the least power of two greater than or equal to the specified value. */
   public static int nextPowerOfTwo(final int x) {
      if (x <= 1) return 1;
      return 1 << (32 - Integer.numberOfLeadingZeros(x - 1));
   }

   /** Returns the least power of two greater than or equal to the specified value. */
   public static long nextPowerOfTwo(final long x) {
      if (x <= 1) return 1;
      return 1L << (64 - Long.numberOfLeadingZeros(x - 1));
   }

   /** Returns the maximum number of entries that can be filled before rehashing. */
   public static int maxFill(final int n, final float f) {
      return Math.min((int) Math.ceil(n * (double) f), n - 1);
   }

   /** Returns the minimum table size for expected elements and load factor. */
   public static int arraySize(final int expected, final float f) {
      final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / (double) f)));
      if (s > (1 << 30)) {
         throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
      }
      return (int) s;
   }
}
