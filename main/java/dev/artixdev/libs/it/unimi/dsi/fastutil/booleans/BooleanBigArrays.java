package dev.artixdev.libs.it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.BigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Hash;

public final class BooleanBigArrays {
   public static final boolean[][] EMPTY_BIG_ARRAY = new boolean[0][];
   public static final boolean[][] DEFAULT_EMPTY_BIG_ARRAY = new boolean[0][];
   public static final Hash.Strategy HASH_STRATEGY = new BooleanBigArrays.BigArrayHashStrategy();
   private static final int QUICKSORT_NO_REC = 7;
   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
   private static final int MEDIUM = 40;

   private BooleanBigArrays() {
   }

   /** @deprecated */
   @Deprecated
   public static boolean get(boolean[][] array, long index) {
      return array[BigArrays.segment(index)][BigArrays.displacement(index)];
   }

   /** @deprecated */
   @Deprecated
   public static void set(boolean[][] array, long index, boolean value) {
      array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
   }

   /** @deprecated */
   @Deprecated
   public static void swap(boolean[][] array, long first, long second) {
      boolean t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
      array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
      array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
   }

   /** @deprecated */
   @Deprecated
   public static long length(boolean[][] array) {
      int length = array.length;
      return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(boolean[][] srcArray, long srcPos, boolean[][] destArray, long destPos, long length) {
      BigArrays.copy(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyFromBig(boolean[][] srcArray, long srcPos, boolean[] destArray, int destPos, int length) {
      BigArrays.copyFromBig(srcArray, srcPos, destArray, destPos, length);
   }

   /** @deprecated */
   @Deprecated
   public static void copyToBig(boolean[] srcArray, int srcPos, boolean[][] destArray, long destPos, long length) {
      BigArrays.copyToBig(srcArray, srcPos, destArray, destPos, length);
   }

   public static boolean[][] newBigArray(long length) {
      if (length == 0L) {
         return EMPTY_BIG_ARRAY;
      } else {
         BigArrays.ensureLength(length);
         int baseLength = (int)(length + 134217727L >>> 27);
         boolean[][] base = new boolean[baseLength][];
         int residual = (int)(length & 134217727L);
         int i;
         if (residual != 0) {
            for(i = 0; i < baseLength - 1; ++i) {
               base[i] = new boolean[134217728];
            }

            base[baseLength - 1] = new boolean[residual];
         } else {
            for(i = 0; i < baseLength; ++i) {
               base[i] = new boolean[134217728];
            }
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] wrap(boolean[] array) {
      return BigArrays.wrap(array);
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] ensureCapacity(boolean[][] array, long length) {
      return ensureCapacity(array, length, length(array));
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] forceCapacity(boolean[][] array, long length, long preserve) {
      return BigArrays.forceCapacity(array, length, preserve);
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] ensureCapacity(boolean[][] array, long length, long preserve) {
      return length > length(array) ? forceCapacity(array, length, preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] grow(boolean[][] array, long length) {
      long oldLength = length(array);
      return length > oldLength ? grow(array, length, oldLength) : array;
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] grow(boolean[][] array, long length, long preserve) {
      long oldLength = length(array);
      return length > oldLength ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] trim(boolean[][] array, long length) {
      BigArrays.ensureLength(length);
      long oldLength = length(array);
      if (length >= oldLength) {
         return array;
      } else {
         int baseLength = (int)(length + 134217727L >>> 27);
         boolean[][] base = (boolean[][])Arrays.copyOf(array, baseLength);
         int residual = (int)(length & 134217727L);
         if (residual != 0) {
            base[baseLength - 1] = BooleanArrays.trim(base[baseLength - 1], residual);
         }

         return base;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] setLength(boolean[][] array, long length) {
      return BigArrays.setLength(array, length);
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] copy(boolean[][] array, long offset, long length) {
      return BigArrays.copy(array, offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static boolean[][] copy(boolean[][] array) {
      return BigArrays.copy(array);
   }

   /** @deprecated */
   @Deprecated
   public static void fill(boolean[][] array, boolean value) {
      int i = array.length;

      while(i-- != 0) {
         Arrays.fill(array[i], value);
      }

   }

   /** @deprecated */
   @Deprecated
   public static void fill(boolean[][] array, long from, long to, boolean value) {
      BigArrays.fill(array, from, to, value);
   }

   /** @deprecated */
   @Deprecated
   public static boolean equals(boolean[][] a1, boolean[][] a2) {
      return BigArrays.equals(a1, a2);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(boolean[][] a) {
      return BigArrays.toString(a);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureFromTo(boolean[][] a, long from, long to) {
      BigArrays.ensureFromTo(length(a), from, to);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureOffsetLength(boolean[][] a, long offset, long length) {
      BigArrays.ensureOffsetLength(length(a), offset, length);
   }

   /** @deprecated */
   @Deprecated
   public static void ensureSameLength(boolean[][] a, boolean[][] b) {
      if (length(a) != length(b)) {
         throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
      }
   }

   private static ForkJoinPool getPool() {
      ForkJoinPool current = ForkJoinTask.getPool();
      return current == null ? ForkJoinPool.commonPool() : current;
   }

   private static void swap(boolean[][] x, long a, long b, long n) {
      for(int i = 0; (long)i < n; ++b) {
         BigArrays.swap(x, a, b);
         ++i;
         ++a;
      }

   }

   private static long med3(boolean[][] x, long a, long b, long c, BooleanComparator comp) {
      int ab = comp.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = comp.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = comp.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(boolean[][] a, long from, long to, BooleanComparator comp) {
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

   public static void quickSort(boolean[][] x, long from, long to, BooleanComparator comp) {
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

         boolean v = BigArrays.get(x, m);
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

   private static long med3(boolean[][] x, long a, long b, long c) {
      int ab = Boolean.compare(BigArrays.get(x, a), BigArrays.get(x, b));
      int ac = Boolean.compare(BigArrays.get(x, a), BigArrays.get(x, c));
      int bc = Boolean.compare(BigArrays.get(x, b), BigArrays.get(x, c));
      return ab < 0 ? (bc < 0 ? b : (ac < 0 ? c : a)) : (bc > 0 ? b : (ac > 0 ? c : a));
   }

   private static void selectionSort(boolean[][] a, long from, long to) {
      for(long i = from; i < to - 1L; ++i) {
         long m = i;

         for(long j = i + 1L; j < to; ++j) {
            if (!BigArrays.get(a, j) && BigArrays.get(a, m)) {
               m = j;
            }
         }

         if (m != i) {
            BigArrays.swap(a, i, m);
         }
      }

   }

   public static void quickSort(boolean[][] x, BooleanComparator comp) {
      quickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static void quickSort(boolean[][] x, long from, long to) {
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

         boolean v = BigArrays.get(x, m);
         long a = from;
         long b = from;
         long c = to - 1L;
         long d = c;

         while(true) {
            int comparison;
            while(b > c || (comparison = Boolean.compare(BigArrays.get(x, b), v)) > 0) {
               for(; c >= b && (comparison = Boolean.compare(BigArrays.get(x, c), v)) >= 0; --c) {
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

   public static void quickSort(boolean[][] x) {
      quickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(boolean[][] x, long from, long to) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new BooleanBigArrays.ForkJoinQuickSort(x, from, to));
      } else {
         quickSort(x, from, to);
      }

   }

   public static void parallelQuickSort(boolean[][] x) {
      parallelQuickSort(x, 0L, BigArrays.length(x));
   }

   public static void parallelQuickSort(boolean[][] x, long from, long to, BooleanComparator comp) {
      ForkJoinPool pool = getPool();
      if (to - from >= 8192L && pool.getParallelism() != 1) {
         pool.invoke(new BooleanBigArrays.ForkJoinQuickSortComp(x, from, to, comp));
      } else {
         quickSort(x, from, to, comp);
      }

   }

   public static void parallelQuickSort(boolean[][] x, BooleanComparator comp) {
      parallelQuickSort(x, 0L, BigArrays.length(x), comp);
   }

   public static boolean[][] shuffle(boolean[][] a, long from, long to, Random random) {
      return BigArrays.shuffle(a, from, to, random);
   }

   public static boolean[][] shuffle(boolean[][] a, Random random) {
      return BigArrays.shuffle(a, random);
   }

   protected static class ForkJoinQuickSort extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final long from;
      private final long to;
      private final boolean[][] x;

      public ForkJoinQuickSort(boolean[][] x, long from, long to) {
         this.from = from;
         this.to = to;
         this.x = x;
      }

      protected void compute() {
         boolean[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            BooleanBigArrays.quickSort(x, this.from, this.to);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = BooleanBigArrays.med3(x, l, l + s, l + 2L * s);
            m = BooleanBigArrays.med3(x, m - s, m, m + s);
            n = BooleanBigArrays.med3(x, n - 2L * s, n - s, n);
            m = BooleanBigArrays.med3(x, l, m, n);
            boolean v = BigArrays.get(x, m);
            long a = this.from;
            long b = a;
            long c = this.to - 1L;
            long d = c;

            while(true) {
               int comparison;
               while(b > c || (comparison = Boolean.compare(BigArrays.get(x, b), v)) > 0) {
                  for(; c >= b && (comparison = Boolean.compare(BigArrays.get(x, c), v)) >= 0; --c) {
                     if (comparison == 0) {
                        BigArrays.swap(x, c, d--);
                     }
                  }

                  if (b > c) {
                     s = Math.min(a - this.from, b - a);
                     BooleanBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     BooleanBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new BooleanBigArrays.ForkJoinQuickSort(x, this.from, this.from + s), new BooleanBigArrays.ForkJoinQuickSort(x, this.to - t, this.to));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new BooleanBigArrays.ForkJoinQuickSort(x, this.from, this.from + s)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new BooleanBigArrays.ForkJoinQuickSort(x, this.to - t, this.to)});
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
      private final boolean[][] x;
      private final BooleanComparator comp;

      public ForkJoinQuickSortComp(boolean[][] x, long from, long to, BooleanComparator comp) {
         this.from = from;
         this.to = to;
         this.x = x;
         this.comp = comp;
      }

      protected void compute() {
         boolean[][] x = this.x;
         long len = this.to - this.from;
         if (len < 8192L) {
            BooleanBigArrays.quickSort(x, this.from, this.to, this.comp);
         } else {
            long m = this.from + len / 2L;
            long l = this.from;
            long n = this.to - 1L;
            long s = len / 8L;
            l = BooleanBigArrays.med3(x, l, l + s, l + 2L * s, this.comp);
            m = BooleanBigArrays.med3(x, m - s, m, m + s, this.comp);
            n = BooleanBigArrays.med3(x, n - 2L * s, n - s, n, this.comp);
            m = BooleanBigArrays.med3(x, l, m, n, this.comp);
            boolean v = BigArrays.get(x, m);
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
                     BooleanBigArrays.swap(x, this.from, b - s, s);
                     s = Math.min(d - c, this.to - d - 1L);
                     BooleanBigArrays.swap(x, b, this.to - s, s);
                     s = b - a;
                     long t = d - c;
                     if (s > 1L && t > 1L) {
                        invokeAll(new BooleanBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new BooleanBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
                     } else if (s > 1L) {
                        invokeAll(new ForkJoinTask[]{new BooleanBigArrays.ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp)});
                     } else {
                        invokeAll(new ForkJoinTask[]{new BooleanBigArrays.ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp)});
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

   private static final class BigArrayHashStrategy implements Serializable, Hash.Strategy<boolean[][]> {
      private static final long serialVersionUID = -7046029254386353129L;

      private BigArrayHashStrategy() {
      }

      public int hashCode(boolean[][] o) {
         return Arrays.deepHashCode(o);
      }

      public boolean equals(boolean[][] a, boolean[][] b) {
         return BooleanBigArrays.equals(a, b);
      }

      // $FF: synthetic method
      BigArrayHashStrategy(Object x0) {
         this();
      }
   }
}
