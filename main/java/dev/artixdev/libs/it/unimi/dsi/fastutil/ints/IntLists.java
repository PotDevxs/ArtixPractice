package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

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

public final class IntLists {
   public static final IntLists.EmptyList EMPTY_LIST = new IntLists.EmptyList();

   private IntLists() {
   }

   public static IntList shuffle(IntList l, Random random) {
      int i = l.size();

      while(i-- != 0) {
         int p = random.nextInt(i + 1);
         int t = l.getInt(i);
         l.set(i, l.getInt(p));
         l.set(p, t);
      }

      return l;
   }

   public static IntList emptyList() {
      return EMPTY_LIST;
   }

   public static IntList singleton(int element) {
      return new IntLists.Singleton(element);
   }

   public static IntList singleton(Object element) {
      return new IntLists.Singleton((Integer)element);
   }

   public static IntList synchronize(IntList l) {
      return (IntList)(l instanceof RandomAccess ? new IntLists.SynchronizedRandomAccessList(l) : new IntLists.SynchronizedList(l));
   }

   public static IntList synchronize(IntList l, Object sync) {
      return (IntList)(l instanceof RandomAccess ? new IntLists.SynchronizedRandomAccessList(l, sync) : new IntLists.SynchronizedList(l, sync));
   }

   public static IntList unmodifiable(IntList l) {
      return (IntList)(l instanceof RandomAccess ? new IntLists.UnmodifiableRandomAccessList(l) : new IntLists.UnmodifiableList(l));
   }

   public static class EmptyList extends IntCollections.EmptyCollection implements Serializable, Cloneable, RandomAccess, IntList {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyList() {
      }

      public int getInt(int i) {
         throw new IndexOutOfBoundsException();
      }

      public boolean rem(int k) {
         throw new UnsupportedOperationException();
      }

      public int removeInt(int i) {
         throw new UnsupportedOperationException();
      }

      public void add(int index, int k) {
         throw new UnsupportedOperationException();
      }

      public int set(int index, int k) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(int k) {
         return -1;
      }

      public int lastIndexOf(int k) {
         return -1;
      }

