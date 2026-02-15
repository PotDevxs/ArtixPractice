package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class LongSets {
   static final int ARRAY_SET_CUTOFF = 4;
   public static final LongSets.EmptySet EMPTY_SET = new LongSets.EmptySet();
   static final LongSet UNMODIFIABLE_EMPTY_SET;

   private LongSets() {
   }

   public static LongSet emptySet() {
      return EMPTY_SET;
   }

   public static LongSet singleton(long element) {
      return new LongSets.Singleton(element);
   }

   public static LongSet singleton(Long element) {
      return new LongSets.Singleton(element);
   }

   public static LongSet synchronize(LongSet s) {
      return new LongSets.SynchronizedSet(s);
   }

   public static LongSet synchronize(LongSet s, Object sync) {
      return new LongSets.SynchronizedSet(s, sync);
   }

   public static LongSet unmodifiable(LongSet s) {
      return new LongSets.UnmodifiableSet(s);
   }

   public static LongSet fromTo(final long from, final long to) {
      return new AbstractLongSet() {
         public boolean contains(long x) {
            return x >= from && x < to;
         }

         public LongIterator iterator() {
            return LongIterators.fromTo(from, to);
         }

         public int size() {
            long size = to - from;
            return size >= 0L && size <= 2147483647L ? (int)size : Integer.MAX_VALUE;
         }
      };
   }

   public static LongSet from(final long from) {
      return new AbstractLongSet() {
         public boolean contains(long x) {
            return x >= from;
         }

         public LongIterator iterator() {
            return LongIterators.concat(LongIterators.fromTo(from, Long.MAX_VALUE), LongSets.singleton(Long.MAX_VALUE).iterator());
         }

         public int size() {
            long size = Long.MAX_VALUE - from + 1L;
            return size >= 0L && size <= 2147483647L ? (int)size : Integer.MAX_VALUE;
         }
      };
   }

   public static LongSet to(final long to) {
      return new AbstractLongSet() {
         public boolean contains(long x) {
            return x < to;
         }

         public LongIterator iterator() {
            return LongIterators.fromTo(Long.MIN_VALUE, to);
         }

         public int size() {
            long size = to - Long.MIN_VALUE;
            return size >= 0L && size <= 2147483647L ? (int)size : Integer.MAX_VALUE;
         }
      };
   }

   static {
      UNMODIFIABLE_EMPTY_SET = unmodifiable(new LongArraySet(LongArrays.EMPTY_ARRAY));
   }

   public static class EmptySet extends LongCollections.EmptyCollection implements Serializable, Cloneable, LongSet {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptySet() {
      }

      public boolean remove(long ok) {
         throw new UnsupportedOperationException();
      }

      public Object clone() {
         return LongSets.EMPTY_SET;
      }

      public boolean equals(Object o) {
         return o instanceof Set && ((Set)o).isEmpty();
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(long k) {
         return super.rem(k);
      }

      private Object readResolve() {
         return LongSets.EMPTY_SET;
      }
   }

   public static class Singleton extends AbstractLongSet implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final long element;

      protected Singleton(long element) {
         this.element = element;
      }

      public boolean contains(long k) {
         return k == this.element;
      }

      public boolean remove(long k) {
         throw new UnsupportedOperationException();
      }

      public LongListIterator iterator() {
         return LongIterators.singleton(this.element);
      }

      public LongSpliterator spliterator() {
         return LongSpliterators.singleton(this.element);
      }

      public int size() {
         return 1;
      }

      public long[] toLongArray() {
         return new long[]{this.element};
      }

      /** @deprecated */
      @Deprecated
      public void forEach(Consumer<? super Long> action) {
         action.accept(this.element);
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
         throw new UnsupportedOperationException();
      }

      public void forEach(java.util.function.LongConsumer action) {
         action.accept(this.element);
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
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Object[] toArray() {
         return new Object[]{this.element};
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedSet extends LongCollections.SynchronizedCollection implements Serializable, LongSet {
      private static final long serialVersionUID = -7046029254386353129L;

      protected SynchronizedSet(LongSet s, Object sync) {
         super(s, sync);
      }

      protected SynchronizedSet(LongSet s) {
         super(s);
      }

      public boolean remove(long k) {
         synchronized(this.sync) {
            return this.collection.rem(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(long k) {
         return super.rem(k);
      }
   }

   public static class UnmodifiableSet extends LongCollections.UnmodifiableCollection implements Serializable, LongSet {
      private static final long serialVersionUID = -7046029254386353129L;

      protected UnmodifiableSet(LongSet s) {
         super(s);
      }

      public boolean remove(long k) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(long k) {
         return super.rem(k);
      }
   }
}
