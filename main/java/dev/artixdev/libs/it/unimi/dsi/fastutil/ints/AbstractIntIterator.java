package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

public abstract class AbstractIntIterator implements IntIterator {
   protected AbstractIntIterator() {
   }

   public void forEachRemaining(IntConsumer action) {
      IntIterator.super.forEachRemaining(action);
   }
}
