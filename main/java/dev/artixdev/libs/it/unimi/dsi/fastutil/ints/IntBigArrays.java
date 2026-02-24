package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicIntegerArray;
import dev.artixdev.libs.it.unimi.dsi.fastutil.BigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Hash;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongBigArrays;

public final class IntBigArrays {
   public static final int[][] EMPTY_BIG_ARRAY = new int[0][];
   public static final int[][] DEFAULT_EMPTY_BIG_ARRAY = new int[0][];
   public static final AtomicIntegerArray[] EMPTY_BIG_ATOMIC_ARRAY = new AtomicIntegerArray[0];
   public static final Hash.Strategy HASH_STRATEGY = new IntBigArrays.BigArrayHashStrategy();
   private static final int QUICKSORT_NO_REC = 7;
   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
   private static final int MEDIUM = 40;
   private static final int DIGIT_BITS = 8;
   private static final int DIGIT_MASK = 255;
   private static final int DIGITS_PER_ELEMENT = 4;
   private static final int RADIXSORT_NO_REC = 1024;

   private IntBigArrays() {
   }

   /** @deprecated */
   @Deprecated
   public static int get(int[][] array, long index) {
      return array[BigArrays.segment(index)][BigArrays.displacement(index)];
   }

   /** @deprecated */
   @Deprecated
   public static void set(int[][] array, long index, int value) {
      array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
   }

   /** @deprecated */
   @Deprecated
   public static void swap(int[][] array, long first, long second) {
      int t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
      array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
      array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
   }

   /** @deprecated */
   @Deprecated
   public static void add(int[][] array, long index, int incr) {
      int[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] += incr;
   }

   /** @deprecated */
   @Deprecated
   public static void mul(int[][] array, long index, int factor) {
      int[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] *= factor;
   }

   /** @deprecated */
   @Deprecated
   public static void incr(int[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]++;
   }

   /** @deprecated */
   @Deprecated
   public static void decr(int[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]--;
   }

   /** @deprecated */
   @Deprecated
   public static long length(int[][] array) {
      int length = array.length;
      return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(int[][] srcArray, long srcPos, int[][] destArray, long destPos, long length) {
      BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyFromBig(int[][] srcArray, long srcPos, int[] destArray, int destPos, int length) {
      BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyToBig(int[] srcArray, int srcPos, int[][] destArray, long destPos, long length) {
      BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
   }

   public static int[][] newBigArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         int[][] base = new int[baseLength][];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new int[134217728];
            }

            base[baseLength - 1] = new int[residual];
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new int[134217728];
            }
         }

