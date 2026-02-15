package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IntUnaryOperator extends java.util.function.IntUnaryOperator, UnaryOperator<Integer> {
   int apply(int var1);

   static IntUnaryOperator identity() {
      return (i) -> {
         return i;
      };
   }

   static IntUnaryOperator negation() {
      return (i) -> {
         return -i;
      };
   }

   /** @deprecated */
   @Deprecated
   default int applyAsInt(int x) {
      return this.apply(x);
   }

   /** @deprecated */
   @Deprecated
   default Integer apply(Integer x) {
      return this.apply(x);
   }
}
