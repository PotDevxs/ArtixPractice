package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.PrimitiveIterator.OfInt;
import java.util.function.Consumer;

public interface IntIterator extends OfInt {
   int nextInt();

   /** @deprecated */
   @Deprecated
   default Integer next() {
      return this.nextInt();
   }

   default void forEachRemaining(IntConsumer action) {
      this.forEachRemaining((java.util.function.IntConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Integer> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((int x) -> action.accept(x));
   }

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextInt();
         }

         return n - i - 1;
      }
   }
}