      public boolean addAll(int i, Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Integer> operator) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.IntUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(IntList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, IntList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void add(int index, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer get(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean add(Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer set(int index, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(int k) {
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

      public void sort(IntComparator comparator) {
      }

      public void unstableSort(IntComparator comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Integer> comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Integer> comparator) {
      }

      public IntListIterator listIterator() {
         return IntIterators.EMPTY_ITERATOR;
      }

      public IntListIterator iterator() {
         return IntIterators.EMPTY_ITERATOR;
      }

      public IntListIterator listIterator(int i) {
         if (i == 0) {
            return IntIterators.EMPTY_ITERATOR;
         } else {
            throw new IndexOutOfBoundsException(String.valueOf(i));
         }
      }

      public IntList subList(int from, int to) {
         if (from == 0 && to == 0) {
            return this;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public void getElements(int from, int[] a, int offset, int length) {
         if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
            throw new IndexOutOfBoundsException();
         }
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int s) {
         throw new UnsupportedOperationException();
      }

      public int compareTo(List<? extends Integer> o) {
         if (o == this) {
            return 0;
         } else {
            return o.isEmpty() ? 0 : -1;
         }
      }

      public Object clone() {
         return IntLists.EMPTY_LIST;
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
         return IntLists.EMPTY_LIST;
      }
   }

   public static class Singleton extends AbstractIntList implements Serializable, Cloneable, RandomAccess {
      private static final long serialVersionUID = -7046029254386353129L;
      private final int element;

      protected Singleton(int element) {
         this.element = element;
      }

      public int getInt(int i) {
         if (i == 0) {
            return this.element;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public boolean rem(int k) {
         throw new UnsupportedOperationException();
      }

      public int removeInt(int i) {
         throw new UnsupportedOperationException();
      }

      public boolean contains(int k) {
         return k == this.element;
      }

      public int indexOf(int k) {
         return k == this.element ? 0 : -1;
      }

      public int[] toIntArray() {
         return new int[]{this.element};
      }

      public IntListIterator listIterator() {
         return IntIterators.singleton(this.element);
      }

      public IntListIterator iterator() {
         return this.listIterator();
      }

      public IntSpliterator spliterator() {
         return IntSpliterators.singleton(this.element);
      }

      public IntListIterator listIterator(int i) {
         if (i <= 1 && i >= 0) {
            IntListIterator l = this.listIterator();
            if (i == 1) {
               l.nextInt();
            }

            return l;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public IntList subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return (IntList)(from == 0 && to == 1 ? this : IntLists.EMPTY_LIST);
         }
      }

      /** @deprecated */
      @Deprecated
      public void forEach(Consumer<? super Integer> action) {
         action.accept(this.element);
      }

      public boolean addAll(int i, Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
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
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Integer> operator) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.IntUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      public void forEach(java.util.function.IntConsumer action) {
         action.accept(this.element);
      }

      public boolean addAll(IntList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, IntList c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int i, IntCollection c) {
         throw new UnsupportedOperationException();
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
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Object[] toArray() {
         return new Object[]{this.element};
      }

      public void sort(IntComparator comparator) {
      }

      public void unstableSort(IntComparator comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Integer> comparator) {
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Integer> comparator) {
      }

      public void getElements(int from, int[] a, int offset, int length) {
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

      public void addElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a, int offset, int length) {
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

   public static class SynchronizedRandomAccessList extends IntLists.SynchronizedList implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected SynchronizedRandomAccessList(IntList l, Object sync) {
         super(l, sync);
      }

      protected SynchronizedRandomAccessList(IntList l) {
         super(l);
      }

      public IntList subList(int from, int to) {
         synchronized(this.sync) {
            return new IntLists.SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
         }
      }
   }

   public static class SynchronizedList extends IntCollections.SynchronizedCollection implements Serializable, IntList {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final IntList list;

      protected SynchronizedList(IntList l, Object sync) {
         super(l, sync);
         this.list = l;
      }

      protected SynchronizedList(IntList l) {
         super(l);
         this.list = l;
      }

      public int getInt(int i) {
         synchronized(this.sync) {
            return this.list.getInt(i);
         }
      }

      public int set(int i, int k) {
         synchronized(this.sync) {
            return this.list.set(i, k);
         }
      }

      public void add(int i, int k) {
         synchronized(this.sync) {
            this.list.add(i, k);
         }
      }

      public int removeInt(int i) {
         synchronized(this.sync) {
            return this.list.removeInt(i);
         }
      }

      public int indexOf(int k) {
         synchronized(this.sync) {
            return this.list.indexOf(k);
         }
      }

      public int lastIndexOf(int k) {
         synchronized(this.sync) {
            return this.list.lastIndexOf(k);
         }
      }

      public boolean removeIf(java.util.function.IntPredicate filter) {
         synchronized(this.sync) {
            return this.list.removeIf(filter);
         }
      }

      public void replaceAll(java.util.function.IntUnaryOperator operator) {
         synchronized(this.sync) {
            this.list.replaceAll(operator);
         }
      }

      public boolean addAll(int index, Collection<? extends Integer> c) {
         synchronized(this.sync) {
            return this.list.addAll(index, (Collection)c);
         }
      }

      public void getElements(int from, int[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.getElements(from, a, offset, length);
         }
      }

      public void removeElements(int from, int to) {
         synchronized(this.sync) {
            this.list.removeElements(from, to);
         }
      }

      public void addElements(int index, int[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.addElements(index, a, offset, length);
         }
      }

      public void addElements(int index, int[] a) {
         synchronized(this.sync) {
            this.list.addElements(index, a);
         }
      }

      public void setElements(int[] a) {
         synchronized(this.sync) {
            this.list.setElements(a);
         }
      }

      public void setElements(int index, int[] a) {
         synchronized(this.sync) {
            this.list.setElements(index, a);
         }
      }

      public void setElements(int index, int[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.setElements(index, a, offset, length);
         }
      }

      public void size(int size) {
         synchronized(this.sync) {
            this.list.size(size);
         }
      }

      public IntListIterator listIterator() {
         return this.list.listIterator();
      }

      public IntListIterator iterator() {
         return this.listIterator();
      }

      public IntListIterator listIterator(int i) {
         return this.list.listIterator(i);
      }

      public IntList subList(int from, int to) {
         synchronized(this.sync) {
            return new IntLists.SynchronizedList(this.list.subList(from, to), this.sync);
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

      public int compareTo(List<? extends Integer> o) {
         synchronized(this.sync) {
            return this.list.compareTo(o);
         }
      }

      public boolean addAll(int index, IntCollection c) {
         synchronized(this.sync) {
            return this.list.addAll(index, c);
         }
      }

      public boolean addAll(int index, IntList l) {
         synchronized(this.sync) {
            return this.list.addAll(index, l);
         }
      }

      public boolean addAll(IntList l) {
         synchronized(this.sync) {
            return this.list.addAll(l);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer get(int i) {
         synchronized(this.sync) {
            return this.list.get(i);
         }
      }

      /** @deprecated */
      @Deprecated
      public void add(int i, Integer k) {
         synchronized(this.sync) {
            this.list.add(i, k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer set(int index, Integer k) {
         synchronized(this.sync) {
            return this.list.set(index, k);
         }
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(int i) {
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

      public void sort(IntComparator comparator) {
         synchronized(this.sync) {
            this.list.sort(comparator);
         }
      }

      public void unstableSort(IntComparator comparator) {
         synchronized(this.sync) {
            this.list.unstableSort(comparator);
         }
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Integer> comparator) {
         synchronized(this.sync) {
            this.list.sort(comparator);
         }
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Integer> comparator) {
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

   public static class UnmodifiableRandomAccessList extends IntLists.UnmodifiableList implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected UnmodifiableRandomAccessList(IntList l) {
         super(l);
      }

      public IntList subList(int from, int to) {
         return new IntLists.UnmodifiableRandomAccessList(this.list.subList(from, to));
      }
   }

   public static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements Serializable, IntList {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final IntList list;

      protected UnmodifiableList(IntList l) {
         super(l);
         this.list = l;
      }

      public int getInt(int i) {
         return this.list.getInt(i);
      }

      public int set(int i, int k) {
         throw new UnsupportedOperationException();
      }

      public void add(int i, int k) {
         throw new UnsupportedOperationException();
      }

      public int removeInt(int i) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(int k) {
         return this.list.indexOf(k);
      }

      public int lastIndexOf(int k) {
         return this.list.lastIndexOf(k);
      }

      public boolean addAll(int index, Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void replaceAll(UnaryOperator<Integer> operator) {
         throw new UnsupportedOperationException();
      }

      public void getElements(int from, int[] a, int offset, int length) {
         this.list.getElements(from, a, offset, length);
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int size) {
         this.list.size(size);
      }

      public IntListIterator listIterator() {
         return IntIterators.unmodifiable(this.list.listIterator());
      }

      public IntListIterator iterator() {
         return this.listIterator();
      }

      public IntListIterator listIterator(int i) {
         return IntIterators.unmodifiable(this.list.listIterator(i));
      }

      public IntList subList(int from, int to) {
         return new IntLists.UnmodifiableList(this.list.subList(from, to));
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      public int compareTo(List<? extends Integer> o) {
         return this.list.compareTo(o);
      }

      public boolean addAll(int index, IntCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(IntList l) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(int index, IntList l) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(java.util.function.IntUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer get(int i) {
         return this.list.get(i);
      }

      /** @deprecated */
      @Deprecated
      public void add(int i, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer set(int index, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Integer remove(int i) {
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

      public void sort(IntComparator comparator) {
         throw new UnsupportedOperationException();
      }

      public void unstableSort(IntComparator comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void sort(Comparator<? super Integer> comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public void unstableSort(Comparator<? super Integer> comparator) {
         throw new UnsupportedOperationException();
      }
   }

   abstract static class ImmutableListBase extends AbstractIntList implements IntList {
      /** @deprecated */
      @Deprecated
      public final void add(int index, int k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean add(int k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, Collection<? extends Integer> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final int removeInt(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean rem(int k) {
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
      public final boolean removeIf(Predicate<? super Integer> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeIf(java.util.function.IntPredicate c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void replaceAll(UnaryOperator<Integer> operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void replaceAll(java.util.function.IntUnaryOperator operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void add(int index, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean add(Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final Integer remove(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean remove(Object k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final Integer set(int index, Integer k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(IntList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, IntCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, IntList c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean removeAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean retainAll(IntCollection c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final int set(int index, int k) {
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
      public final void addElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void setElements(int index, int[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void sort(IntComparator comp) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void unstableSort(IntComparator comp) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void sort(Comparator<? super Integer> comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void unstableSort(Comparator<? super Integer> comparator) {
         throw new UnsupportedOperationException();
      }
   }
}
