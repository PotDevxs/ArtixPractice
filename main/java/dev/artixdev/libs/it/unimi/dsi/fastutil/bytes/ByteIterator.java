package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface ByteIterator extends PrimitiveIterator<Byte, ByteConsumer> {
   byte nextByte();

   /** @deprecated */
   @Deprecated
   default Byte next() {
      return this.nextByte();
   }

   default void forEachRemaining(ByteConsumer action) {
      Objects.requireNonNull(action);

      while(this.hasNext()) {
         action.accept(this.nextByte());
      }

   }

   default void forEachRemaining(IntConsumer action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((byte x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Byte> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((byte x) -> action.accept(x));
   }

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextByte();
         }

         return n - i - 1;
      }
   }
}
