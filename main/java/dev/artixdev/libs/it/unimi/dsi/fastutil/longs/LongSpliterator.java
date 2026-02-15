package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.Spliterator.OfLong;
import java.util.function.Consumer;

public interface LongSpliterator extends OfLong {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Long> action) {
      java.util.function.LongConsumer var10001;
      if (action instanceof java.util.function.LongConsumer) {
         var10001 = (java.util.function.LongConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      return this.tryAdvance((java.util.function.LongConsumer)var10001);
   }

   default boolean tryAdvance(LongConsumer action) {
      return this.tryAdvance((java.util.function.LongConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Long> action) {
      java.util.function.LongConsumer var10001;
      if (action instanceof java.util.function.LongConsumer) {
         var10001 = (java.util.function.LongConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining((java.util.function.LongConsumer)var10001);
   }

   default void forEachRemaining(LongConsumer action) {
      this.forEachRemaining((java.util.function.LongConsumer)action);
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

   LongSpliterator trySplit();

   default LongComparator getComparator() {
      throw new IllegalStateException();
   }
}
