package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.util.Comparator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Pair;

public interface ObjectLongPair<K> extends Pair<K, Long> {
   long rightLong();

   /** @deprecated */
   @Deprecated
   default Long right() {
      return this.rightLong();
   }

   default ObjectLongPair<K> right(long r) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default ObjectLongPair<K> right(Long l) {
      return this.right(l);
   }

   default long secondLong() {
      return this.rightLong();
   }

   /** @deprecated */
   @Deprecated
   default Long second() {
      return this.secondLong();
   }

   default ObjectLongPair<K> second(long r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default ObjectLongPair<K> second(Long l) {
      return this.second(l);
   }

   default long valueLong() {
      return this.rightLong();
   }

   /** @deprecated */
   @Deprecated
   default Long value() {
      return this.valueLong();
   }

   default ObjectLongPair<K> value(long r) {
      return this.right(r);
   }

   /** @deprecated */
   @Deprecated
   default ObjectLongPair<K> value(Long l) {
      return this.value(l);
   }

   static <K> ObjectLongPair<K> of(K left, long right) {
      return new ObjectLongImmutablePair(left, right);
   }

   static <K> Comparator<ObjectLongPair<K>> lexComparator() {
      return (x, y) -> {
         int t = ((Comparable)x.left()).compareTo(y.left());
         return t != 0 ? t : Long.compare(x.rightLong(), y.rightLong());
      };
   }
}
