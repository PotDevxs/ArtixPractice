package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

public interface ShortSpliterator extends OfPrimitive<Short, ShortConsumer, ShortSpliterator> {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Short> action) {
      ShortConsumer var10001;
      if (action instanceof ShortConsumer) {
         var10001 = (ShortConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      return this.tryAdvance(var10001);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Short> action) {
      ShortConsumer var10001;
      if (action instanceof ShortConsumer) {
         var10001 = (ShortConsumer)action;
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

   ShortSpliterator trySplit();

   default ShortComparator getComparator() {
      throw new IllegalStateException();
   }
}
