package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntSpliterator implements IntSpliterator {
   protected AbstractIntSpliterator() {
   }

   public final boolean tryAdvance(IntConsumer action) {
      return this.tryAdvance(action);
   }

   public final void forEachRemaining(IntConsumer action) {
      this.forEachRemaining(action);
   }
}
