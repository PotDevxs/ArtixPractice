package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import dev.artixdev.libs.it.unimi.dsi.fastutil.HashCommon;

public abstract class AbstractLongList extends AbstractLongCollection implements LongList, LongStack {
   protected AbstractLongList() {
   }

   protected void ensureIndex(int index) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
      } else if (index > this.size()) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
      }
   }

   protected void ensureRestrictedIndex(int index) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
      } else if (index >= this.size()) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size() + ")");
      }
   }

   public void add(int index, long k) {
      throw new UnsupportedOperationException();
   }

   public boolean add(long k) {
      this.add(this.size(), k);
      return true;
   }

   public long removeLong(int i) {
      throw new UnsupportedOperationException();
   }

   public long set(int index, long k) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(int index, Collection<? extends Long> c) {
      if (c instanceof LongCollection) {
         return this.addAll(index, (LongCollection)c);
      } else {
         this.ensureIndex(index);
         Iterator<? extends Long> i = c.iterator();
         boolean retVal = i.hasNext();

         while(i.hasNext()) {
            this.add(index++, (Long)i.next());
         }

         return retVal;
      }
   }

   public boolean addAll(Collection<? extends Long> c) {
      return this.addAll(this.size(), c);
   }

   public LongListIterator iterator() {
      return this.listIterator();
   }

   public LongListIterator listIterator() {
      return this.listIterator(0);
   }

   public LongListIterator listIterator(int index) {
      this.ensureIndex(index);
      return new LongIterators.AbstractIndexBasedListIterator(0, index) {
         protected final long get(int i) {
            return AbstractLongList.this.getLong(i);
         }

         protected final void add(int i, long k) {
            AbstractLongList.this.add(i, k);
         }

         protected final void set(int i, long k) {
            AbstractLongList.this.set(i, k);
         }

         protected final void remove(int i) {
            AbstractLongList.this.removeLong(i);
         }

         protected final int getMaxPos() {
            return AbstractLongList.this.size();
         }
      };
   }

   public boolean contains(long k) {
      return this.indexOf(k) >= 0;
   }

   public int indexOf(long k) {
      LongListIterator i = this.listIterator();

      long e;
      do {
         if (!i.hasNext()) {
            return -1;
         }

         e = i.nextLong();
      } while(k != e);

      return i.previousIndex();
   }

   public int lastIndexOf(long k) {
      LongListIterator i = this.listIterator(this.size());

      long e;
      do {
         if (!i.hasPrevious()) {
            return -1;
         }

         e = i.previousLong();
      } while(k != e);

      return i.nextIndex();
   }

   public void size(int size) {
      int i = this.size();
      if (size > i) {
         while(i++ < size) {
            this.add(0L);
         }
      } else {
         while(i-- != size) {
            this.removeLong(i);
         }
      }

   }

   public LongList subList(int from, int to) {
      this.ensureIndex(from);
      this.ensureIndex(to);
      if (from > to) {
         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
      } else {
         return (LongList)(this instanceof RandomAccess ? new AbstractLongList.LongRandomAccessSubList(this, from, to) : new AbstractLongList.LongSubList(this, from, to));
      }
   }

   public void forEach(java.util.function.LongConsumer action) {
      if (this instanceof RandomAccess) {
         int i = 0;

         for(int max = this.size(); i < max; ++i) {
            action.accept(this.getLong(i));
         }
      } else {
         LongList.super.forEach(action);
      }

   }

   public void removeElements(int from, int to) {
      this.ensureIndex(to);
      LongListIterator i = this.listIterator(from);
      int n = to - from;
      if (n < 0) {
         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
      } else {
         while(n-- != 0) {
            i.nextLong();
            i.remove();
         }

      }
   }

   public void addElements(int index, long[] a, int offset, int length) {
      this.ensureIndex(index);
      LongArrays.ensureOffsetLength(a, offset, length);
      if (this instanceof RandomAccess) {
         while(length-- != 0) {
            this.add(index++, a[offset++]);
         }
      } else {
         LongListIterator iter = this.listIterator(index);

         while(length-- != 0) {
            iter.add(a[offset++]);
         }
      }

   }

   public void addElements(int index, long[] a) {
      this.addElements(index, a, 0, a.length);
   }

   public void getElements(int from, long[] a, int offset, int length) {
      this.ensureIndex(from);
      LongArrays.ensureOffsetLength(a, offset, length);
      if (from + length > this.size()) {
         throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + this.size() + ")");
      } else {
         if (this instanceof RandomAccess) {
            for(int var5 = from; length-- != 0; a[offset++] = this.getLong(var5++)) {
            }
         } else {
            for(LongListIterator i = this.listIterator(from); length-- != 0; a[offset++] = i.nextLong()) {
            }
         }

      }
   }

   public void setElements(int index, long[] a, int offset, int length) {
      this.ensureIndex(index);
      LongArrays.ensureOffsetLength(a, offset, length);
      if (index + length > this.size()) {
         throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
      } else {
         if (this instanceof RandomAccess) {
            for(int i = 0; i < length; ++i) {
               this.set(i + index, a[i + offset]);
            }
         } else {
            LongListIterator iter = this.listIterator(index);
            int i = 0;

            while(i < length) {
               iter.nextLong();
               iter.set(a[offset + i++]);
            }
         }

      }
   }

   public void clear() {
      this.removeElements(0, this.size());
   }

   public int hashCode() {
      LongIterator i = this.iterator();
      int h = 1;

      long k;
      for(int var3 = this.size(); var3-- != 0; h = 31 * h + HashCommon.long2int(k)) {
         k = i.nextLong();
      }

      return h;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof List)) {
         return false;
      } else {
         List<?> l = (List)o;
         int s = this.size();
         if (s != l.size()) {
            return false;
         } else {
            LongListIterator i1;
            if (l instanceof LongList) {
               i1 = this.listIterator();
               LongListIterator i2 = ((LongList)l).listIterator();

               do {
                  if (s-- == 0) {
                     return true;
                  }
               } while(i1.nextLong() == i2.nextLong());

               return false;
            } else {
               i1 = this.listIterator();
               ListIterator i2 = l.listIterator();

               do {
                  if (s-- == 0) {
                     return true;
                  }
               } while(Objects.equals(i1.next(), i2.next()));

               return false;
            }
         }
      }
   }

   public int compareTo(List<? extends Long> l) {
      if (l == this) {
         return 0;
      } else {
         LongListIterator i1;
         int r;
         if (l instanceof LongList) {
            i1 = this.listIterator();
            LongListIterator i2 = ((LongList)l).listIterator();

            while(i1.hasNext() && i2.hasNext()) {
               long e1 = i1.nextLong();
               long e2 = i2.nextLong();
               if ((r = Long.compare(e1, e2)) != 0) {
                  return r;
               }
            }

            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
         } else {
            i1 = this.listIterator();
            ListIterator i2 = l.listIterator();

            while(i1.hasNext() && i2.hasNext()) {
               if ((r = ((Comparable)i1.next()).compareTo(i2.next())) != 0) {
                  return r;
               }
            }

            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
         }
      }
   }

   public void push(long o) {
      this.add(o);
   }

   public long popLong() {
      if (this.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return this.removeLong(this.size() - 1);
      }
   }

   public long topLong() {
      if (this.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return this.getLong(this.size() - 1);
      }
   }

   public long peekLong(int i) {
      return this.getLong(this.size() - 1 - i);
   }

   public boolean rem(long k) {
      int index = this.indexOf(k);
      if (index == -1) {
         return false;
      } else {
         this.removeLong(index);
         return true;
      }
   }

   public long[] toLongArray() {
      int size = this.size();
      if (size == 0) {
         return LongArrays.EMPTY_ARRAY;
      } else {
         long[] ret = new long[size];
         this.getElements(0, ret, 0, size);
         return ret;
      }
   }

   public long[] toArray(long[] a) {
      int size = this.size();
      if (a.length < size) {
         a = Arrays.copyOf(a, size);
      }

      this.getElements(0, a, 0, size);
      return a;
   }

   public boolean addAll(int index, LongCollection c) {
      this.ensureIndex(index);
      LongIterator i = c.iterator();
      boolean retVal = i.hasNext();

      while(i.hasNext()) {
         this.add(index++, i.nextLong());
      }

      return retVal;
   }

   public boolean addAll(LongCollection c) {
      return this.addAll(this.size(), c);
   }

   public final void replaceAll(LongUnaryOperator operator) {
      this.replaceAll(operator);
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      LongIterator i = this.iterator();
      int n = this.size();
      boolean first = true;
      s.append("[");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         long k = i.nextLong();
         s.append(String.valueOf(k));
      }

      s.append("]");
      return s.toString();
   }

   public static class LongRandomAccessSubList extends AbstractLongList.LongSubList implements RandomAccess {
      private static final long serialVersionUID = -107070782945191929L;

      public LongRandomAccessSubList(LongList l, int from, int to) {
         super(l, from, to);
      }

      public LongList subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return new AbstractLongList.LongRandomAccessSubList(this, from, to);
         }
      }
   }

   public static class LongSubList extends AbstractLongList implements Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final LongList l;
      protected final int from;
      protected int to;

      public LongSubList(LongList l, int from, int to) {
         this.l = l;
         this.from = from;
         this.to = to;
      }

      private boolean assertRange() {
         assert this.from <= this.l.size();

         assert this.to <= this.l.size();

         assert this.to >= this.from;

         return true;
      }

      public boolean add(long k) {
         this.l.add(this.to, k);
         ++this.to;

         assert this.assertRange();

         return true;
      }

      public void add(int index, long k) {
         this.ensureIndex(index);
         this.l.add(this.from + index, k);
         ++this.to;

         assert this.assertRange();

      }

      public boolean addAll(int index, Collection<? extends Long> c) {
         this.ensureIndex(index);
         this.to += c.size();
         return this.l.addAll(this.from + index, (Collection)c);
      }

      public long getLong(int index) {
         this.ensureRestrictedIndex(index);
         return this.l.getLong(this.from + index);
      }

      public long removeLong(int index) {
         this.ensureRestrictedIndex(index);
         --this.to;
         return this.l.removeLong(this.from + index);
      }

      public long set(int index, long k) {
         this.ensureRestrictedIndex(index);
         return this.l.set(this.from + index, k);
      }

      public int size() {
         return this.to - this.from;
      }

      public void getElements(int from, long[] a, int offset, int length) {
         this.ensureIndex(from);
         if (from + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + this.size() + ")");
         } else {
            this.l.getElements(this.from + from, a, offset, length);
         }
      }

      public void removeElements(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         this.l.removeElements(this.from + from, this.from + to);
         this.to -= to - from;

         assert this.assertRange();

      }

      public void addElements(int index, long[] a, int offset, int length) {
         this.ensureIndex(index);
         this.l.addElements(this.from + index, a, offset, length);
         this.to += length;

         assert this.assertRange();

      }

      public void setElements(int index, long[] a, int offset, int length) {
         this.ensureIndex(index);
         this.l.setElements(this.from + index, a, offset, length);

         assert this.assertRange();

      }

      public LongListIterator listIterator(int index) {
         this.ensureIndex(index);
         return (LongListIterator)(this.l instanceof RandomAccess ? new AbstractLongList.LongSubList.RandomAccessIter(index) : new AbstractLongList.LongSubList.ParentWrappingIter(this.l.listIterator(index + this.from)));
      }

      public LongSpliterator spliterator() {
         return (LongSpliterator)(this.l instanceof RandomAccess ? new AbstractLongList.IndexBasedSpliterator(this.l, this.from, this.to) : super.spliterator());
      }

      public LongList subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return new AbstractLongList.LongSubList(this, from, to);
         }
      }

      public boolean rem(long k) {
         int index = this.indexOf(k);
         if (index == -1) {
            return false;
         } else {
            --this.to;
            this.l.removeLong(this.from + index);

            assert this.assertRange();

            return true;
         }
      }

      public boolean addAll(int index, LongCollection c) {
         this.ensureIndex(index);
         return super.addAll(index, c);
      }

      public boolean addAll(int index, LongList l) {
         this.ensureIndex(index);
         return super.addAll(index, (LongList)l);
      }

      private final class RandomAccessIter extends LongIterators.AbstractIndexBasedListIterator {
         RandomAccessIter(int pos) {
            super(0, pos);
         }

         protected final long get(int i) {
            return LongSubList.this.l.getLong(LongSubList.this.from + i);
         }

         protected final void add(int i, long k) {
            LongSubList.this.add(i, k);
         }

         protected final void set(int i, long k) {
            LongSubList.this.set(i, k);
         }

         protected final void remove(int i) {
            LongSubList.this.removeLong(i);
         }

         protected final int getMaxPos() {
            return LongSubList.this.to - LongSubList.this.from;
         }

         public void add(long k) {
            super.add(k);

            assert LongSubList.this.assertRange();

         }

         public void remove() {
            super.remove();

            assert LongSubList.this.assertRange();

         }
      }

      private class ParentWrappingIter implements LongListIterator {
         private LongListIterator parent;

         ParentWrappingIter(LongListIterator parent) {
            this.parent = parent;
         }

         public int nextIndex() {
            return this.parent.nextIndex() - LongSubList.this.from;
         }

         public int previousIndex() {
            return this.parent.previousIndex() - LongSubList.this.from;
         }

         public boolean hasNext() {
            return this.parent.nextIndex() < LongSubList.this.to;
         }

         public boolean hasPrevious() {
            return this.parent.previousIndex() >= LongSubList.this.from;
         }

         public long nextLong() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return this.parent.nextLong();
            }
         }

         public long previousLong() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException();
            } else {
               return this.parent.previousLong();
            }
         }

         public void add(long k) {
            this.parent.add(k);
         }

         public void set(long k) {
            this.parent.set(k);
         }

         public void remove() {
            this.parent.remove();
         }

         public int back(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int currentPos = this.parent.previousIndex();
               int parentNewPos = currentPos - n;
               if (parentNewPos < LongSubList.this.from - 1) {
                  parentNewPos = LongSubList.this.from - 1;
               }

               int toSkip = parentNewPos - currentPos;
               return this.parent.back(toSkip);
            }
         }

         public int skip(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int currentPos = this.parent.nextIndex();
               int parentNewPos = currentPos + n;
               if (parentNewPos > LongSubList.this.to) {
                  parentNewPos = LongSubList.this.to;
               }

               int toSkip = parentNewPos - currentPos;
               return this.parent.skip(toSkip);
            }
         }
      }
   }

   static final class IndexBasedSpliterator extends LongSpliterators.LateBindingSizeIndexBasedSpliterator {
      final LongList l;

      IndexBasedSpliterator(LongList l, int pos) {
         super(pos);
         this.l = l;
      }

      IndexBasedSpliterator(LongList l, int pos, int maxPos) {
         super(pos, maxPos);
         this.l = l;
      }

      protected final int getMaxPosFromBackingStore() {
         return this.l.size();
      }

      protected final long get(int i) {
         return this.l.getLong(i);
      }

      protected final AbstractLongList.IndexBasedSpliterator makeForSplit(int pos, int maxPos) {
         return new AbstractLongList.IndexBasedSpliterator(this.l, pos, maxPos);
      }
   }
}
