package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.Consumer;

public interface LongIterator extends OfLong {
   long nextLong();

   /** @deprecated */
   @Deprecated
   default Long next() {
      return this.nextLong();
   }

   default void forEachRemaining(LongConsumer action) {
      this.forEachRemaining((java.util.function.LongConsumer)action);
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

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextLong();
         }

         return n - i - 1;
      }
   }
}
