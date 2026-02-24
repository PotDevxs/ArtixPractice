package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class ObjectLists {
   public static final ObjectLists.EmptyList EMPTY_LIST = new ObjectLists.EmptyList();

   private ObjectLists() {
   }

   public static <K> ObjectList<K> shuffle(ObjectList<K> l, Random random) {
      int i = l.size();

      while(i-- != 0) {
         int p = random.nextInt(i + 1);
         K t = l.get(i);
         l.set(i, l.get(p));
         l.set(p, t);
      }

      return l;
   }

   public static <K> ObjectList<K> emptyList() {
      return EMPTY_LIST;
   }

   public static <K> ObjectList<K> singleton(K element) {
      return new ObjectLists.Singleton(element);
   }

   public static <K> ObjectList<K> synchronize(ObjectList<K> l) {
      return (ObjectList)(l instanceof RandomAccess ? new ObjectLists.SynchronizedRandomAccessList(l) : new ObjectLists.SynchronizedList(l));
   }

   public static <K> ObjectList<K> synchronize(ObjectList<K> l, Object sync) {
      return (ObjectList)(l instanceof RandomAccess ? new ObjectLists.SynchronizedRandomAccessList(l, sync) : new ObjectLists.SynchronizedList(l, sync));
   }

   public static <K> ObjectList<K> unmodifiable(ObjectList<? extends K> l) {
      return (ObjectList)(l instanceof RandomAccess ? new ObjectLists.UnmodifiableRandomAccessList(l) : new ObjectLists.UnmodifiableList(l));
   }

   public static class EmptyList<K> extends ObjectCollections.EmptyCollection<K> implements Serializable, Cloneable, RandomAccess, ObjectList<K> {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyList() {
      }

      public K get(int i) {
         throw new IndexOutOfBoundsException();
      }

      public boolean remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public K remove(int i) {
         throw new UnsupportedOperationException();
      }

      public void add(int index, K k) {
         throw new UnsupportedOperationException();
      }

      public K set(int index, K k) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(Object k) {
         return -1;
      }

      public int lastIndexOf(Object k) {
         return -1;
      }

      public boolean addAll(int i, Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(UnaryOperator<K> operator) {
         throw new UnsupportedOperationException();
      }

      public void sort(Comparator<? super K> comparator) {
      }

      public void unstableSort(Comparator<? super K> comparator) {
      }

      public ObjectListIterator<K> listIterator() {
         return ObjectIterators.EMPTY_ITERATOR;
      }

      public ObjectListIterator<K> iterator() {
         return ObjectIterators.EMPTY_ITERATOR;
      }

      public ObjectListIterator<K> listIterator(int i) {
         if (i == 0) {
            return ObjectIterators.EMPTY_ITERATOR;
         } else {
            throw new IndexOutOfBoundsException(String.valueOf(i));
         }
      }

      public ObjectList<K> subList(int from, int to) {
         if (from == 0 && to == 0) {
            return this;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public void getElements(int from, Object[] a, int offset, int length) {
         if (from != 0 || length != 0 || offset < 0 || offset > a.length) {
            throw new IndexOutOfBoundsException();
         }
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int s) {
         throw new UnsupportedOperationException();
      }

      public int compareTo(List<? extends K> o) {
         if (o == this) {
            return 0;
         } else {
            return o.isEmpty() ? 0 : -1;
         }
      }

      public Object clone() {
         return ObjectLists.EMPTY_LIST;
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
         return ObjectLists.EMPTY_LIST;
      }
   }

   public static class Singleton<K> extends AbstractObjectList<K> implements Serializable, Cloneable, RandomAccess {
      private static final long serialVersionUID = -7046029254386353129L;
      private final K element;

      protected Singleton(K element) {
         this.element = element;
      }

      public K get(int i) {
         if (i == 0) {
            return this.element;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public boolean remove(Object k) {
         throw new UnsupportedOperationException();
      }

      public K remove(int i) {
         throw new UnsupportedOperationException();
      }

      public boolean contains(Object k) {
         return Objects.equals(k, this.element);
      }

      public int indexOf(Object k) {
         return Objects.equals(k, this.element) ? 0 : -1;
      }

      public Object[] toArray() {
         return new Object[]{this.element};
      }

      public ObjectListIterator<K> listIterator() {
         return ObjectIterators.singleton(this.element);
      }

      public ObjectListIterator<K> iterator() {
         return this.listIterator();
      }

      public ObjectSpliterator<K> spliterator() {
         return ObjectSpliterators.singleton(this.element);
      }

      public ObjectListIterator<K> listIterator(int i) {
         if (i <= 1 && i >= 0) {
            ObjectListIterator<K> l = this.listIterator();
            if (i == 1) {
               l.next();
            }

            return l;
         } else {
            throw new IndexOutOfBoundsException();
         }
      }

      public ObjectList<K> subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return (ObjectList)(from == 0 && to == 1 ? this : ObjectLists.EMPTY_LIST);
         }
      }

      public void forEach(Consumer<? super K> action) {
         action.accept(this.element);
      }

      public boolean addAll(int i, Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(Predicate<? super K> filter) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(UnaryOperator<K> operator) {
         throw new UnsupportedOperationException();
      }

      public void sort(Comparator<? super K> comparator) {
      }

      public void unstableSort(Comparator<? super K> comparator) {
      }

      public void getElements(int from, Object[] a, int offset, int length) {
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

      public void addElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void setElements(K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a, int offset, int length) {
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

   public static class SynchronizedRandomAccessList<K> extends ObjectLists.SynchronizedList<K> implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected SynchronizedRandomAccessList(ObjectList<K> l, Object sync) {
         super(l, sync);
      }

      protected SynchronizedRandomAccessList(ObjectList<K> l) {
         super(l);
      }

      public ObjectList<K> subList(int from, int to) {
         synchronized(this.sync) {
            return new ObjectLists.SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
         }
      }
   }

   public static class SynchronizedList<K> extends ObjectCollections.SynchronizedCollection<K> implements Serializable, ObjectList<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final ObjectList<K> list;

      protected SynchronizedList(ObjectList<K> l, Object sync) {
         super(l, sync);
         this.list = l;
      }

      protected SynchronizedList(ObjectList<K> l) {
         super(l);
         this.list = l;
      }

      public K get(int i) {
         synchronized(this.sync) {
            return this.list.get(i);
         }
      }

      public K set(int i, K k) {
         synchronized(this.sync) {
            return this.list.set(i, k);
         }
      }

      public void add(int i, K k) {
         synchronized(this.sync) {
            this.list.add(i, k);
         }
      }

      public K remove(int i) {
         synchronized(this.sync) {
            return this.list.remove(i);
         }
      }

      public int indexOf(Object k) {
         synchronized(this.sync) {
            return this.list.indexOf(k);
         }
      }

      public int lastIndexOf(Object k) {
         synchronized(this.sync) {
            return this.list.lastIndexOf(k);
         }
      }

      public boolean removeIf(Predicate<? super K> filter) {
         synchronized(this.sync) {
            return this.list.removeIf(filter);
         }
      }

      public void replaceAll(UnaryOperator<K> operator) {
         synchronized(this.sync) {
            this.list.replaceAll(operator);
         }
      }

      public boolean addAll(int index, Collection<? extends K> c) {
         synchronized(this.sync) {
            return this.list.addAll(index, c);
         }
      }

      public void getElements(int from, Object[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.getElements(from, a, offset, length);
         }
      }

      public void removeElements(int from, int to) {
         synchronized(this.sync) {
            this.list.removeElements(from, to);
         }
      }

      public void addElements(int index, K[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.addElements(index, a, offset, length);
         }
      }

      public void addElements(int index, K[] a) {
         synchronized(this.sync) {
            this.list.addElements(index, a);
         }
      }

      public void setElements(K[] a) {
         synchronized(this.sync) {
            this.list.setElements(a);
         }
      }

      public void setElements(int index, K[] a) {
         synchronized(this.sync) {
            this.list.setElements(index, a);
         }
      }

      public void setElements(int index, K[] a, int offset, int length) {
         synchronized(this.sync) {
            this.list.setElements(index, a, offset, length);
         }
      }

      public void size(int size) {
         synchronized(this.sync) {
            this.list.size(size);
         }
      }

      public ObjectListIterator<K> listIterator() {
         return this.list.listIterator();
      }

      public ObjectListIterator<K> iterator() {
         return this.listIterator();
      }

      public ObjectListIterator<K> listIterator(int i) {
         return this.list.listIterator(i);
      }

      public ObjectList<K> subList(int from, int to) {
         synchronized(this.sync) {
            return new ObjectLists.SynchronizedList(this.list.subList(from, to), this.sync);
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

      public int compareTo(List<? extends K> o) {
         synchronized(this.sync) {
            return this.list.compareTo(o);
         }
      }

      public void sort(Comparator<? super K> comparator) {
         synchronized(this.sync) {
            this.list.sort(comparator);
         }
      }

      public void unstableSort(Comparator<? super K> comparator) {
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

   public static class UnmodifiableRandomAccessList<K> extends ObjectLists.UnmodifiableList<K> implements Serializable, RandomAccess {
      private static final long serialVersionUID = 0L;

      protected UnmodifiableRandomAccessList(ObjectList<? extends K> l) {
         super(l);
      }

      public ObjectList<K> subList(int from, int to) {
         return new ObjectLists.UnmodifiableRandomAccessList(this.list.subList(from, to));
      }
   }

   public static class UnmodifiableList<K> extends ObjectCollections.UnmodifiableCollection<K> implements Serializable, ObjectList<K> {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final ObjectList<? extends K> list;

      protected UnmodifiableList(ObjectList<? extends K> l) {
         super(l);
         this.list = l;
      }

      public K get(int i) {
         return this.list.get(i);
      }

      public K set(int i, K k) {
         throw new UnsupportedOperationException();
      }

      public void add(int i, K k) {
         throw new UnsupportedOperationException();
      }

      public K remove(int i) {
         throw new UnsupportedOperationException();
      }

      public int indexOf(Object k) {
         return this.list.indexOf(k);
      }

      public int lastIndexOf(Object k) {
         return this.list.lastIndexOf(k);
      }

      public boolean addAll(int index, Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      public void replaceAll(UnaryOperator<K> operator) {
         throw new UnsupportedOperationException();
      }

      public void getElements(int from, Object[] a, int offset, int length) {
         this.list.getElements(from, a, offset, length);
      }

      public void removeElements(int from, int to) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void addElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a) {
         throw new UnsupportedOperationException();
      }

      public void setElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      public void size(int size) {
         this.list.size(size);
      }

      public ObjectListIterator<K> listIterator() {
         return ObjectIterators.unmodifiable(this.list.listIterator());
      }

      public ObjectListIterator<K> iterator() {
         return this.listIterator();
      }

      public ObjectListIterator<K> listIterator(int i) {
         return ObjectIterators.unmodifiable(this.list.listIterator(i));
      }

      public ObjectList<K> subList(int from, int to) {
         return new ObjectLists.UnmodifiableList(this.list.subList(from, to));
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      @SuppressWarnings("unchecked")
      public int compareTo(List<? extends K> o) {
         return this.list.compareTo((List) o);
      }

      public void sort(Comparator<? super K> comparator) {
         throw new UnsupportedOperationException();
      }

      public void unstableSort(Comparator<? super K> comparator) {
         throw new UnsupportedOperationException();
      }
   }

   abstract static class ImmutableListBase<K> extends AbstractObjectList<K> implements ObjectList<K> {
      /** @deprecated */
      @Deprecated
      public final void add(int index, K k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean add(K k) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean addAll(int index, Collection<? extends K> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final K remove(int index) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final boolean remove(Object k) {
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
      public final boolean removeIf(Predicate<? super K> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void replaceAll(UnaryOperator<K> operator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final K set(int index, K k) {
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
      public final void addElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void setElements(int index, K[] a, int offset, int length) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void sort(Comparator<? super K> comparator) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public final void unstableSort(Comparator<? super K> comparator) {
         throw new UnsupportedOperationException();
      }
   }
}
