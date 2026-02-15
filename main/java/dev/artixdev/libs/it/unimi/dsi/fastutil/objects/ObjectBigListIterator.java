package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import dev.artixdev.libs.it.unimi.dsi.fastutil.BigListIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

public interface ObjectBigListIterator<K> extends BigListIterator<K>, ObjectBidirectionalIterator<K> {
   default void set(K k) {
      throw new UnsupportedOperationException();
   }

   default void add(K k) {
      throw new UnsupportedOperationException();
   }

   default long skip(long n) {
      long i = n;

      while(i-- != 0L && this.hasNext()) {
         this.next();
      }

      return n - i - 1L;
   }

   default long back(long n) {
      long i = n;

      while(i-- != 0L && this.hasPrevious()) {
         this.previous();
      }

      return n - i - 1L;
   }

   default int skip(int n) {
      return SafeMath.safeLongToInt(this.skip((long)n));
   }
}
