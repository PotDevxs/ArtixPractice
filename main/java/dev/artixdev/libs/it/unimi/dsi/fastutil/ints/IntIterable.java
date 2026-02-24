package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

public interface IntIterable extends Iterable<Integer> {
   IntIterator iterator();

   default IntIterator intIterator() {
      return this.iterator();
   }

   default IntSpliterator spliterator() {
      return IntSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
   }

   default IntSpliterator intSpliterator() {
      return this.spliterator();
   }

   default void forEach(java.util.function.IntConsumer action) {
      Objects.requireNonNull(action);
      this.iterator().forEachRemaining((java.util.function.IntConsumer)action);
   }

   default void forEach(IntConsumer action) {
      this.forEach((java.util.function.IntConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEach(Consumer<? super Integer> action) {
      Objects.requireNonNull(action);
      this.forEach((int x) -> action.accept(x));
   }
}
