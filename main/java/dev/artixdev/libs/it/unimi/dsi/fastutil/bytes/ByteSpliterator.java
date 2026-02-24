package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import java.util.Spliterator.OfPrimitive;
import java.util.function.Consumer;

public interface ByteSpliterator extends OfPrimitive<Byte, ByteConsumer, ByteSpliterator> {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Byte> action) {
      Objects.requireNonNull(action);
      return this.tryAdvance((byte x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Byte> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((byte x) -> action.accept(x));
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

   ByteSpliterator trySplit();

   default ByteComparator getComparator() {
      throw new IllegalStateException();
   }
}