         return base;
      }
   }

   public static AtomicIntegerArray[] newBigAtomicArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ATOMIC_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         AtomicIntegerArray[] base = new AtomicIntegerArray[baseLength];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new AtomicIntegerArray(134217728);
            }

            base[baseLength - 1] = new AtomicIntegerArray(residual);
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new AtomicIntegerArray(134217728);
            }
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static int[][] wrap(int[] array) {
      return BigArrays.wrap(array);
   }

   /** @deprecated */
   @Deprecated
   public static int[][] ensureCapacity(int[][] array, long length) {
      return ensureCapacity(array, length, length(array));
   }

   /** @deprecated */
   @Deprecated
   public static int[][] forceCapacity(int[][] array, long length, long preserve) {
      return BigArrays.forceCapacity(array, length, preserve);
   }

   /** @deprecated */
   @Deprecated
   public static int[][] ensureCapacity(int[][] array, long length, long preserve) {
      return length > length(array) ? forceCapacity(array, length, preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static int[][] grow(int[][] array, long length) {
      long oldLength = length(array);
      return length > oldLength ? grow(array, length, oldLength) : array;
   }

   /** @deprecated */
   @Deprecated
   public static int[][] grow(int[][] array, long length, long preserve) {
      long oldLength = length(array);
      return length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static int[][] trim(int[][] array, long length) {
      BigArrays.ensureLength(length);
      long oldLength = length(array);
      if (length >= oldLength) {
         return array;
      } else {
         int baseLength = (int)(length + 134217727L >>> 27);
         int[][] base = (int[][])Arrays.copyOf(array, baseLength);
         int residual = (int)(length & 134217727L);
         if (residual != 0) {
            base[baseLength - 1] = IntArrays.trim(base[baseLength - 1], residual);
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static int[][] setLength(int[][] array, long length) {
      return BigArrays.setLength(array, length);
   }

   /** @deprecated */
   @Deprecated
   public static int[][] copy(int[][] array, long offset, long length) {
      return BigArrays.copy(array, offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static int[][] copy(int[][] array) {
      return BigArrays.copy(array);
   }

   /** @deprecated */
   @Deprecated
   public static void fill(int[][] array, int value) {
      int i = array.length;

      while(i-- != 0) {
         Arrays.fill(array[i], value);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void fill(int[][] array, long from, long to, int value) {
      BigArrays.fill(array, from, to, value);
   }

   /** @deprecated */
   @Deprecated
   public static boolean equals(int[][] a1, int[][] a2) {
      return BigArrays.equals(a1, a2);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(int[][] a) {
      return BigArrays.toString(a);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureFromTo(int[][] a, long from, long to) {
      BigArrays.ensureFromTo(length(a), from, to);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureOffsetLength(int[][] a, long offset, long length) {
      BigArrays.ensureOffsetLength(length(a), offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureSameLength(int[][] a, int[][] b) {
      if (length(a) != length(b)) {
         throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
      }
   }

   private static ForkJoinPool getPool() {
      ForkJoinPool current = ForkJoinTask.getPool();
      return current == null ? ForkJoinPool.commonPool() : current;
   }

   private static void swap(int[][] x, long a, long b, long n) {
      for(int i = 0; (long)i < n; ++b) {
         BigArrays.swap(x, a, b);
         ++i;
         ++a;
      }

   }

   private static long med3(int[][] x, long a, long b, long c, IntComparator comp) {
      int ab = comp.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = comp.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = comp.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(int[][] a, long from, long to, IntComparator comp) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (comp.compare(BigArrays.get(a, j), BigArrays.get(a, m)) < 0) {
               m = j;
            }
         }

         if (m != i) {
            BigArrays.swap(a, i, m);
         }
      }

   }

   public static void quickSort(int[][] x, long from, long to, IntComparator comp) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to, comp);
      } else {
         long m = from + len / 2L;
         if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
               long s = len / 8L;
               l = med3(x, from, from + s, from + 2L * s, comp);
               m = med3(x, m - s, m, m + s, comp);
               n = med3(x, n - 2L * s, n - s, n, comp);
            }

            m = med3(x, l, m, n, comp);
         }

         int v = BigArrays.get(x, m);
         long a = from;
         long b = from;
         long c = to - 1L;
         long d = c;

         while(true) {
            int comparison;
            while(b > c || (comparison = comp.compare(BigArrays.get(x, b), v)) > 0) {
               for(; c >= b && (comparison = comp.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                  if (comparison == 0) {
                     BigArrays.swap(x, c, d--);
                  }
               }

               if (b > c) {
                  long s = Math.min(a - from, b - a);
                  swap(x, from, b - s, s);
                  s = Math.min(d - c, to - d - 1L);
                  swap(x, b, to - s, s);
                  if ((s = b - a) > 1L) {
                     quickSort(x, from, from + s, comp);
                  }

                  if ((s = d - c) > 1L) {
                     quickSort(x, to - s, to, comp);
                  }

                  return;
               }

               BigArrays.swap(x, b++, c--);
            }

            if (comparison == 0) {
               BigArrays.swap(x, a++, b);
            }

            ++b;
         }
      }
   }

   private static long med3(int[][] x, long a, long b, long c) {
      int ab = Integer.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = Integer.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = Integer.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(int[][] a, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (BigArrays.get(a, j) < BigArrays.get(a, m)) {
               m = j;
            }
         }

         if (m != i) {
            BigArrays.swap(a, i, m);
         }
      }

   }

   public static void quickSort(int[][] x, IntComparator comp) {
      quickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static void quickSort(int[][] x, long from, long to) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to);
      } else {
         long m = from + len / 2L;
         if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
               long s = len / 8L;
               l = med3(x, from, from + s, from + 2L * s);
               m = med3(x, m - s, m, m + s);
               n = med3(x, n - 2L * s, n - s, n);
            }

            m = med3(x, l, m, n);
         }

         int v = BigArrays.get(x, m);
         long a = from;
         long b = from;
         long c = to - 1L;
         long d = c;

         while(true) {
            int comparison;
            while(b > c || (comparison = Integer.compare(BigArrays.get(x, b), v)) > 0) {
               for(; c >= b && (comparison = Integer.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                  if (comparison == 0) {
                     BigArrays.swap(x, c, d--);
                  }
               }

               if (b > c) {
                  long s = Math.min(a - from, b - a);
                  swap(x, from, b - s, s);
                  s = Math.min(d - c, to - d - 1L);
                  swap(x, b, to - s, s);
                  if ((s = b - a) > 1L) {
                     quickSort(x, from, from + s);
                  }

                  if ((s = d - c) > 1L) {
                     quickSort(x, to - s, to);
                  }

                  return;
               }

               BigArrays.swap(x, b++, c--);
            }

            if (comparison == 0) {
               BigArrays.swap(x, a++, b);
            }

            ++b;
         }
      }
   }

   public static void quickSort(int[][] x) {
      quickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(int[][] x, long from, long to) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new IntBigArrays.ForkJoinQuickSort(x, from, to));
      } else {
         quickSort(x, from, to);
      }

   }

   public static void parallelQuickSort(int[][] x) {
      parallelQuickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(int[][] x, long from, long to, IntComparator comp) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new IntBigArrays.ForkJoinQuickSortComp(x, from, to, comp));
      } else {
         quickSort(x, from, to, comp);
      }

   }

   public static void parallelQuickSort(int[][] x, IntComparator comp) {
      parallelQuickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static long binarySearch(int[][] a, long from, long to, int key) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         int midVal = BigArrays.get(a, mid);
         if (midVal < key) {
            from = mid + 1L;
         } else {
            if (midVal <= key) {
               return mid;
            }

            to = mid - 1L;
         }
      }

      return -(from + 1L);
   }

   public static long binarySearch(int[][] a, int key) {
      return binarySearch(a, 0L, BigArrays.length(a), key);
   }

   public static long binarySearch(int[][] a, long from, long to, int key, IntComparator c) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         int midVal = BigArrays.get(a, mid);
         int cmp = c.compare(midVal, key);
         if (cmp < 0) {
            from = mid + 1L;
         } else {
            if (cmp <= 0) {
               return mid;
            }

            to = mid - 1L;
         }
      }

      return -(from + 1L);
   }

   public static long binarySearch(int[][] a, int key, IntComparator c) {
      return binarySearch(a, 0L, BigArrays.length(a), key, c);
   }

   public static void radixSort(int[][] a) {
      radixSort(a, 0L, BigArrays.length(a));
   }

   public static void radixSort(int[][] a, long from, long to) {
      int maxLevel = 3;
      int stackSize = 766;
      long[] offsetStack = new long[766];
      int offsetPos = 0;
      long[] lengthStack = new long[766];
      int lengthPos = 0;
      int[] levelStack = new int[766];
      int levelPos = 0;
      offsetPos = offsetPos + 1;
      offsetStack[offsetPos] = from;
      lengthPos = lengthPos + 1;
      lengthStack[lengthPos] = to - from;
      levelPos = levelPos + 1;
      levelStack[levelPos] = 0;
      long[] count = new long[256];
      long[] pos = new long[256];
      byte[][] digit = ByteBigArrays.newBigArray(to - from);

      while(true) {
         while(offsetPos > 0) {
            --offsetPos;
            long first = offsetStack[offsetPos];
            --lengthPos;
            long length = lengthStack[lengthPos];
            --levelPos;
            int level = levelStack[levelPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            if (length < 40L) {
               selectionSort(a, first, first + length);
            } else {
               int shift = (3 - level % 4) * 8;
               long i = length;

               while(i-- != 0L) {
                  BigArrays.set(digit, i, (byte)(BigArrays.get(a, first + i) >>> shift & 255 ^ signMask));
               }

               for(i = length; i-- != 0L; ++count[BigArrays.get(digit, i) & 255]) {
               }

               int lastUsed = -1;
               long p = 0L;

               for(int b = 0; b < 256; ++b) {
                  if (count[b] != 0L) {
                     lastUsed = b;
                     if (level < 3 && count[b] > 1L) {
                        offsetStack[offsetPos++] = p + first;
                        lengthStack[lengthPos++] = count[b];
                        levelStack[levelPos++] = level + 1;
                     }
                  }

                  pos[b] = p += count[b];
               }

               long end = length - count[lastUsed];
               count[lastUsed] = 0L;
               int c;
               for(i = 0L; i < end; count[c] = 0L) {
                  int t = BigArrays.get(a, i + first);
                  c = BigArrays.get(digit, i) & 255;

                  long d;
                  while((d = --pos[c]) > i) {
                     int z = t;
                     int zz = c;
                     t = BigArrays.get(a, d + first);
                     c = BigArrays.get(digit, d) & 255;
                     BigArrays.set(a, d + first, z);
                     BigArrays.set(digit, d, (byte)zz);
                  }

                  BigArrays.set(a, i + first, t);
                  i += count[c];
               }
            }
         }

         return;
      }
   }

   private static void selectionSort(int[][] a, int[][] b, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (BigArrays.get(a, j) < BigArrays.get(a, m) || BigArrays.get(a, j) == BigArrays.get(a, m) && BigArrays.get(b, j) < BigArrays.get(b, m)) {
               m = j;
            }
         }

         if (m != i) {
            int t = BigArrays.get(a, i);
            BigArrays.set(a, i, BigArrays.get(a, m));
            BigArrays.set(a, m, t);
            t = BigArrays.get(b, i);
            BigArrays.set(b, i, BigArrays.get(b, m));
            BigArrays.set(b, m, t);
         }
      }

   }

   public static void radixSort(int[][] a, int[][] b) {
      radixSort(a, b, 0L, BigArrays.length(a));
   }

   public static void radixSort(int[][] a, int[][] b, long from, long to) {
      int layers = 2;
      if (BigArrays.length(a) != BigArrays.length(b)) {
         throw new IllegalArgumentException("Array size mismatch.");
      } else {
         int maxLevel = 3;
         int stackSize = 766;
         long[] offsetStack = new long[1786];
         int offsetPos = 0;
         long[] lengthStack = new long[1786];
         int lengthPos = 0;
         int[] levelStack = new int[1786];
         int levelPos = 0;
         offsetPos = offsetPos + 1;
         offsetStack[offsetPos] = from;
         lengthPos = lengthPos + 1;
         lengthStack[lengthPos] = to - from;
         levelPos = levelPos + 1;
         levelStack[levelPos] = 0;
         long[] count = new long[256];
         long[] pos = new long[256];
         byte[][] digit = ByteBigArrays.newBigArray(to - from);

         while(true) {
            while(offsetPos > 0) {
               --offsetPos;
               long first = offsetStack[offsetPos];
               --lengthPos;
               long length = lengthStack[lengthPos];
               --levelPos;
               int level = levelStack[levelPos];
               int signMask = level % 4 == 0 ? 128 : 0;
               if (length < 40L) {
                  selectionSort(a, b, first, first + length);
               } else {
                  int[][] k = level < 4 ? a : b;
                  int shift = (3 - level % 4) * 8;
                  long i = length;

                  while(i-- != 0L) {
                     BigArrays.set(digit, i, (byte)(BigArrays.get(k, first + i) >>> shift & 255 ^ signMask));
                  }

                  for(i = length; i-- != 0L; ++count[BigArrays.get(digit, i) & 255]) {
                  }

                  int lastUsed = -1;
                  long p = 0L;

                  for(int j = 0; j < 256; ++j) {
                     if (count[j] != 0L) {
                        lastUsed = j;
                        if (level < 7 && count[j] > 1L) {
                           offsetStack[offsetPos++] = p + first;
                           lengthStack[lengthPos++] = count[j];
                           levelStack[levelPos++] = level + 1;
                        }
                     }

                     pos[j] = p += count[j];
                  }

                  long end = length - count[lastUsed];
                  count[lastUsed] = 0L;
                  int c;
                  for(i = 0L; i < end; count[c] = 0L) {
                     int t = BigArrays.get(a, i + first);
                     int u = BigArrays.get(b, i + first);
                     c = BigArrays.get(digit, i) & 255;

                     long d;
                     while((d = --pos[c]) > i) {
                        int z = t;
                        int zz = c;
                        t = BigArrays.get(a, d + first);
                        BigArrays.set(a, d + first, z);
                        z = u;
                        u = BigArrays.get(b, d + first);
                        BigArrays.set(b, d + first, z);
                        c = BigArrays.get(digit, d) & 255;
                        BigArrays.set(digit, d, (byte)zz);
                     }

                     BigArrays.set(a, i + first, t);
                     BigArrays.set(b, i + first, u);
                     i += count[c];
                  }
               }
            }

            return;
         }
      }
   }

   private static void insertionSortIndirect(long[][] perm, int[][] a, int[][] b, long from, long to) {
      long t;
      long j;
      for(long i = from; ++i < to; BigArrays.set(perm, j, t)) {
         t = BigArrays.get(perm, i);
         j = i;

         for(long u = BigArrays.get(perm, i - 1L); BigArrays.get(a, t) < BigArrays.get(a, u) || BigArrays.get(a, t) == BigArrays.get(a, u) && BigArrays.get(b, t) < BigArrays.get(b, u); u = BigArrays.get(perm, --j - 1L)) {
            BigArrays.set(perm, j, u);
            if (from == j - 1L) {
               --j;
               break;
            }
         }
      }

   }

   public static void radixSortIndirect(long[][] perm, int[][] a, int[][] b, boolean stable) {
      ensureSameLength(a, b);
      radixSortIndirect(perm, a, b, 0L, BigArrays.length(a), stable);
   }

   public static void radixSortIndirect(long[][] perm, int[][] a, int[][] b, long from, long to, boolean stable) {
      if (to - from < 1024L) {
         insertionSortIndirect(perm, a, b, from, to);
      } else {
         int layers = 2;
         int maxLevel = 3;
         int stackSize = 766;
         int stackPos = 0;
         long[] offsetStack = new long[1786];
         long[] lengthStack = new long[1786];
         int[] levelStack = new int[1786];
         offsetStack[stackPos] = from;
         lengthStack[stackPos] = to - from;
         stackPos = stackPos + 1;
         levelStack[stackPos] = 0;
         long[] count = new long[256];
         long[] pos = new long[256];
         long[][] support = stable ? LongBigArrays.newBigArray(BigArrays.length(perm)) : null;

         while(true) {
            while(stackPos > 0) {
               --stackPos;
               long first = offsetStack[stackPos];
               long length = lengthStack[stackPos];
               int level = levelStack[stackPos];
               int signMask = level % 4 == 0 ? 128 : 0;
               int[][] k = level < 4 ? a : b;
               int shift = (3 - level % 4) * 8;

               for(long i = first + length; i-- != first; ++count[BigArrays.get(k, BigArrays.get(perm, i)) >>> shift & 255 ^ signMask]) {
               }

               int lastUsed = -1;
               long p = stable ? 0L : first;

               int i;
               for(i = 0; i < 256; ++i) {
                  if (count[i] != 0L) {
                     lastUsed = i;
                  }

                  pos[i] = p += count[i];
               }

               long end;
               if (stable) {
                  end = first + length;

                  while(end-- != first) {
                     BigArrays.set(support, --pos[BigArrays.get(k, BigArrays.get(perm, end)) >>> shift & 255 ^ signMask], BigArrays.get(perm, end));
                  }

                  BigArrays.copy(support, 0L, perm, first, length);
                  p = first;

                  for(i = 0; i < 256; ++i) {
                     if (level < 7 && count[i] > 1L) {
                        if (count[i] < 1024L) {
                           insertionSortIndirect(perm, a, b, p, p + count[i]);
                        } else {
                           offsetStack[stackPos] = p;
                           lengthStack[stackPos] = count[i];
                           levelStack[stackPos++] = level + 1;
                        }
                     }

                     p += count[i];
                  }

                  Arrays.fill(count, 0L);
               } else {
                  end = first + length - count[lastUsed];
                  int c;
                  for(long idx = first; idx <= end; count[c] = 0L) {
                     long t = BigArrays.get(perm, idx);
                     c = BigArrays.get(k, t) >>> shift & 255 ^ signMask;
                     if (idx < end) {
                        long d;
                        while((d = --pos[c]) > idx) {
                           long z = t;
                           t = BigArrays.get(perm, d);
                           BigArrays.set(perm, d, z);
                           c = BigArrays.get(k, t) >>> shift & 255 ^ signMask;
                        }

                        BigArrays.set(perm, idx, t);
                     }

                     if (level < 7 && count[c] > 1L) {
                        if (count[c] < 1024L) {
                           insertionSortIndirect(perm, a, b, idx, idx + count[c]);
                        } else {
                           offsetStack[stackPos] = idx;
                           lengthStack[stackPos] = count[c];
                           levelStack[stackPos++] = level + 1;
                        }
                     }

                     idx += count[c];
                  }
               }
            }

            return;
         }
      }
   }

   public static int[][] shuffle(int[][] a, long from, long to, Random random) {
      return BigArrays.shuffle(a, from, to, random);
   }

   public static int[][] shuffle(int[][] a, Random random) {
      return BigArrays.shuffle(a, random);
   }

   protected static class ForkJoinQuickSort extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final long from;
      private final long to;
      private final int[][] x;

      public ForkJoinQuickSort(int[][] x, long from, long to) {
         this.from = from;
         this.to = to;
         this.x = x;
      }

      protected void compute() {
         int[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            IntBigArrays.quickSort(x, this.from, this.to);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = IntBigArrays.med3(x, l, l + s, l + 2L * s);
            m = IntBigArrays.med3(x, m - s, m, m + s);
            n = IntBigArrays.med3(x, n - 2L * s, n - s, n);
            m = IntBigArrays.med3(x, l, m, n);
            int v = BigArrays.get(x, m);
            long a = this.from;
            long b = a;
            long c = this.to - 1L;
            long d = c;

            while(true) {
               int comparison;
               while(b > c || (comparison = Integer.compare(BigArrays.get(x, b), v)) > 0) {
                  for(; c >= b && (comparison = Integer.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                     if (comparison == 0) {
                        BigArrays.swap(x, c, d--);
                     }
                  }

                  if (b > c) {
                     s = Math.min(a - this.from, b - a);
                     IntBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     IntBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new IntBigArrays.ForkJoinQuickSort(x, this.from, this.from + s), new IntBigArrays.ForkJoinQuickSort(x, this.to - t, this.to));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new IntBigArrays.ForkJoinQuickSort(x, this.from, this.from + s)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new IntBigArrays.ForkJoinQuickSort(x, this.to - t, this.to)});
                     }

                     return;
                  }

                  BigArrays.swap(x, b++, c--);
               }

               if (comparison == 0) {
                  BigArrays.swap(x, a++, b);
               }

               ++b;
            }
         }
      }
   }

   protected static class ForkJoinQuickSortComp extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final long from;
      private final long to;
      private final int[][] x;
      private final IntComparator comp;

      public ForkJoinQuickSortComp(int[][] x, long from, long to, IntComparator comp) {
         this.from = from;
         this.to = to;
         this.x = x;
         this.comp = comp;
      }

      protected void compute() {
         int[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            IntBigArrays.quickSort(x, this.from, this.to, this.comp);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = IntBigArrays.med3(x, l, l + s, l + 2L * s, this.comp);
            m = IntBigArrays.med3(x, m - s, m, m + s, this.comp);
            n = IntBigArrays.med3(x, n - 2L * s, n - s, n, this.comp);
            m = IntBigArrays.med3(x, l, m, n, this.comp);
            int v = BigArrays.get(x, m);
            long a = this.from;
            long b = a;
            long c = this.to - 1L;
            long d = c;

            while(true) {
               int comparison;
               while(b > c || (comparison = this.comp.compare(BigArrays.get(x, b), v)) > 0) {
                  for(; c >= b && (comparison = this.comp.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                     if (comparison == 0) {
                        BigArrays.swap(x, c, d--);
                     }
                  }

                  if (b > c) {
                     s = Math.min(a - this.from, b - a);
                     IntBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     IntBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new IntBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new IntBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new IntBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new IntBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
                     }

                     return;
                  }

                  BigArrays.swap(x, b++, c--);
               }

               if (comparison == 0) {
                  BigArrays.swap(x, a++, b);
               }

               ++b;
            }
         }
      }
   }

   private static final class BigArrayHashStrategy implements Serializable, Hash.Strategy<int[][]> {
      private static final long serialVersionUID = -7046029254386353129L;

      private BigArrayHashStrategy() {
      }

      public int hashCode(int[][] o) {
         return Arrays.deepHashCode(o);
      }

      public boolean equals(int[][] a, int[][] b) {
         return IntBigArrays.equals(a, b);
      }

      // $FF: synthetic method
      BigArrayHashStrategy(Object x0) {
         this();
      }
   }
}
