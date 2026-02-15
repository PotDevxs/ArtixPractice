package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.Comparator;

public final class CharComparators {
   public static final CharComparator NATURAL_COMPARATOR = new CharComparators.NaturalImplicitComparator();
   public static final CharComparator OPPOSITE_COMPARATOR = new CharComparators.OppositeImplicitComparator();

   private CharComparators() {
   }

   public static CharComparator oppositeComparator(CharComparator c) {
      return (CharComparator)(c instanceof CharComparators.OppositeComparator ? ((CharComparators.OppositeComparator)c).comparator : new CharComparators.OppositeComparator(c));
   }

   public static CharComparator asCharComparator(final Comparator<? super Character> c) {
      return c != null && !(c instanceof CharComparator) ? new CharComparator() {
         public int compare(char x, char y) {
            return c.compare(x, y);
         }

         public int compare(Character x, Character y) {
            return c.compare(x, y);
         }
      } : (CharComparator)c;
   }

   protected static class OppositeComparator implements Serializable, CharComparator {
      private static final long serialVersionUID = 1L;
      final CharComparator comparator;

      protected OppositeComparator(CharComparator c) {
         this.comparator = c;
      }

      public final int compare(char a, char b) {
         return this.comparator.compare(b, a);
      }

      public final CharComparator reversed() {
         return this.comparator;
      }
   }

   protected static class NaturalImplicitComparator implements Serializable, CharComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(char a, char b) {
         return Character.compare(a, b);
      }

      public CharComparator reversed() {
         return CharComparators.OPPOSITE_COMPARATOR;
      }

      private Object readResolve() {
         return CharComparators.NATURAL_COMPARATOR;
      }
   }

   protected static class OppositeImplicitComparator implements Serializable, CharComparator {
      private static final long serialVersionUID = 1L;

      public final int compare(char a, char b) {
         return -Character.compare(a, b);
      }

      public CharComparator reversed() {
         return CharComparators.NATURAL_COMPARATOR;
      }

      private Object readResolve() {
         return CharComparators.OPPOSITE_COMPARATOR;
      }
   }
}
