package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntIterator implements IntIterator {
   protected AbstractIntIterator() {
   }

   public final void forEachRemaining(IntConsumer action) {
      this.forEachRemaining(action);
   }
}
