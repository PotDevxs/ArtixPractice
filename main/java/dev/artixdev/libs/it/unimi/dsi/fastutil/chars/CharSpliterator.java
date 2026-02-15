package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

public interface CharSpliterator extends OfPrimitive<Character, CharConsumer, CharSpliterator> {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Character> action) {
      CharConsumer var10001;
      if (action instanceof CharConsumer) {
         var10001 = (CharConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      return this.tryAdvance(var10001);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Character> action) {
      CharConsumer var10001;
      if (action instanceof CharConsumer) {
         var10001 = (CharConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
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
