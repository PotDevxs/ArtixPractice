package dev.artixdev.libs.it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.BigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Hash;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.LongBigArrays;

public final class DoubleBigArrays {
   public static final double[][] EMPTY_BIG_ARRAY = new double[0][];
   public static final double[][] DEFAULT_EMPTY_BIG_ARRAY = new double[0][];
   public static final Hash.Strategy HASH_STRATEGY = new DoubleBigArrays.BigArrayHashStrategy();
   private static final int QUICKSORT_NO_REC = 7;
   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
   private static final int MEDIUM = 40;
   private static final int DIGIT_BITS = 8;
   private static final int DIGIT_MASK = 255;
   private static final int DIGITS_PER_ELEMENT = 8;
   private static final int RADIXSORT_NO_REC = 1024;

   private DoubleBigArrays() {
   }

   /** @deprecated */
   @Deprecated
   public static double get(double[][] array, long index) {
      return array[BigArrays.segment(index)][BigArrays.displacement(index)];
   }

   /** @deprecated */
   @Deprecated
   public static void set(double[][] array, long index, double value) {
      array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
   }

   /** @deprecated */
   @Deprecated
   public static void swap(double[][] array, long first, long second) {
      double t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
      array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
      array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
   }

   /** @deprecated */
   @Deprecated
   public static void add(double[][] array, long index, double incr) {
      double[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] += incr;
   }

   /** @deprecated */
   @Deprecated
   public static void mul(double[][] array, long index, double factor) {
      double[] var10000 = array[BigArrays.segment(index)];
      int var10001 = BigArrays.displacement(index);
      var10000[var10001] *= factor;
   }

   /** @deprecated */
   @Deprecated
   public static void incr(double[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]++;
   }

   /** @deprecated */
   @Deprecated
   public static void decr(double[][] array, long index) {
      int var10002 = array[BigArrays.segment(index)][BigArrays.displacement(index)]--;
   }

