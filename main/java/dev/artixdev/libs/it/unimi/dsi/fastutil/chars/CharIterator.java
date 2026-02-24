package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {
   char nextChar();

   /** @deprecated */
   @Deprecated
   default Character next() {
      return this.nextChar();
   }

   default void forEachRemaining(CharConsumer action) {
      Objects.requireNonNull(action);

      while(this.hasNext()) {
         action.accept(this.nextChar());
      }

   }

   default void forEachRemaining(IntConsumer action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((char x) -> action.accept(x));
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Character> action) {
      Objects.requireNonNull(action);
      this.forEachRemaining((char x) -> action.accept(x));
   }

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextChar();
         }

         return n - i - 1;
      }
   }
}
