package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrays;

public final class LongCollections {
   private LongCollections() {
   }

   public static LongCollection synchronize(LongCollection c) {
      return new LongCollections.SynchronizedCollection(c);
   }

   public static LongCollection synchronize(LongCollection c, Object sync) {
      return new LongCollections.SynchronizedCollection(c, sync);
   }

   public static LongCollection unmodifiable(LongCollection c) {
      return new LongCollections.UnmodifiableCollection(c);
   }

   public static LongCollection asCollection(LongIterable iterable) {
      return (LongCollection)(iterable instanceof LongCollection ? (LongCollection)iterable : new LongCollections.IterableCollection(iterable));
   }

   static class SynchronizedCollection implements Serializable, LongCollection {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongCollection collection;
      protected final Object sync;

      protected SynchronizedCollection(LongCollection c, Object sync) {
         this.collection = (LongCollection)Objects.requireNonNull(c);
         this.sync = sync;
      }

      protected SynchronizedCollection(LongCollection c) {
         this.collection = (LongCollection)Objects.requireNonNull(c);
         this.sync = this;
      }

      public boolean add(long k) {
         synchronized(this.sync) {
            return this.collection.add(k);
         }
      }

      public boolean contains(long k) {
         synchronized(this.sync) {
            return this.collection.contains(k);
         }
      }

      public boolean rem(long k) {
         synchronized(this.sync) {
            return this.collection.rem(k);
         }
      }

      public int size() {
         synchronized(this.sync) {
            return this.collection.size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.sync) {
            return this.collection.isEmpty();
         }
      }

      public long[] toLongArray() {
         synchronized(this.sync) {
            return this.collection.toLongArray();
         }
      }

      public Object[] toArray() {
         synchronized(this.sync) {
            return this.collection.toArray();
         }
      }

      /** @deprecated */
      @Deprecated
      public long[] toLongArray(long[] a) {
         return this.toArray(a);
      }

      public long[] toArray(long[] a) {
         synchronized(this.sync) {
            return this.collection.toArray(a);
         }
      }

      public boolean addAll(LongCollection c) {
         synchronized(this.sync) {
            return this.collection.addAll(c);
         }
      }

      public boolean containsAll(LongCollection c) {
         synchronized(this.sync) {
            return this.collection.containsAll(c);
         }
      }

      public boolean removeAll(LongCollection c) {
         synchronized(this.sync) {
            return this.collection.removeAll(c);
         }
      }