   /** @deprecated */
   @Deprecated
   public static long length(double[][] array) {
      int length = array.length;
      return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(double[][] srcArray, long srcPos, double[][] destArray, long destPos, long length) {
      BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyFromBig(double[][] srcArray, long srcPos, double[] destArray, int destPos, int length) {
      BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyToBig(double[] srcArray, int srcPos, double[][] destArray, long destPos, long length) {
      BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
   }

   public static double[][] newBigArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         double[][] base = new double[baseLength][];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new double[134217728];
            }

            base[baseLength - 1] = new double[residual];
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new double[134217728];
            }
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static double[][] wrap(double[] array) {
      return BigArrays.wrap(array);
   }

   /** @deprecated */
   @Deprecated
   public static double[][] ensureCapacity(double[][] array, long length) {
      return ensureCapacity(array, length, length(array));
   }

   /** @deprecated */
   @Deprecated
   public static double[][] forceCapacity(double[][] array, long length, long preserve) {
      return BigArrays.forceCapacity(array, length, preserve);
   }

   /** @deprecated */
   @Deprecated
   public static double[][] ensureCapacity(double[][] array, long length, long preserve) {
      return length > length(array) ? forceCapacity(array, length, preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static double[][] grow(double[][] array, long length) {
      long oldLength = length(array);
      return length > oldLength ? grow(array, length, oldLength) : array;
   }

   /** @deprecated */
   @Deprecated
   public static double[][] grow(double[][] array, long length, long preserve) {
      long oldLength = length(array);
      return length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static double[][] trim(double[][] array, long length) {
      BigArrays.ensureLength(length);
      long oldLength = length(array);
      if (length >= oldLength) {
         return array;
      } else {
         int baseLength = (int)(length + 134217727L >>> 27);
         double[][] base = (double[][])Arrays.copyOf(array, baseLength);
         int residual = (int)(length & 134217727L);
         if (residual != 0) {
            base[baseLength - 1] = DoubleArrays.trim(base[baseLength - 1], residual);
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static double[][] setLength(double[][] array, long length) {
      return BigArrays.setLength(array, length);
   }

   /** @deprecated */
   @Deprecated
   public static double[][] copy(double[][] array, long offset, long length) {
      return BigArrays.copy(array, offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static double[][] copy(double[][] array) {
      return BigArrays.copy(array);
   }

   /** @deprecated */
   @Deprecated
   public static void fill(double[][] array, double value) {
      int i = array.length;

      while(i-- != 0) {
         Arrays.fill(array[i], value);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void fill(double[][] array, long from, long to, double value) {
      BigArrays.fill(array, from, to, value);
   }

   /** @deprecated */
   @Deprecated
   public static boolean equals(double[][] a1, double[][] a2) {
      return BigArrays.equals(a1, a2);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(double[][] a) {
      return BigArrays.toString(a);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureFromTo(double[][] a, long from, long to) {
      BigArrays.ensureFromTo(length(a), from, to);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureOffsetLength(double[][] a, long offset, long length) {
      BigArrays.ensureOffsetLength(length(a), offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureSameLength(double[][] a, double[][] b) {
      if (length(a) != length(b)) {
         throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
      }
   }

   private static ForkJoinPool getPool() {
      ForkJoinPool current = ForkJoinTask.getPool();
      return current == null ? ForkJoinPool.commonPool() : current;
   }

   private static void swap(double[][] x, long a, long b, long n) {
      for(int i = 0; (long)i < n; ++b) {
         BigArrays.swap(x, a, b);
         ++i;
         ++a;
      }

   }

   private static long med3(double[][] x, long a, long b, long c, DoubleComparator comp) {
      int ab = comp.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = comp.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = comp.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(double[][] a, long from, long to, DoubleComparator comp) {
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

   public static void quickSort(double[][] x, long from, long to, DoubleComparator comp) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to, comp);
      } else {
         long m = from + len / 2L;
         long a;
         long b;
         if (len > 7L) {
            long l = from;
            a = to - 1L;
            if (len > 40L) {
               b = len / 8L;
               l = med3(x, from, from + b, from + 2L * b, comp);
               m = med3(x, m - b, m, m + b, comp);
               a = med3(x, a - 2L * b, a - b, a, comp);
            }

            m = med3(x, l, m, a, comp);
         }

         double v = BigArrays.get(x, m);
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

   private static long med3(double[][] x, long a, long b, long c) {
      int ab = Double.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = Double.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = Double.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(double[][] a, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (Double.compare(BigArrays.get(a, j), BigArrays.get(a, m)) < 0) {
               m = j;
            }
         }

         if (m != i) {
            BigArrays.swap(a, i, m);
         }
      }

   }

   public static void quickSort(double[][] x, DoubleComparator comp) {
      quickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static void quickSort(double[][] x, long from, long to) {
      long len = to - from;
      if (len < 7L) {
         selectionSort(x, from, to);
      } else {
         long m = from + len / 2L;
         long a;
         long b;
         if (len > 7L) {
            long l = from;
            a = to - 1L;
            if (len > 40L) {
               b = len / 8L;
               l = med3(x, from, from + b, from + 2L * b);
               m = med3(x, m - b, m, m + b);
               a = med3(x, a - 2L * b, a - b, a);
            }

            m = med3(x, l, m, a);
         }

         double v = BigArrays.get(x, m);
         a = from;
         b = from;
         long c = to - 1L;
         long d = c;

         while(true) {
            int comparison;
            while(b > c || (comparison = Double.compare(BigArrays.get(x, b), v)) > 0) {
               for(; c >= b && (comparison = Double.compare(BigArrays.get(x, c), v)) >= 0; --c) {
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

   public static void quickSort(double[][] x) {
      quickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(double[][] x, long from, long to) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new DoubleBigArrays.ForkJoinQuickSort(x, from, to));
      } else {
         quickSort(x, from, to);
      }

   }

   public static void parallelQuickSort(double[][] x) {
      parallelQuickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(double[][] x, long from, long to, DoubleComparator comp) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new DoubleBigArrays.ForkJoinQuickSortComp(x, from, to, comp));
      } else {
         quickSort(x, from, to, comp);
      }

   }

   public static void parallelQuickSort(double[][] x, DoubleComparator comp) {
      parallelQuickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static long binarySearch(double[][] a, long from, long to, double key) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         double midVal = BigArrays.get(a, mid);
         if (midVal < key) {
            from = mid + 1L;
         } else {
            if (!(midVal > key)) {
               return mid;
            }

            to = mid - 1L;
         }
      }

      return -(from + 1L);
   }

   public static long binarySearch(double[][] a, double key) {
      return binarySearch(a, 0L, BigArrays.length(a), key);
   }

   public static long binarySearch(double[][] a, long from, long to, double key, DoubleComparator c) {
      --to;

      while(from <= to) {
         long mid = from + to >>> 1;
         double midVal = BigArrays.get(a, mid);
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

   public static long binarySearch(double[][] a, double key, DoubleComparator c) {
      return binarySearch(a, 0L, BigArrays.length(a), key, c);
   }

   private static final long fixDouble(double d) {
      long l = Double.doubleToRawLongBits(d);
      return l >= 0L ? l : l ^ Long.MAX_VALUE;
   }

   public static void radixSort(double[][] a) {
      radixSort(a, 0L, BigArrays.length(a));
   }

   public static void radixSort(double[][] a, long from, long to) {
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
                  BigArrays.set(digit, i, (byte)((int)(fixDouble(BigArrays.get(a, first + i)) >>> shift & 255L ^ (long)signMask)));
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
                  double t = BigArrays.get(a, i + first);
                  c = BigArrays.get(digit, i) & 255;

                  long d;
                  while((d = --pos[c]) > i) {
                     double z = t;
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

   private static void selectionSort(double[][] a, double[][] b, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (Double.compare(BigArrays.get(a, j), BigArrays.get(a, m)) < 0 || Double.compare(BigArrays.get(a, j), BigArrays.get(a, m)) == 0 && Double.compare(BigArrays.get(b, j), BigArrays.get(b, m)) < 0) {
               m = j;
            }
         }

         if (m != i) {
            double t = BigArrays.get(a, i);
            BigArrays.set(a, i, BigArrays.get(a, m));
            BigArrays.set(a, m, t);
            t = BigArrays.get(b, i);
            BigArrays.set(b, i, BigArrays.get(b, m));
            BigArrays.set(b, m, t);
         }
      }

   }

   public static void radixSort(double[][] a, double[][] b) {
      radixSort(a, b, 0L, BigArrays.length(a));
   }

   public static void radixSort(double[][] a, double[][] b, long from, long to) {
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
                  double[][] k = level < 8 ? a : b;
                  int shift = (7 - level % 8) * 8;
                  long i = length;

                  while(i-- != 0L) {
                     BigArrays.set(digit, i, (byte)((int)(fixDouble(BigArrays.get(k, first + i)) >>> shift & 255L ^ (long)signMask)));
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
                     double t = BigArrays.get(a, i + first);
                     double u = BigArrays.get(b, i + first);
                     c = BigArrays.get(digit, i) & 255;

                     long d;
                     while((d = --pos[c]) > i) {
                        double z = t;
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

   private static void insertionSortIndirect(long[][] perm, double[][] a, double[][] b, long from, long to) {
      long t;
      long j;
      for(long i = from; ++i < to; BigArrays.set(perm, j, t)) {
         t = BigArrays.get(perm, i);
         j = i;

         for(long u = BigArrays.get(perm, i - 1L); Double.compare(BigArrays.get(a, t), BigArrays.get(a, u)) < 0 || Double.compare(BigArrays.get(a, t), BigArrays.get(a, u)) == 0 && Double.compare(BigArrays.get(b, t), BigArrays.get(b, u)) < 0; u = BigArrays.get(perm, --j - 1L)) {
            BigArrays.set(perm, j, u);
            if (from == j - 1L) {
               --j;
               break;
            }
         }
      }

   }

   public static void radixSortIndirect(long[][] perm, double[][] a, double[][] b, boolean stable) {
      ensureSameLength(a, b);
      radixSortIndirect(perm, a, b, 0L, BigArrays.length(a), stable);
   }

   public static void radixSortIndirect(long[][] perm, double[][] a, double[][] b, long from, long to, boolean stable) {
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
         long[][] support = stable ? LongBigArrays.newBigArray(BigArrays.length(perm)) : null;

         while(true) {
            while(stackPos > 0) {
               --stackPos;
               long first = offsetStack[stackPos];
               long length = lengthStack[stackPos];
               int level = levelStack[stackPos];
               int signMask = level % 8 == 0 ? 128 : 0;
               double[][] k = level < 8 ? a : b;
               int shift = (7 - level % 8) * 8;

               for(long i = first + length; i-- != first; ++count[(int)(fixDouble(BigArrays.get(k, BigArrays.get(perm, i))) >>> shift & 255L ^ (long)signMask)]) {
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
                     BigArrays.set(support, --pos[(int)(fixDouble(BigArrays.get(k, BigArrays.get(perm, end))) >>> shift & 255L ^ (long)signMask)], BigArrays.get(perm, end));
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
                     c = (int)(fixDouble(BigArrays.get(k, t)) >>> shift & 255L ^ (long)signMask);
                     if (i < end) {
                        long d;
                        while((d = --pos[c]) > i) {
                           long z = t;
                           t = BigArrays.get(perm, d);
                           BigArrays.set(perm, d, z);
                           c = (int)(fixDouble(BigArrays.get(k, t)) >>> shift & 255L ^ (long)signMask);
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

   public static double[][] shuffle(double[][] a, long from, long to, Random random) {
      return BigArrays.shuffle(a, from, to, random);
   }

   public static double[][] shuffle(double[][] a, Random random) {
      return BigArrays.shuffle(a, random);
   }

   protected static class ForkJoinQuickSort extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final long from;
      private final long to;
      private final double[][] x;

      public ForkJoinQuickSort(double[][] x, long from, long to) {
         this.from = from;
         this.to = to;
         this.x = x;
      }

      protected void compute() {
         double[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            DoubleBigArrays.quickSort(x, this.from, this.to);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = DoubleBigArrays.med3(x, l, l + s, l + 2L * s);
            m = DoubleBigArrays.med3(x, m - s, m, m + s);
            n = DoubleBigArrays.med3(x, n - 2L * s, n - s, n);
            m = DoubleBigArrays.med3(x, l, m, n);
            double v = BigArrays.get(x, m);
            long a = this.from;
            long b = a;
            long c = this.to - 1L;
            long d = c;

            while(true) {
               int comparison;
               while(b > c || (comparison = Double.compare(BigArrays.get(x, b), v)) > 0) {
                  for(; c >= b && (comparison = Double.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                     if (comparison == 0) {
                        BigArrays.swap(x, c, d--);
                     }
                  }

                  if (b > c) {
                     s = Math.min(a - this.from, b - a);
                     DoubleBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     DoubleBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new DoubleBigArrays.ForkJoinQuickSort(x, this.from, this.from + s), new DoubleBigArrays.ForkJoinQuickSort(x, this.to - t, this.to));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new DoubleBigArrays.ForkJoinQuickSort(x, this.from, this.from + s)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new DoubleBigArrays.ForkJoinQuickSort(x, this.to - t, this.to)});
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
      private final double[][] x;
      private final DoubleComparator comp;

      public ForkJoinQuickSortComp(double[][] x, long from, long to, DoubleComparator comp) {
         this.from = from;
         this.to = to;
         this.x = x;
         this.comp = comp;
      }

      protected void compute() {
         double[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            DoubleBigArrays.quickSort(x, this.from, this.to, this.comp);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = DoubleBigArrays.med3(x, l, l + s, l + 2L * s, this.comp);
            m = DoubleBigArrays.med3(x, m - s, m, m + s, this.comp);
            n = DoubleBigArrays.med3(x, n - 2L * s, n - s, n, this.comp);
            m = DoubleBigArrays.med3(x, l, m, n, this.comp);
            double v = BigArrays.get(x, m);
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
                     DoubleBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     DoubleBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new DoubleBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new DoubleBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new DoubleBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new DoubleBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
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

   private static final class BigArrayHashStrategy implements Serializable, Hash.Strategy<double[][]> {
      private static final long serialVersionUID = -7046029254386353129L;

      private BigArrayHashStrategy() {
      }

      public int hashCode(double[][] o) {
         return Arrays.deepHashCode(o);
      }

      public boolean equals(double[][] a, double[][] b) {
         return DoubleBigArrays.equals(a, b);
      }

      // $FF: synthetic method
      BigArrayHashStrategy(Object x0) {
         this();
      }
   }
}
