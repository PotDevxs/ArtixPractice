package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

public interface CharSpliterator extends OfPrimitive<Character, CharConsumer, CharSpliterator> {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Character> action) {
      Objects.requireNonNull(action);
      return this.tryAdvance((char x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Character> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((char x) -> action.accept(x));
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

   CharSpliterator trySplit();

   default CharComparator getComparator() {
      throw new IllegalStateException();
   }
}
