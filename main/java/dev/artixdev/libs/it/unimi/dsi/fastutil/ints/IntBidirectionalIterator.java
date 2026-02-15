package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface IntBidirectionalIterator extends IntIterator, ObjectBidirectionalIterator<Integer> {
   int previousInt();

   /** @deprecated */
   @Deprecated
   default Integer previous() {
      return this.previousInt();
   }

   default int back(int n) {
      int i = n;

      while(i-- != 0 && this.hasPrevious()) {
         this.previousInt();
      }

      return n - i - 1;
   }

   default int skip(int n) {
      return IntIterator.super.skip(n);
   }
}
