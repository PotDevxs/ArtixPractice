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
      ByteConsumer var10001;
      if (action instanceof ByteConsumer) {
         var10001 = (ByteConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Byte> action) {
      ByteConsumer var10001;
      if (action instanceof ByteConsumer) {
         var10001 = (ByteConsumer)action;
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
            this.nextByte();
         }

         return n - i - 1;
      }
   }
}
