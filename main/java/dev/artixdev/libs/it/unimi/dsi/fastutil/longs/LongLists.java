package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class LongLists {
   public static final LongLists.EmptyList EMPTY_LIST = new LongLists.EmptyList();

   private LongLists() {
   }

   public static LongList shuffle(LongList l, Random random) {
      int i = l.size();

      while(i-- != 0) {
         int p = random.nextInt(i + 1);
         long t = l.getLong(i);
         l.set(i, l.getLong(p));
         l.set(p, t);
      }

      return l;
   }

   public static LongList emptyList() {
      return EMPTY_LIST;
   }

   public static LongList singleton(long element) {
      return new LongLists.Singleton(element);
   }

   public static LongList singleton(Object element) {
      return new LongLists.Singleton((Long)element);
   }

   public static LongList synchronize(LongList l) {
      return (LongList)(l instanceof RandomAccess ? new LongLists.SynchronizedRandomAccessList(l) : new LongLists.SynchronizedList(l));
   }

   public static LongList synchronize(LongList l, Object sync) {
      return (LongList)(l instanceof RandomAccess ? new LongLists.SynchronizedRandomAccessList(l, sync) : new LongLists.SynchronizedList(l, sync));
   }

   public static LongList unmodifiable(LongList l) {
      return (LongList)(l instanceof RandomAccess ? new LongLists.UnmodifiableRandomAccessList(l) : new LongLists.UnmodifiableList(l));
   }

   public static class EmptyList extends LongCollections.EmptyCollection implements Serializable, Cloneable, RandomAccess, LongList {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyList() {
      }

      public long getLong(int i) {
         throw new IndexOutOfBoundsException();
      }

      public boolean rem(long k) {
         throw new UnsupportedOperationException();
      }

      public long removeLong(int i) {
         throw new UnsupportedOperationException();
      }

      public void add(int index, long k) {
         throw new UnsupportedOperationException();
      }

      public long set(int index, long k) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(long k) {
         return -1;
      }

      public int lastIndexOf(long k) {
         return -1;
      }

      public boolean addAll(int i, Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Long> operator) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.LongUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(LongList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, LongList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void add(int index, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long get(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long set(int index, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long remove(int k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public int indexOf(Object k) {
         return -1;
      }

      /** @deprecated */
      @Deprecated
      public int lastIndexOf(Object k) {
         return -1;
      }

      public void sort(LongComparator comparator) {
      }

      public void unstableSort(LongComparator comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Long> comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Long> comparator) {
      }

      public LongListIterator listIterator() {
         return LongIterators.EMPTY_ITERATOR;
      }

      public LongListIterator iterator() {
         return LongIterators.EMPTY_ITERATOR;
      }

      public LongListIterator listIterator(int i) {
         if (i == 0) {
            return LongIterators.EMPTY_ITERATOR;
         } else {
            throw new IndexOutOfBoundsException(String.valueOf(i));
         }
      }

      public LongList subList(int from, int to) {
         if (from == 0 && to == 0) {
            return this;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public void getElements(int from, long[] a, int offset, int length) {
         if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
            throw new IndexOutOfBoundsException();
         }
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int s) {
         throw new UnsupportedOperationException();
      }

      public int compareTo(List<? extends Long> o) {
         if (o == this) {
            return 0;
         } else {
            return o.isEmpty() ? 0 : -1;
         }
      }

      public Object clone() {
         return LongLists.EMPTY_LIST;
      }

      public int hashCode() {
         return 1;
      }

      public boolean equals(Object o) {
         return o instanceof List && ((List)o).isEmpty();
      }

      public String toString() {
         return "[]";
      }

      private Object readResolve() {
         return LongLists.EMPTY_LIST;
      }
   }

   public static class Singleton extends AbstractLongList implements Serializable, Cloneable, RandomAccess {
      private static final long serialVersionUID = -7046029254386353129L;
      private final long element;

      protected Singleton(long element) {
         this.element = element;
      }

      public long getLong(int i) {
         if (i == 0) {
            return this.element;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public boolean rem(long k) {
         throw new UnsupportedOperationException();
      }

      public long removeLong(int i) {
         throw new UnsupportedOperationException();
      }

      public boolean contains(long k) {
         return k == this.element;
      }

      public int indexOf(long k) {
         return k == this.element ? 0 : -1;
      }

      public long[] toLongArray() {
         return new long[]{this.element};
      }

      public LongListIterator listIterator() {
         return LongIterators.singleton(this.element);
      }

      public LongListIterator iterator() {
         return this.listIterator();
      }

      public LongSpliterator spliterator() {
         return LongSpliterators.singleton(this.element);
      }

      public LongListIterator listIterator(int i) {
         if (i <= 1 && i >= 0) {
            LongListIterator l = this.listIterator();
            if (i == 1) {
               l.nextLong();
            }

            return l;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public LongList subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return (LongList)(from == 0 && to == 1 ? this : LongLists.EMPTY_LIST);
         }
      }

      /** @deprecated */
      @Deprecated
      public void forEach(Consumer<? super Long> action) {
         action.accept(this.element);
      }

      public boolean addAll(int i, Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
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

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Long> operator) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.LongUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      public void forEach(java.util.function.LongConsumer action) {
         action.accept(this.element);
      }

      public boolean addAll(LongList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, LongList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, LongCollection c) {
         throw new UnsupportedOperationException();
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

      public void sort(LongComparator comparator) {
      }

      public void unstableSort(LongComparator comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Long> comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Long> comparator) {
      }

      public void getElements(int from, long[] a, int offset, int length) {
         if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
         } else if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
         } else if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
         } else if (length > 0) {
            a[offset] = this.element;
         }
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void setElements(long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return 1;
      }

      public void size(int size) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedRandomAccessList extends LongLists.SynchronizedList implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected SynchronizedRandomAccessList(LongList l, Object sync) {
         super(l, sync);
      }

      protected SynchronizedRandomAccessList(LongList l) {
         super(l);
      }

      public LongList subList(int from, int to) {
         synchronized(this.sync) {
            return new LongLists.SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
         }
      }
   }

   public static class SynchronizedList extends LongCollections.SynchronizedCollection implements Serializable, LongList {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongList list;

      protected SynchronizedList(LongList l, Object sync) {
         super(l, sync);
         this.list = l;
      }

      protected SynchronizedList(LongList l) {
         super(l);
         this.list = l;
      }

      public long getLong(int i) {
         synchronized(this.sync) {
            return this.list.getLong(i);
         }
      }

      public long set(int i, long k) {
         synchronized(this.sync) {
            return this.list.set(i, k);
         }
      }

      public void add(int i, long k) {
         synchronized(this.sync) {
            this.list.add(i, k);
         }
      }

      public long removeLong(int i) {
         synchronized(this.sync) {
            return this.list.removeLong(i);
         }
      }

      public int indexOf(long k) {
         synchronized(this.sync) {
            return this.list.indexOf(k);
         }
      }

      public int lastIndexOf(long k) {
         synchronized(this.sync) {
            return this.list.lastIndexOf(k);
         }
      }

      public boolean removeIf(java.util.function.LongPredicate filter) {
         synchronized(this.sync) {
            return this.list.removeIf(filter);
         }
      }

      public void replaceAll(java.util.function.LongUnaryOperator operator) {
         synchronized(this.sync) {
            this.list.replaceAll(operator);
         }
      }

      public boolean addAll(int index, Collection<? extends Long> c) {
         synchronized(this.sync) {
            return this.list.addAll(index, (Collection)c);
         }
      }

      public void getElements(int from, long[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.getElements(from, a, offset, length);
         }
      }

      public void removeElements(int from, int to) {
         synchronized(this.sync) {
            this.list.removeElements(from, to);
         }
      }

      public void addElements(int index, long[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.addElements(index, a, offset, length);
         }
      }

      public void addElements(int index, long[] a) {
         synchronized(this.sync) {
            this.list.addElements(index, a);
         }
      }

      public void setElements(long[] a) {
         synchronized(this.sync) {
            this.list.setElements(a);
         }
      }

      public void setElements(int index, long[] a) {
         synchronized(this.sync) {
            this.list.setElements(index, a);
         }
      }

      public void setElements(int index, long[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.setElements(index, a, offset, length);
         }
      }

      public void size(int size) {
         synchronized(this.sync) {
            this.list.size(size);
         }
      }

      public LongListIterator listIterator() {
         return this.list.listIterator();
      }

      public LongListIterator iterator() {
         return this.listIterator();
      }

      public LongListIterator listIterator(int i) {
         return this.list.listIterator(i);
      }

      public LongList subList(int from, int to) {
         synchronized(this.sync) {
            return new LongLists.SynchronizedList(this.list.subList(from, to), this.sync);
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

      public int hashCode() {
         synchronized(this.sync) {
            return this.collection.hashCode();
         }
      }

      public int compareTo(List<? extends Long> o) {
         synchronized(this.sync) {
            return this.list.compareTo(o);
         }
      }

      public boolean addAll(int index, LongCollection c) {
         synchronized(this.sync) {
            return this.list.addAll(index, c);
         }
      }

      public boolean addAll(int index, LongList l) {
         synchronized(this.sync) {
            return this.list.addAll(index, l);
         }
      }

      public boolean addAll(LongList l) {
         synchronized(this.sync) {
            return this.list.addAll(l);
         }
      }

      /** @deprecated */
      @Deprecated
      public Long get(int i) {
         synchronized(this.sync) {
            return this.list.get(i);
         }
      }

      /** @deprecated */
      @Deprecated
      public void add(int i, Long k) {
         synchronized(this.sync) {
            this.list.add(i, k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Long set(int index, Long k) {
         synchronized(this.sync) {
            return this.list.set(index, k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Long remove(int i) {
         synchronized(this.sync) {
            return this.list.remove(i);
         }
      }

      /** @deprecated */
      @Deprecated
      public int indexOf(Object o) {
         synchronized(this.sync) {
            return this.list.indexOf(o);
         }
      }

      /** @deprecated */
      @Deprecated
      public int lastIndexOf(Object o) {
         synchronized(this.sync) {
            return this.list.lastIndexOf(o);
         }
      }

      public void sort(LongComparator comparator) {
         synchronized(this.sync) {
            this.list.sort(comparator);
         }
      }

      public void unstableSort(LongComparator comparator) {
         synchronized(this.sync) {
            this.list.unstableSort(comparator);
         }
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Long> comparator) {
         synchronized(this.sync) {
            this.list.sort(comparator);
         }
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Long> comparator) {
         synchronized(this.sync) {
            this.list.unstableSort(comparator);
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }
   }

   public static class UnmodifiableRandomAccessList extends LongLists.UnmodifiableList implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected UnmodifiableRandomAccessList(LongList l) {
         super(l);
      }

      public LongList subList(int from, int to) {
         return new LongLists.UnmodifiableRandomAccessList(this.list.subList(from, to));
      }
   }

   public static class UnmodifiableList extends LongCollections.UnmodifiableCollection implements Serializable, LongList {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongList list;

      protected UnmodifiableList(LongList l) {
         super(l);
         this.list = l;
      }

      public long getLong(int i) {
         return this.list.getLong(i);
      }

      public long set(int i, long k) {
         throw new UnsupportedOperationException();
      }

      public void add(int i, long k) {
         throw new UnsupportedOperationException();
      }

      public long removeLong(int i) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(long k) {
         return this.list.indexOf(k);
      }

      public int lastIndexOf(long k) {
         return this.list.lastIndexOf(k);
      }

      public boolean addAll(int index, Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Long> operator) {
         throw new UnsupportedOperationException();
      }

      public void getElements(int from, long[] a, int offset, int length) {
         this.list.getElements(from, a, offset, length);
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int size) {
         this.list.size(size);
      }

      public LongListIterator listIterator() {
         return LongIterators.unmodifiable(this.list.listIterator());
      }

      public LongListIterator iterator() {
         return this.listIterator();
      }

      public LongListIterator listIterator(int i) {
         return LongIterators.unmodifiable(this.list.listIterator(i));
      }

      public LongList subList(int from, int to) {
         return new LongLists.UnmodifiableList(this.list.subList(from, to));
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      public int compareTo(List<? extends Long> o) {
         return this.list.compareTo(o);
      }

      public boolean addAll(int index, LongCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(LongList l) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int index, LongList l) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.LongUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long get(int i) {
         return this.list.get(i);
      }

      /** @deprecated */
      @Deprecated
      public void add(int i, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long set(int index, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Long remove(int i) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public int indexOf(Object o) {
         return this.list.indexOf(o);
      }

      /** @deprecated */
      @Deprecated
      public int lastIndexOf(Object o) {
         return this.list.lastIndexOf(o);
      }

      public void sort(LongComparator comparator) {
         throw new UnsupportedOperationException();
      }

      public void unstableSort(LongComparator comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Long> comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Long> comparator) {
         throw new UnsupportedOperationException();
      }
   }

   abstract static class ImmutableListBase extends AbstractLongList implements LongList {
      /** @deprecated */
      @Deprecated
      public final void add(int index, long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean add(long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, Collection<? extends Long> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final long removeLong(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean rem(long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeIf(Predicate<? super Long> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeIf(java.util.function.LongPredicate c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void replaceAll(UnaryOperator<Long> operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void replaceAll(java.util.function.LongUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void add(int index, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean add(Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final Long remove(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean remove(Object k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final Long set(int index, Long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(LongList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, LongCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, LongList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean retainAll(LongCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final long set(int index, long k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void clear() {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void size(int size) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void addElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void setElements(int index, long[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void sort(LongComparator comp) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void unstableSort(LongComparator comp) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void sort(Comparator<? super Long> comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void unstableSort(Comparator<? super Long> comparator) {
         throw new UnsupportedOperationException();
      }
   }
}
