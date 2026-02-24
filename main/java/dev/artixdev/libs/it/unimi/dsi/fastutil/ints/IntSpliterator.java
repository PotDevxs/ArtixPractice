package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.Spliterator.OfInt;
import java.util.function.Consumer;

public interface IntSpliterator extends OfInt {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Integer> action) {
      Objects.requireNonNull(action);
      return this.tryAdvance((int x) -> action.accept(x));
   }

   default boolean tryAdvance(IntConsumer action) {
      return this.tryAdvance((java.util.function.IntConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Integer> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((int x) -> action.accept(x));
   }

   default void forEachRemaining(IntConsumer action) {
      this.forEachRemaining((java.util.function.IntConsumer)action);
   }

   default long skip(long n) {
      if (n < 0L) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         long i = n;

         while(i-- != 0L && this.tryAdvance((unused) -> {
         })) {
         }

         return n - i - 1L;
      }
   }

   IntSpliterator trySplit();

   default IntComparator getComparator() {
      throw new IllegalStateException();
   }
}
