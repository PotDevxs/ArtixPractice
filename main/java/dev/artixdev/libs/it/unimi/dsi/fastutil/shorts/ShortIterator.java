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
      ShortConsumer var10001;
      if (action instanceof ShortConsumer) {
         var10001 = (ShortConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
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