      public boolean retainAll(LongCollection c) {
         synchronized(this.sync) {
            return this.collection.retainAll(c);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Long k) {
         synchronized(this.sync) {
            return this.collection.add(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean contains(Object k) {
         synchronized(this.sync) {
            return this.collection.contains(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object k) {
         synchronized(this.sync) {
            return this.collection.remove(k);
         }
      }

      public LongIterator longIterator() {
         return this.collection.longIterator();
      }

      public LongSpliterator longSpliterator() {
         return this.collection.longSpliterator();
      }

      public LongStream longStream() {
         return this.collection.longStream();
      }

      public LongStream longParallelStream() {
         return this.collection.longParallelStream();
      }

      public <T> T[] toArray(T[] a) {
         synchronized(this.sync) {
            return this.collection.toArray(a);
         }
      }

      public LongIterator iterator() {
         return this.collection.iterator();
      }

      public LongSpliterator spliterator() {
         return this.collection.spliterator();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Long> stream() {
         return this.collection.stream();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Long> parallelStream() {
         return this.collection.parallelStream();
      }

      public void forEach(java.util.function.LongConsumer action) {
         synchronized(this.sync) {
            this.collection.forEach(action);
         }
      }

      public boolean addAll(Collection<? extends Long> c) {
         synchronized(this.sync) {
            return this.collection.addAll(c);
         }
      }

      public boolean containsAll(Collection<?> c) {
         synchronized(this.sync) {
            return this.collection.containsAll(c);
         }
      }

      public boolean removeAll(Collection<?> c) {
         synchronized(this.sync) {
            return this.collection.removeAll(c);
         }
      }

      public boolean retainAll(Collection<?> c) {
         synchronized(this.sync) {
            return this.collection.retainAll(c);
         }
      }

      public boolean removeIf(java.util.function.LongPredicate filter) {
         synchronized(this.sync) {
            return this.collection.removeIf(filter);
         }
      }

      public void clear() {
         synchronized(this.sync) {
            this.collection.clear();
         }
      }

      public String toString() {
         synchronized(this.sync) {
            return this.collection.toString();
         }
      }

      public int hashCode() {
         synchronized(this.sync) {
            return this.collection.hashCode();
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.sync) {
               return this.collection.equals(o);
            }
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }
   }

   static class UnmodifiableCollection implements Serializable, LongCollection {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongCollection collection;

      protected UnmodifiableCollection(LongCollection c) {
         this.collection = (LongCollection)Objects.requireNonNull(c);
      }

      public boolean add(long k) {
         throw new UnsupportedOperationException();
      }

      public boolean rem(long k) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.collection.size();
      }

      public boolean isEmpty() {
         return this.collection.isEmpty();
      }

      public boolean contains(long o) {
         return this.collection.contains(o);
      }

      public LongIterator iterator() {
         return LongIterators.unmodifiable(this.collection.iterator());
      }

      public LongSpliterator spliterator() {
         return this.collection.spliterator();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Long> stream() {
         return this.collection.stream();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Long> parallelStream() {
         return this.collection.parallelStream();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public <T> T[] toArray(T[] a) {
         return this.collection.toArray(a);
      }

      public Object[] toArray() {
         return this.collection.toArray();
      }

      public void forEach(java.util.function.LongConsumer action) {
         this.collection.forEach(action);
      }

      public boolean containsAll(Collection<?> c) {
         return this.collection.containsAll(c);
      }

      public boolean addAll(Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(java.util.function.LongPredicate filter) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean contains(Object k) {
         return this.collection.contains(k);
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public long[] toLongArray() {
         return this.collection.toLongArray();
      }

      /** @deprecated */
      @Deprecated
      public long[] toLongArray(long[] a) {
         return this.toArray(a);
      }

      public long[] toArray(long[] a) {
         return this.collection.toArray(a);
      }

      public boolean containsAll(LongCollection c) {
         return this.collection.containsAll(c);
      }

      public boolean addAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public LongIterator longIterator() {
         return this.collection.longIterator();
      }

      public LongSpliterator longSpliterator() {
         return this.collection.longSpliterator();
      }

      public LongStream longStream() {
         return this.collection.longStream();
      }

      public LongStream longParallelStream() {
         return this.collection.longParallelStream();
      }

      public String toString() {
         return this.collection.toString();
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }
   }

   public static class IterableCollection extends AbstractLongCollection implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongIterable iterable;

      protected IterableCollection(LongIterable iterable) {
         this.iterable = (LongIterable)Objects.requireNonNull(iterable);
      }

      public int size() {
         long size = this.iterable.spliterator().getExactSizeIfKnown();
         if (size >= 0L) {
            return (int)Math.min(2147483647L, size);
         } else {
            int c = 0;

            for(LongIterator iterator = this.iterator(); iterator.hasNext(); ++c) {
               iterator.nextLong();
            }

            return c;
         }
      }

      public boolean isEmpty() {
         return !this.iterable.iterator().hasNext();
      }

      public LongIterator iterator() {
         return this.iterable.iterator();
      }

      public LongSpliterator spliterator() {
         return this.iterable.spliterator();
      }

      public LongIterator longIterator() {
         return this.iterable.longIterator();
      }

      public LongSpliterator longSpliterator() {
         return this.iterable.longSpliterator();
      }
   }

   static class SizeDecreasingSupplier<C extends LongCollection> implements Supplier<C> {
      static final int RECOMMENDED_MIN_SIZE = 8;
      final AtomicInteger suppliedCount = new AtomicInteger(0);
      final int expectedFinalSize;
      final IntFunction<C> builder;

      SizeDecreasingSupplier(int expectedFinalSize, IntFunction<C> builder) {
         this.expectedFinalSize = expectedFinalSize;
         this.builder = builder;
      }

      public C get() {
         int expectedNeededNextSize = 1 + (this.expectedFinalSize - 1) / this.suppliedCount.incrementAndGet();
         if (expectedNeededNextSize < 0) {
            expectedNeededNextSize = 8;
         }

         return (LongCollection)this.builder.apply(expectedNeededNextSize);
      }
   }

   public abstract static class EmptyCollection extends AbstractLongCollection {
      protected EmptyCollection() {
      }

      public boolean contains(long k) {
         return false;
      }

      public Object[] toArray() {
         return ObjectArrays.EMPTY_ARRAY;
      }

      public <T> T[] toArray(T[] array) {
         if (array.length > 0) {
            array[0] = null;
         }

         return array;
      }

      public LongBidirectionalIterator iterator() {
         return LongIterators.EMPTY_ITERATOR;
      }

      public LongSpliterator spliterator() {
         return LongSpliterators.EMPTY_SPLITERATOR;
      }

      public int size() {
         return 0;
      }

      public void clear() {
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            return !(o instanceof Collection) ? false : ((Collection)o).isEmpty();
         }
      }

      /** @deprecated */
      @Deprecated
      public void forEach(Consumer<? super Long> action) {
      }

      public boolean containsAll(Collection<?> c) {
         return c.isEmpty();
      }

      public boolean addAll(Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean removeIf(Predicate<? super Long> filter) {
         Objects.requireNonNull(filter);
         return false;
      }

      public long[] toLongArray() {
         return LongArrays.EMPTY_ARRAY;
      }

      /** @deprecated */
      @Deprecated
      public long[] toLongArray(long[] a) {
         return a;
      }

      public void forEach(java.util.function.LongConsumer action) {
      }

      public boolean containsAll(LongCollection c) {
         return c.isEmpty();
      }

      public boolean addAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(java.util.function.LongPredicate filter) {
         Objects.requireNonNull(filter);
         return false;
      }
   }
}
