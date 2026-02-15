package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface LongUnaryOperator extends java.util.function.LongUnaryOperator, UnaryOperator<Long> {
   long apply(long var1);

   static LongUnaryOperator identity() {
      return (i) -> {
         return i;
      };
   }

   static LongUnaryOperator negation() {
      return (i) -> {
         return -i;
      };
   }

   /** @deprecated */
   @Deprecated
   default long applyAsLong(long x) {
      return this.apply(x);
   }

   /** @deprecated */
   @Deprecated
   default Long apply(Long x) {
      return this.apply(x);
   }
}
