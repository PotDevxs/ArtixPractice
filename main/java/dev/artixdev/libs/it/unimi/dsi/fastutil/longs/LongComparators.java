package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Comparator;

public final class LongComparators {
   public static final LongComparator NATURAL_COMPARATOR = new LongComparators.NaturalImplicitComparator();
   public static final LongComparator OPPOSITE_COMPARATOR = new LongComparators.OppositeImplicitComparator();

   private LongComparators() {
   }

   public static LongComparator oppositeComparator(LongComparator c) {
      return (LongComparator)(c instanceof LongComparators.OppositeComparator ? ((LongComparators.OppositeComparator)c).comparator : new LongComparators.OppositeComparator(c));
   }

   public static LongComparator asLongComparator(final Comparator<? super Long> c) {
      return c != null && !(c instanceof LongComparator) ? new LongComparator() {
         public int compare(long x, long y) {
            return c.compare(x, y);
         }

         public int compare(Long x, Long y) {
            return c.compare(x, y);
         }
      } : (LongComparator)c;
   }

   protected static class OppositeComparator implements Serializable, LongComparator {
      private static final long serialVersionUID = 1L;
      final LongComparator comparator;

      protected OppositeComparator(LongComparator c) {
         this.comparator = c;
      }

      public final int compare(long a, long b) {
         return this.comparator.compare(b, a);
      }

      public final LongComparator reversed() {
         return this.comparator;
      }
   }

   protected static class NaturalImplicitComparator implements Serializable, LongComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(long a, long b) {
         return Long.compare(a, b);
      }

      public LongComparator reversed() {
         return LongComparators.OPPOSITE_COMPARATOR;
      }

      private Object readResolve() {
         return LongComparators.NATURAL_COMPARATOR;
      }
   }

   protected static class OppositeImplicitComparator implements Serializable, LongComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(long a, long b) {
         return -Long.compare(a, b);
      }

      public LongComparator reversed() {
         return LongComparators.NATURAL_COMPARATOR;
      }

      private Object readResolve() {
         return LongComparators.OPPOSITE_COMPARATOR;
      }
   }
}
