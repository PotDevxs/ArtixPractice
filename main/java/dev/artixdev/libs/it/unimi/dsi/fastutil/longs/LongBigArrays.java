package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicLongArray;
import dev.artixdev.libs.it.unimi.dsi.fastutil.BigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Hash;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.ByteBigArrays;

public final class LongBigArrays {
   public static final long[][] EMPTY_BIG_ARRAY = new long[0][];
   public static final long[][] DEFAULT_EMPTY_BIG_ARRAY = new long[0][];
   public static final AtomicLongArray[] EMPTY_BIG_ATOMIC_ARRAY = new AtomicLongArray[0];
   public static final Hash.Strategy HASH_STRATEGY = new LongBigArrays.BigArrayHashStrategy();
   private static final int QUICKSORT_NO_REC = 7;
   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
   private static final int MEDIUM = 40;
   private static final int DIGIT_BITS = 8;
   private static final int DIGIT_MASK = 255;
   private static final int DIGITS_PER_ELEMENT = 8;
   private static final int RADIXSORT_NO_REC = 1024;

   private LongBigArrays() {
   }

   /** @deprecated */
   @Deprecated
   public static long get(long[][] array, long index) {
      return array[BigArrays.segment(index)][BigArrays.displacement(index)];
   }

   /** @deprecated */
   @Deprecated
   public static void set(long[][] array, long index, long value) {
      array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
   }

   /** @deprecated */
   @Deprecated
   public static void swap(long[][] array, long first, long second) {
      long t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
      array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
      array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
   }

   /** @deprecated */
   @Deprecated
   public static void add(long[][] array, long index, long incr) {
      long[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] += incr;
   }

   /** @deprecated */
   @Deprecated
   public static void mul(long[][] array, long index, long factor) {
      long[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] *= factor;
   }

   /** @deprecated */
   @Deprecated
   public static void incr(long[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]++;
   }

   /** @deprecated */
   @Deprecated
   public static void decr(long[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]--;
   }

   /** @deprecated */
   @Deprecated
   public static long length(long[][] array) {
      int length = array.length;
      return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(long[][] srcArray, long srcPos, long[][] destArray, long destPos, long length) {
      BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyFromBig(long[][] srcArray, long srcPos, long[] destArray, int destPos, int length) {
      BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyToBig(long[] srcArray, int srcPos, long[][] destArray, long destPos, long length) {
      BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
   }

   public static long[][] newBigArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         long[][] base = new long[baseLength][];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new long[134217728];
            }

            base[baseLength - 1] = new long[residual];
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new long[134217728];
            }
         }

