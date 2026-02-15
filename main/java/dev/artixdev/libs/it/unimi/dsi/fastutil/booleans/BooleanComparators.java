package dev.artixdev.libs.it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.Comparator;

public final class BooleanComparators {
   public static final BooleanComparator NATURAL_COMPARATOR = new BooleanComparators.NaturalImplicitComparator();
   public static final BooleanComparator OPPOSITE_COMPARATOR = new BooleanComparators.OppositeImplicitComparator();

   private BooleanComparators() {
   }

   public static BooleanComparator oppositeComparator(BooleanComparator c) {
      return (BooleanComparator)(c instanceof BooleanComparators.OppositeComparator ? ((BooleanComparators.OppositeComparator)c).comparator : new BooleanComparators.OppositeComparator(c));
   }

   public static BooleanComparator asBooleanComparator(final Comparator<? super Boolean> c) {
      return c != null && !(c instanceof BooleanComparator) ? new BooleanComparator() {
         public int compare(boolean x, boolean y) {
            return c.compare(x, y);
         }

         public int compare(Boolean x, Boolean y) {
            return c.compare(x, y);
         }
      } : (BooleanComparator)c;
   }

   protected static class OppositeComparator implements Serializable, BooleanComparator {
      private static final long serialVersionUID = 1L;
      final BooleanComparator comparator;

      protected OppositeComparator(BooleanComparator c) {
         this.comparator = c;
      }

      public final int compare(boolean a, boolean b) {
         return this.comparator.compare(b, a);
      }

      public final BooleanComparator reversed() {
         return this.comparator;
      }
   }

   protected static class NaturalImplicitComparator implements Serializable, BooleanComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(boolean a, boolean b) {
         return Boolean.compare(a, b);
      }

      public BooleanComparator reversed() {
         return BooleanComparators.OPPOSITE_COMPARATOR;
      }

      private Object readResolve() {
         return BooleanComparators.NATURAL_COMPARATOR;
      }
   }

   protected static class OppositeImplicitComparator implements Serializable, BooleanComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(boolean a, boolean b) {
         return -Boolean.compare(a, b);
      }

      public BooleanComparator reversed() {
         return BooleanComparators.NATURAL_COMPARATOR;
      }

      private Object readResolve() {
         return BooleanComparators.OPPOSITE_COMPARATOR;
      }
   }
}
