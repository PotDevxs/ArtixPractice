package dev.artixdev.libs.it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.util.Comparator;

public final class DoubleComparators {
   public static final DoubleComparator NATURAL_COMPARATOR = new DoubleComparators.NaturalImplicitComparator();
   public static final DoubleComparator OPPOSITE_COMPARATOR = new DoubleComparators.OppositeImplicitComparator();

   private DoubleComparators() {
   }

   public static DoubleComparator oppositeComparator(DoubleComparator c) {
      return (DoubleComparator)(c instanceof DoubleComparators.OppositeComparator ? ((DoubleComparators.OppositeComparator)c).comparator : new DoubleComparators.OppositeComparator(c));
   }

   public static DoubleComparator asDoubleComparator(final Comparator<? super Double> c) {
      return c != null && !(c instanceof DoubleComparator) ? new DoubleComparator() {
         public int compare(double x, double y) {
            return c.compare(x, y);
         }

         public int compare(Double x, Double y) {
            return c.compare(x, y);
         }
      } : (DoubleComparator)c;
   }

   protected static class OppositeComparator implements Serializable, DoubleComparator {
      private static final long serialVersionUID = 1L;
      final DoubleComparator comparator;

      protected OppositeComparator(DoubleComparator c) {
         this.comparator = c;
      }

      public final int compare(double a, double b) {
         return this.comparator.compare(b, a);
      }

      public final DoubleComparator reversed() {
         return this.comparator;
      }
   }

   protected static class NaturalImplicitComparator implements Serializable, DoubleComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(double a, double b) {
         return Double.compare(a, b);
      }

      public DoubleComparator reversed() {
         return DoubleComparators.OPPOSITE_COMPARATOR;
      }

      private Object readResolve() {
         return DoubleComparators.NATURAL_COMPARATOR;
      }
   }

   protected static class OppositeImplicitComparator implements Serializable, DoubleComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(double a, double b) {
         return -Double.compare(a, b);
      }

      public DoubleComparator reversed() {
         return DoubleComparators.NATURAL_COMPARATOR;
      }

      private Object readResolve() {
         return DoubleComparators.OPPOSITE_COMPARATOR;
      }
   }
}
