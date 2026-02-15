package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface LongBinaryOperator extends BinaryOperator<Long>, java.util.function.LongBinaryOperator {
   long apply(long var1, long var3);

   /** @deprecated */
   @Deprecated
   default long applyAsLong(long x, long y) {
      return this.apply(x, y);
   }

   /** @deprecated */
   @Deprecated
   default Long apply(Long x, Long y) {
      return this.apply(x, y);
   }
}