         return base;
      }
   }

   public static AtomicLongArray[] newBigAtomicArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ATOMIC_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         AtomicLongArray[] base = new AtomicLongArray[baseLength];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new AtomicLongArray(134217728);
            }

            base[baseLength - 1] = new AtomicLongArray(residual);
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new AtomicLongArray(134217728);
            }
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static long[][] wrap(long[] array) {
      return BigArrays.wrap(array);
   }

   /** @deprecated */
   @Deprecated
   public static long[][] ensureCapacity(long[][] array, long length) {
      return ensureCapacity(array, length, length(array));
   }

   /** @deprecated */
   @Deprecated
   public static long[][] forceCapacity(long[][] array, long length, long preserve) {
      return BigArrays.forceCapacity(array, length, preserve);
   }

   /** @deprecated */
   @Deprecated
   public static long[][] ensureCapacity(long[][] array, long length, long preserve) {
      return length > length(array) ? forceCapacity(array, length, preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static long[][] grow(long[][] array, long length) {
      long oldLength = length(array);
      return length > oldLength ? grow(array, length, oldLength) : array;
   }

   /** @deprecated */
   @Deprecated
   public static long[][] grow(long[][] array, long length, long preserve) {
      long oldLength = length(array);
      return length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static long[][] trim(long[][] array, long length) {
      BigArrays.ensureLength(length);
      long oldLength = length(array);
      if (length >= oldLength) {
         return array;
      } else {
         int baseLength = (int)(length + 134217727L >>> 27);
         long[][] base = (long[][])Arrays.copyOf(array, baseLength);
         int residual = (int)(length & 134217727L);
         if (residual != 0) {
            base[baseLength - 1] = LongArrays.trim(base[baseLength - 1], residual);
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static long[][] setLength(long[][] array, long length) {
      return BigArrays.setLength(array, length);
   }

   /** @deprecated */
   @Deprecated
   public static long[][] copy(long[][] array, long offset, long length) {
      return BigArrays.copy(array, offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static long[][] copy(long[][] array) {
      return BigArrays.copy(array);
   }

   /** @deprecated */
   @Deprecated
   public static void fill(long[][] array, long value) {
      int i = array.length;

      while(i-- != 0) {
         Arrays.fill(array[i], value);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void fill(long[][] array, long from, long to, long value) {
      BigArrays.fill(array, from, to, value);
   }

   /** @deprecated */
   @Deprecated
   public static boolean equals(long[][] a1, long[][] a2) {
      return BigArrays.equals(a1, a2);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(long[][] a) {
      return BigArrays.toString(a);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureFromTo(long[][] a, long from, long to) {
      BigArrays.ensureFromTo(length(a), from, to);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureOffsetLength(long[][] a, long offset, long length) {
      BigArrays.ensureOffsetLength(length(a), offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureSameLength(long[][] a, long[][] b) {
      if (length(a) != length(b)) {
         throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
      }
   }

   private static ForkJoinPool getPool() {
      ForkJoinPool current = ForkJoinTask.getPool();
      return current == null ? ForkJoinPool.commonPool() : current;
   }

   private static void swap(long[][] x, long a, long b, long n) {
      for(int i = 0; (long)i < n; ++b) {
         BigArrays.swap(x, a, b);
         ++i;
         ++a;
      }

   }

   private static long med3(long[][] x, long a, long b, long c, LongComparator comp) {
      int ab = comp.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = comp.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = comp.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(long[][] a, long from, long to, LongComparator comp) {
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

   public static void quickSort(long[][] x, long from, long to, LongComparator comp) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to, comp);
      } else {
         long m = from + len / 2L;
         long v;
         long a;
         long b;
         if (len > 7L) {
            v = from;
            a = to - 1L;
            if (len > 40L) {
               b = len / 8L;
               v = med3(x, from, from + b, from + 2L * b, comp);
               m = med3(x, m - b, m, m + b, comp);
               a = med3(x, a - 2L * b, a - b, a, comp);
            }

            m = med3(x, v, m, a, comp);
         }

         v = BigArrays.get(x, m);
         a = from;
         b = from;
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

   private static long med3(long[][] x, long a, long b, long c) {
      int ab = Long.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = Long.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = Long.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(long[][] a, long from, long to) {
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

   public static void quickSort(long[][] x, LongComparator comp) {
      quickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static void quickSort(long[][] x, long from, long to) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to);
      } else {
         long m = from + len / 2L;
         long v;
         long a;
         long b;
         if (len > 7L) {
            v = from;
            a = to - 1L;
            if (len > 40L) {
               b = len / 8L;
               v = med3(x, from, from + b, from + 2L * b);
               m = med3(x, m - b, m, m + b);
               a = med3(x, a - 2L * b, a - b, a);
            }

            m = med3(x, v, m, a);
         }

         v = BigArrays.get(x, m);
         a = from;
         b = from;
         long c = to - 1L;
         long d = c;

         while(true) {
            int comparison;
            while(b > c || (comparison = Long.compare(BigArrays.get(x, b), v)) > 0) {
               for(; c >= b && (comparison = Long.compare(BigArrays.get(x, c), v)) >= 0; --c) {
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

   public static void quickSort(long[][] x) {
      quickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(long[][] x, long from, long to) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new LongBigArrays.ForkJoinQuickSort(x, from, to));
      } else {
         quickSort(x, from, to);
      }

   }

   public static void parallelQuickSort(long[][] x) {
      parallelQuickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(long[][] x, long from, long to, LongComparator comp) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new LongBigArrays.ForkJoinQuickSortComp(x, from, to, comp));
      } else {
         quickSort(x, from, to, comp);
      }

   }

   public static void parallelQuickSort(long[][] x, LongComparator comp) {
      parallelQuickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static long binarySearch(long[][] a, long from, long to, long key) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         long midVal = BigArrays.get(a, mid);
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

   public static long binarySearch(long[][] a, long key) {
      return binarySearch(a, 0L, BigArrays.length(a), key);
   }

   public static long binarySearch(long[][] a, long from, long to, long key, LongComparator c) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         long midVal = BigArrays.get(a, mid);
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

   public static long binarySearch(long[][] a, long key, LongComparator c) {
      return binarySearch(a, 0L, BigArrays.length(a), key, c);
   }

   public static void radixSort(long[][] a) {
      radixSort(a, 0L, BigArrays.length(a));
   }

   public static void radixSort(long[][] a, long from, long to) {
      int maxLevel = true;
      int stackSize = true;
      long[] offsetStack = new long[1786];
      int offsetPos = 0;
      long[] lengthStack = new long[1786];
      int lengthPos = 0;
      int[] levelStack = new int[1786];
      int levelPos = 0;
      int offsetPos = offsetPos + 1;
      offsetStack[offsetPos] = from;
      int lengthPos = lengthPos + 1;
      lengthStack[lengthPos] = to - from;
      int levelPos = levelPos + 1;
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
            int signMask = level % 8 == 0 ? 128 : 0;
            if (length < 40L) {
               selectionSort(a, first, first + length);
            } else {
               int shift = (7 - level % 8) * 8;
               long i = length;

               while(i-- != 0L) {
                  BigArrays.set(digit, i, (byte)((int)(BigArrays.get(a, first + i) >>> shift & 255L ^ (long)signMask)));
               }

               for(i = length; i-- != 0L; ++count[BigArrays.get(digit, i) & 255]) {
               }

               int lastUsed = -1;
               long p = 0L;

               for(int i = 0; i < 256; ++i) {
                  if (count[i] != 0L) {
                     lastUsed = i;
                     if (level < 7 && count[i] > 1L) {
                        offsetStack[offsetPos++] = p + first;
                        lengthStack[lengthPos++] = count[i];
                        levelStack[levelPos++] = level + 1;
                     }
                  }

                  pos[i] = p += count[i];
               }

               long end = length - count[lastUsed];
               count[lastUsed] = 0L;
               int c = true;

               int c;
               for(long i = 0L; i < end; count[c] = 0L) {
                  long t = BigArrays.get(a, i + first);
                  c = BigArrays.get(digit, i) & 255;

                  long d;
                  while((d = --pos[c]) > i) {
                     long z = t;
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

   private static void selectionSort(long[][] a, long[][] b, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         long t;
         for(t = i + 1L; t < to; ++t) {
            if (BigArrays.get(a, t) < BigArrays.get(a, m) || BigArrays.get(a, t) == BigArrays.get(a, m) && BigArrays.get(b, t) < BigArrays.get(b, m)) {
               m = t;
            }
         }

         if (m != i) {
            t = BigArrays.get(a, i);
            BigArrays.set(a, i, BigArrays.get(a, m));
            BigArrays.set(a, m, t);
            t = BigArrays.get(b, i);
            BigArrays.set(b, i, BigArrays.get(b, m));
            BigArrays.set(b, m, t);
         }
      }

   }

   public static void radixSort(long[][] a, long[][] b) {
      radixSort(a, b, 0L, BigArrays.length(a));
   }

   public static void radixSort(long[][] a, long[][] b, long from, long to) {
      int layers = true;
      if (BigArrays.length(a) != BigArrays.length(b)) {
         throw new IllegalArgumentException("Array size mismatch.");
      } else {
         int maxLevel = true;
         int stackSize = true;
         long[] offsetStack = new long[3826];
         int offsetPos = 0;
         long[] lengthStack = new long[3826];
         int lengthPos = 0;
         int[] levelStack = new int[3826];
         int levelPos = 0;
         int offsetPos = offsetPos + 1;
         offsetStack[offsetPos] = from;
         int lengthPos = lengthPos + 1;
         lengthStack[lengthPos] = to - from;
         int levelPos = levelPos + 1;
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
               int signMask = level % 8 == 0 ? 128 : 0;
               if (length < 40L) {
                  selectionSort(a, b, first, first + length);
               } else {
                  long[][] k = level < 8 ? a : b;
                  int shift = (7 - level % 8) * 8;
                  long i = length;

                  while(i-- != 0L) {
                     BigArrays.set(digit, i, (byte)((int)(BigArrays.get(k, first + i) >>> shift & 255L ^ (long)signMask)));
                  }

                  for(i = length; i-- != 0L; ++count[BigArrays.get(digit, i) & 255]) {
                  }

                  int lastUsed = -1;
                  long p = 0L;

                  for(int i = 0; i < 256; ++i) {
                     if (count[i] != 0L) {
                        lastUsed = i;
                        if (level < 15 && count[i] > 1L) {
                           offsetStack[offsetPos++] = p + first;
                           lengthStack[lengthPos++] = count[i];
                           levelStack[levelPos++] = level + 1;
                        }
                     }

                     pos[i] = p += count[i];
                  }

                  long end = length - count[lastUsed];
                  count[lastUsed] = 0L;
                  int c = true;

                  int c;
                  for(long i = 0L; i < end; count[c] = 0L) {
                     long t = BigArrays.get(a, i + first);
                     long u = BigArrays.get(b, i + first);
                     c = BigArrays.get(digit, i) & 255;

                     long d;
                     while((d = --pos[c]) > i) {
                        long z = t;
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

   private static void insertionSortIndirect(long[][] perm, long[][] a, long[][] b, long from, long to) {
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

   public static void radixSortIndirect(long[][] perm, long[][] a, long[][] b, boolean stable) {
      ensureSameLength(a, b);
      radixSortIndirect(perm, a, b, 0L, BigArrays.length(a), stable);
   }

   public static void radixSortIndirect(long[][] perm, long[][] a, long[][] b, long from, long to, boolean stable) {
      if (to - from < 1024L) {
         insertionSortIndirect(perm, a, b, from, to);
      } else {
         int layers = true;
         int maxLevel = true;
         int stackSize = true;
         int stackPos = 0;
         long[] offsetStack = new long[3826];
         long[] lengthStack = new long[3826];
         int[] levelStack = new int[3826];
         offsetStack[stackPos] = from;
         lengthStack[stackPos] = to - from;
         int stackPos = stackPos + 1;
         levelStack[stackPos] = 0;
         long[] count = new long[256];
         long[] pos = new long[256];
         long[][] support = stable ? newBigArray(BigArrays.length(perm)) : null;

         while(true) {
            while(stackPos > 0) {
               --stackPos;
               long first = offsetStack[stackPos];
               long length = lengthStack[stackPos];
               int level = levelStack[stackPos];
               int signMask = level % 8 == 0 ? 128 : 0;
               long[][] k = level < 8 ? a : b;
               int shift = (7 - level % 8) * 8;

               for(long i = first + length; i-- != first; ++count[(int)(BigArrays.get(k, BigArrays.get(perm, i)) >>> shift & 255L ^ (long)signMask)]) {
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
                     BigArrays.set(support, --pos[(int)(BigArrays.get(k, BigArrays.get(perm, end)) >>> shift & 255L ^ (long)signMask)], BigArrays.get(perm, end));
                  }

                  BigArrays.copy(support, 0L, perm, first, length);
                  p = first;

                  for(i = 0; i < 256; ++i) {
                     if (level < 15 && count[i] > 1L) {
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
                  int c = true;

                  int c;
                  for(long i = first; i <= end; count[c] = 0L) {
                     long t = BigArrays.get(perm, i);
                     c = (int)(BigArrays.get(k, t) >>> shift & 255L ^ (long)signMask);
                     if (i < end) {
                        long d;
                        while((d = --pos[c]) > i) {
                           long z = t;
                           t = BigArrays.get(perm, d);
                           BigArrays.set(perm, d, z);
                           c = (int)(BigArrays.get(k, t) >>> shift & 255L ^ (long)signMask);
                        }

                        BigArrays.set(perm, i, t);
                     }

                     if (level < 15 && count[c] > 1L) {
                        if (count[c] < 1024L) {
                           insertionSortIndirect(perm, a, b, i, i + count[c]);
                        } else {
                           offsetStack[stackPos] = i;
                           lengthStack[stackPos] = count[c];
                           levelStack[stackPos++] = level + 1;
                        }
                     }

                     i += count[c];
                  }
               }
            }

            return;
         }
      }
   }

   public static long[][] shuffle(long[][] a, long from, long to, Random random) {
      return BigArrays.shuffle(a, from, to, random);
   }

   public static long[][] shuffle(long[][] a, Random random) {
      return BigArrays.shuffle(a, random);
   }

   protected static class ForkJoinQuickSort extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final long from;
      private final long to;
      private final long[][] x;

      public ForkJoinQuickSort(long[][] x, long from, long to) {
         this.from = from;
         this.to = to;
         this.x = x;
      }

      protected void compute() {
         long[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            LongBigArrays.quickSort(x, this.from, this.to);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = LongBigArrays.med3(x, l, l + s, l + 2L * s);
            m = LongBigArrays.med3(x, m - s, m, m + s);
            n = LongBigArrays.med3(x, n - 2L * s, n - s, n);
            m = LongBigArrays.med3(x, l, m, n);
            long v = BigArrays.get(x, m);
            long a = this.from;
            long b = a;
            long c = this.to - 1L;
            long d = c;

            while(true) {
               int comparison;
               while(b > c || (comparison = Long.compare(BigArrays.get(x, b), v)) > 0) {
                  for(; c >= b && (comparison = Long.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                     if (comparison == 0) {
                        BigArrays.swap(x, c, d--);
                     }
                  }

                  if (b > c) {
                     s = Math.min(a - this.from, b - a);
                     LongBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     LongBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new LongBigArrays.ForkJoinQuickSort(x, this.from, this.from + s), new LongBigArrays.ForkJoinQuickSort(x, this.to - t, this.to));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new LongBigArrays.ForkJoinQuickSort(x, this.from, this.from + s)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new LongBigArrays.ForkJoinQuickSort(x, this.to - t, this.to)});
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
      private final long[][] x;
      private final LongComparator comp;

      public ForkJoinQuickSortComp(long[][] x, long from, long to, LongComparator comp) {
         this.from = from;
         this.to = to;
         this.x = x;
         this.comp = comp;
      }

      protected void compute() {
         long[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            LongBigArrays.quickSort(x, this.from, this.to, this.comp);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = LongBigArrays.med3(x, l, l + s, l + 2L * s, this.comp);
            m = LongBigArrays.med3(x, m - s, m, m + s, this.comp);
            n = LongBigArrays.med3(x, n - 2L * s, n - s, n, this.comp);
            m = LongBigArrays.med3(x, l, m, n, this.comp);
            long v = BigArrays.get(x, m);
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
                     LongBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     LongBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new LongBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new LongBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new LongBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new LongBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
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

   private static final class BigArrayHashStrategy implements Serializable, Hash.Strategy<long[][]> {
      private static final long serialVersionUID = -7046029254386353129L;

      private BigArrayHashStrategy() {
      }

      public int hashCode(long[][] o) {
         return Arrays.deepHashCode(o);
      }

      public boolean equals(long[][] a, long[][] b) {
         return LongBigArrays.equals(a, b);
      }

      // $FF: synthetic method
      BigArrayHashStrategy(Object x0) {
         this();
      }
   }
}
