package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

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
import java.util.stream.IntStream;
import java.util.stream.Stream;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrays;

public final class IntCollections {
   private IntCollections() {
   }

   public static IntCollection synchronize(IntCollection c) {
      return new IntCollections.SynchronizedCollection(c);
   }

   public static IntCollection synchronize(IntCollection c, Object sync) {
      return new IntCollections.SynchronizedCollection(c, sync);
   }

   public static IntCollection unmodifiable(IntCollection c) {
      return new IntCollections.UnmodifiableCollection(c);
   }

   public static IntCollection asCollection(IntIterable iterable) {
      return (IntCollection)(iterable instanceof IntCollection ? (IntCollection)iterable : new IntCollections.IterableCollection(iterable));
   }

   static class SynchronizedCollection implements Serializable, IntCollection {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final IntCollection collection;
      protected final Object sync;

      protected SynchronizedCollection(IntCollection c, Object sync) {
         this.collection = (IntCollection)Objects.requireNonNull(c);
         this.sync = sync;
      }

      protected SynchronizedCollection(IntCollection c) {
         this.collection = (IntCollection)Objects.requireNonNull(c);
         this.sync = this;
      }

      public boolean add(int k) {
         synchronized(this.sync) {
            return this.collection.add(k);
         }
      }

      public boolean contains(int k) {
         synchronized(this.sync) {
            return this.collection.contains(k);
         }
      }

      public boolean rem(int k) {
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

      public int[] toIntArray() {
         synchronized(this.sync) {
            return this.collection.toIntArray();
         }
      }

      public Object[] toArray() {
         synchronized(this.sync) {
            return this.collection.toArray();
         }
      }

      /** @deprecated */
      @Deprecated
      public int[] toIntArray(int[] a) {
         return this.toArray(a);
      }

      public int[] toArray(int[] a) {
         synchronized(this.sync) {
            return this.collection.toArray(a);
         }
      }

      public boolean addAll(IntCollection c) {
         synchronized(this.sync) {
            return this.collection.addAll(c);
         }
      }

      public boolean containsAll(IntCollection c) {
         synchronized(this.sync) {
            return this.collection.containsAll(c);
         }
      }

      public boolean removeAll(IntCollection c) {
         synchronized(this.sync) {
            return this.collection.removeAll(c);
         }
      }

      public boolean retainAll(IntCollection c) {
         synchronized(this.sync) {
            return this.collection.retainAll(c);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Integer k) {
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

      public IntIterator intIterator() {
         return this.collection.intIterator();
      }

      public IntSpliterator intSpliterator() {
         return this.collection.intSpliterator();
      }

      public IntStream intStream() {
         return this.collection.intStream();
      }

      public IntStream intParallelStream() {
         return this.collection.intParallelStream();
      }

      public <T> T[] toArray(T[] a) {
         synchronized(this.sync) {
            return this.collection.toArray(a);
         }
      }

      public IntIterator iterator() {
         return this.collection.iterator();
      }

      public IntSpliterator spliterator() {
         return this.collection.spliterator();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Integer> stream() {
         return this.collection.stream();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Integer> parallelStream() {
         return this.collection.parallelStream();
      }

      public void forEach(java.util.function.IntConsumer action) {
         synchronized(this.sync) {
            this.collection.forEach(action);
         }
      }

      public boolean addAll(Collection<? extends Integer> c) {
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

      public boolean removeIf(java.util.function.IntPredicate filter) {
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

   static class UnmodifiableCollection implements Serializable, IntCollection {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final IntCollection collection;

      protected UnmodifiableCollection(IntCollection c) {
         this.collection = (IntCollection)Objects.requireNonNull(c);
      }

      public boolean add(int k) {
         throw new UnsupportedOperationException();
      }

      public boolean rem(int k) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return this.collection.size();
      }

      public boolean isEmpty() {
         return this.collection.isEmpty();
      }

      public boolean contains(int o) {
         return this.collection.contains(o);
      }

      public IntIterator iterator() {
         return IntIterators.unmodifiable(this.collection.iterator());
      }

      public IntSpliterator spliterator() {
         return this.collection.spliterator();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Integer> stream() {
         return this.collection.stream();
      }

      /** @deprecated */
      @Deprecated
      public Stream<Integer> parallelStream() {
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

      public void forEach(java.util.function.IntConsumer action) {
         this.collection.forEach(action);
      }

      public boolean containsAll(Collection<?> c) {
         return this.collection.containsAll(c);
      }

      public boolean addAll(Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(java.util.function.IntPredicate filter) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Integer k) {
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

      public int[] toIntArray() {
         return this.collection.toIntArray();
      }

      /** @deprecated */
      @Deprecated
      public int[] toIntArray(int[] a) {
         return this.toArray(a);
      }

      public int[] toArray(int[] a) {
         return this.collection.toArray(a);
      }

      public boolean containsAll(IntCollection c) {
         return this.collection.containsAll(c);
      }

      public boolean addAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public IntIterator intIterator() {
         return this.collection.intIterator();
      }

      public IntSpliterator intSpliterator() {
         return this.collection.intSpliterator();
      }

      public IntStream intStream() {
         return this.collection.intStream();
      }

      public IntStream intParallelStream() {
         return this.collection.intParallelStream();
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

   public static class IterableCollection extends AbstractIntCollection implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final IntIterable iterable;

      protected IterableCollection(IntIterable iterable) {
         this.iterable = (IntIterable)Objects.requireNonNull(iterable);
      }

      public int size() {
         long size = this.iterable.spliterator().getExactSizeIfKnown();
         if (size >= 0L) {
            return (int)Math.min(2147483647L, size);
         } else {
            int c = 0;

            for(IntIterator iterator = this.iterator(); iterator.hasNext(); ++c) {
               iterator.nextInt();
            }

            return c;
         }
      }

      public boolean isEmpty() {
         return !this.iterable.iterator().hasNext();
      }

      public IntIterator iterator() {
         return this.iterable.iterator();
      }

      public IntSpliterator spliterator() {
         return this.iterable.spliterator();
      }

      public IntIterator intIterator() {
         return this.iterable.intIterator();
      }

      public IntSpliterator intSpliterator() {
         return this.iterable.intSpliterator();
      }
   }

   static class SizeDecreasingSupplier<C extends IntCollection> implements Supplier<C> {
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

         return this.builder.apply(expectedNeededNextSize);
      }
   }

   public abstract static class EmptyCollection extends AbstractIntCollection {
      protected EmptyCollection() {
      }

      public boolean contains(int k) {
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

      public IntBidirectionalIterator iterator() {
         return IntIterators.EMPTY_ITERATOR;
      }

      public IntSpliterator spliterator() {
         return IntSpliterators.EMPTY_SPLITERATOR;
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
      public void forEach(Consumer<? super Integer> action) {
      }

      public boolean containsAll(Collection<?> c) {
         return c.isEmpty();
      }

      public boolean addAll(Collection<? extends Integer> c) {
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
      public boolean removeIf(Predicate<? super Integer> filter) {
         Objects.requireNonNull(filter);
         return false;
      }

      public int[] toIntArray() {
         return IntArrays.EMPTY_ARRAY;
      }

      /** @deprecated */
      @Deprecated
      public int[] toIntArray(int[] a) {
         return a;
      }

      public void forEach(java.util.function.IntConsumer action) {
      }

      public boolean containsAll(IntCollection c) {
         return c.isEmpty();
      }

      public boolean addAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(java.util.function.IntPredicate filter) {
         Objects.requireNonNull(filter);
         return false;
      }
   }
}
