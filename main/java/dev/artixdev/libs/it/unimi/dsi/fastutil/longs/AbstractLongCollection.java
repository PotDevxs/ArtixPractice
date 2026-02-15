package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection {
   protected AbstractLongCollection() {
   }

   public abstract LongIterator iterator();

   public boolean add(long k) {
      throw new UnsupportedOperationException();
   }

   public boolean contains(long k) {
      LongIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextLong());

      return true;
   }

   public boolean rem(long k) {
      LongIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextLong());

      iterator.remove();
      return true;
   }

   /** @deprecated */
   @Deprecated
   public boolean add(Long key) {
      return LongCollection.super.add(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean contains(Object key) {
      return LongCollection.super.contains(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean remove(Object key) {
      return LongCollection.super.remove(key);
   }

   public long[] toArray(long[] a) {
      int size = this.size();
      if (a == null) {
         a = new long[size];
      } else if (a.length < size) {
         a = Arrays.copyOf(a, size);
      }

      LongIterators.unwrap(this.iterator(), a);
      return a;
   }

   public long[] toLongArray() {
      int size = this.size();
      if (size == 0) {
         return LongArrays.EMPTY_ARRAY;
      } else {
         long[] a = new long[size];
         LongIterators.unwrap(this.iterator(), a);
         return a;
      }
   }

   /** @deprecated */
   @Deprecated
   public long[] toLongArray(long[] a) {
      return this.toArray(a);
   }

   public final void forEach(LongConsumer action) {
      LongCollection.super.forEach(action);
   }

   public final boolean removeIf(LongPredicate filter) {
      return LongCollection.super.removeIf(filter);
   }

   public boolean addAll(LongCollection c) {
      boolean retVal = false;
      LongIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.add(i.nextLong())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean addAll(Collection<? extends Long> c) {
      return c instanceof LongCollection ? this.addAll((LongCollection)c) : super.addAll(c);
   }

   public boolean containsAll(LongCollection c) {
      LongIterator i = c.iterator();

      do {
         if (!i.hasNext()) {
            return true;
         }
      } while(this.contains(i.nextLong()));

      return false;
   }

   public boolean containsAll(Collection<?> c) {
      return c instanceof LongCollection ? this.containsAll((LongCollection)c) : super.containsAll(c);
   }

   public boolean removeAll(LongCollection c) {
      boolean retVal = false;
      LongIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.rem(i.nextLong())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean removeAll(Collection<?> c) {
      return c instanceof LongCollection ? this.removeAll((LongCollection)c) : super.removeAll(c);
   }

   public boolean retainAll(LongCollection c) {
      boolean retVal = false;
      LongIterator i = this.iterator();

      while(i.hasNext()) {
         if (!c.contains(i.nextLong())) {
            i.remove();
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean retainAll(Collection<?> c) {
      return c instanceof LongCollection ? this.retainAll((LongCollection)c) : super.retainAll(c);
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      LongIterator i = this.iterator();
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         long k = i.nextLong();
         s.append(String.valueOf(k));
      }

      s.append("}");
      return s.toString();
   }
}
