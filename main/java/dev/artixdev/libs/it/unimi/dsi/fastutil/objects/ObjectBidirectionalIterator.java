package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import dev.artixdev.libs.it.unimi.dsi.fastutil.BidirectionalIterator;

public interface ObjectBidirectionalIterator<K> extends BidirectionalIterator<K>, ObjectIterator<K> {
   default int back(int n) {
      int i = n;

      while(i-- != 0 && this.hasPrevious()) {
         this.previous();
      }

      return n - i - 1;
   }

   default int skip(int n) {
      return ObjectIterator.super.skip(n);
   }
}
