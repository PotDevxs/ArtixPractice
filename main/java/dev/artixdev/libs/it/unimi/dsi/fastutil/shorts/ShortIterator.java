package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface ShortIterator extends PrimitiveIterator<Short, ShortConsumer> {
   short nextShort();

   /** @deprecated */
   @Deprecated
   default Short next() {
      return this.nextShort();
   }

   default void forEachRemaining(ShortConsumer action) {
      Objects.requireNonNull(action);

      while(this.hasNext()) {
         action.accept(this.nextShort());
      }

   }

   default void forEachRemaining(IntConsumer action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((short x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Short> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((short x) -> action.accept(x));
   }

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextShort();
         }

         return n - i - 1;
      }
   }
}
