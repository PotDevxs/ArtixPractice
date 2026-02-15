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
      CharConsumer var10001;
      if (action instanceof CharConsumer) {
         var10001 = (CharConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
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
