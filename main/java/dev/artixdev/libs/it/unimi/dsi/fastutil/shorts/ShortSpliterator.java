package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

public interface ShortSpliterator extends OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Short> action) {
      Objects.requireNonNull(action);
      return this.tryAdvance((short x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Short> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((short x) -> action.accept(x));
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

   ShortSpliterator trySplit();

   default ShortComparator getComparator() {
      throw new IllegalStateException();
   }
}
