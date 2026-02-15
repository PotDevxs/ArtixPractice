package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.Comparator;

public final class ByteComparators {
   public static final ByteComparator NATURAL_COMPARATOR = new ByteComparators.NaturalImplicitComparator();
   public static final ByteComparator OPPOSITE_COMPARATOR = new ByteComparators.OppositeImplicitComparator();

   private ByteComparators() {
   }

   public static ByteComparator oppositeComparator(ByteComparator c) {
      return (ByteComparator)(c instanceof ByteComparators.OppositeComparator ? ((ByteComparators.OppositeComparator)c).comparator : new ByteComparators.OppositeComparator(c));
   }

   public static ByteComparator asByteComparator(final Comparator<? super Byte> c) {
      return c != null && !(c instanceof ByteComparator) ? new ByteComparator() {
         public int compare(byte x, byte y) {
            return c.compare(x, y);
         }

         public int compare(Byte x, Byte y) {
            return c.compare(x, y);
         }
      } : (ByteComparator)c;
   }

   protected static class OppositeComparator implements Serializable, ByteComparator {
      private static final long serialVersionUID = 1L;
      final ByteComparator comparator;

      protected OppositeComparator(ByteComparator c) {
         this.comparator = c;
      }

      public final int compare(byte a, byte b) {
         return this.comparator.compare(b, a);
      }

      public final ByteComparator reversed() {
         return this.comparator;
      }
   }

   protected static class NaturalImplicitComparator implements Serializable, ByteComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(byte a, byte b) {
         return Byte.compare(a, b);
      }

      public ByteComparator reversed() {
         return ByteComparators.OPPOSITE_COMPARATOR;
      }

      private Object readResolve() {
         return ByteComparators.NATURAL_COMPARATOR;
      }
   }

   protected static class OppositeImplicitComparator implements Serializable, ByteComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(byte a, byte b) {
         return -Byte.compare(a, b);
      }

      public ByteComparator reversed() {
         return ByteComparators.NATURAL_COMPARATOR;
      }

      private Object readResolve() {
         return ByteComparators.OPPOSITE_COMPARATOR;
      }
   }
}
