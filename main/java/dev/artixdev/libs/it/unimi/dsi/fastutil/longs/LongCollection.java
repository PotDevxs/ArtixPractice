package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Size64;

public interface LongCollection extends Collection<Long>, LongIterable {
   LongIterator iterator();

   default LongIterator longIterator() {
      return this.iterator();
   }

   default LongSpliterator spliterator() {
      return LongSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 320);
   }

   default LongSpliterator longSpliterator() {
      return this.spliterator();
   }

   boolean add(long var1);

   boolean contains(long var1);

   boolean rem(long var1);

   /** @deprecated */
   @Deprecated
   default boolean add(Long key) {
      return this.add(key);
   }

   /** @deprecated */
   @Deprecated
   default boolean contains(Object key) {
      return key == null ? false : this.contains((Long)key);
   }

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key) {
      return key == null ? false : this.rem((Long)key);
   }

   long[] toLongArray();

   /** @deprecated */
   @Deprecated
   default long[] toLongArray(long[] a) {
      return this.toArray(a);
   }

   long[] toArray(long[] var1);

   boolean addAll(LongCollection var1);

   boolean containsAll(LongCollection var1);

   boolean removeAll(LongCollection var1);

   /** @deprecated */
   @Deprecated
   default boolean removeIf(Predicate<? super Long> filter) {
      return this.removeIf(filter instanceof java.util.function.LongPredicate ? (java.util.function.LongPredicate)filter : (key) -> {
         return filter.test(key);
      });
   }

   default boolean removeIf(java.util.function.LongPredicate filter) {
      Objects.requireNonNull(filter);
      boolean removed = false;
      LongIterator each = this.iterator();

      while(each.hasNext()) {
         if (filter.test(each.nextLong())) {
            each.remove();
            removed = true;
         }
      }

      return removed;
   }

   default boolean removeIf(LongPredicate filter) {
      return this.removeIf((java.util.function.LongPredicate)filter);
   }

   boolean retainAll(LongCollection var1);

   /** @deprecated */
   @Deprecated
   default Stream<Long> stream() {
      return super.stream();
   }

   default LongStream longStream() {
      return StreamSupport.longStream(this.longSpliterator(), false);
   }

   /** @deprecated */
   @Deprecated
   default Stream<Long> parallelStream() {
      return super.parallelStream();
   }

   default LongStream longParallelStream() {
      return StreamSupport.longStream(this.longSpliterator(), true);
   }
}
