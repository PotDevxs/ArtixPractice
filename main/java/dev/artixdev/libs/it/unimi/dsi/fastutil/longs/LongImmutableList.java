package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.stream.LongStream;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

public class LongImmutableList extends LongLists.ImmutableListBase implements Serializable, Cloneable, RandomAccess, LongList {
   private static final long serialVersionUID = 0L;
   static final LongImmutableList EMPTY;
   private final long[] a;

   public LongImmutableList(long[] a) {
      this.a = a;
   }

   public LongImmutableList(Collection<? extends Long> c) {
      this(c.isEmpty() ? LongArrays.EMPTY_ARRAY : LongIterators.unwrap(LongIterators.asLongIterator(c.iterator())));
   }

   public LongImmutableList(LongCollection c) {
      this(c.isEmpty() ? LongArrays.EMPTY_ARRAY : LongIterators.unwrap(c.iterator()));
   }

   public LongImmutableList(LongList l) {
      this(l.isEmpty() ? LongArrays.EMPTY_ARRAY : new long[l.size()]);
      l.getElements(0, this.a, 0, l.size());
   }

   public LongImmutableList(long[] a, int offset, int length) {
      this(length == 0 ? LongArrays.EMPTY_ARRAY : new long[length]);
      System.arraycopy(a, offset, this.a, 0, length);
   }

   public LongImmutableList(LongIterator i) {
      this(i.hasNext() ? LongIterators.unwrap(i) : LongArrays.EMPTY_ARRAY);
   }

   public static LongImmutableList of() {
      return EMPTY;
   }

   public static LongImmutableList of(long... init) {
      return init.length == 0 ? of() : new LongImmutableList(init);
   }

   private static LongImmutableList convertTrustedToImmutableList(LongArrayList arrayList) {
      if (arrayList.isEmpty()) {
         return of();
      } else {
         long[] backingArray = arrayList.elements();
         if (arrayList.size() != backingArray.length) {
            backingArray = Arrays.copyOf(backingArray, arrayList.size());
         }

         return new LongImmutableList(backingArray);
      }
   }

   public static LongImmutableList toList(LongStream stream) {
      return convertTrustedToImmutableList(LongArrayList.toList(stream));
   }

   public static LongImmutableList toListWithExpectedSize(LongStream stream, int expectedSize) {
      return convertTrustedToImmutableList(LongArrayList.toListWithExpectedSize(stream, expectedSize));
   }

   public long getLong(int index) {
      if (index >= this.a.length) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.a.length + ")");
      } else {
         return this.a[index];
      }
   }

   public int indexOf(long k) {
      int i = 0;

      for(int size = this.a.length; i < size; ++i) {
         if (k == this.a[i]) {
            return i;
         }
      }

      return -1;
   }

   public int lastIndexOf(long k) {
      int i = this.a.length;

      do {
         if (i-- == 0) {
            return -1;
         }
      } while(k != this.a[i]);

      return i;
   }

   public int size() {
      return this.a.length;
   }

   public boolean isEmpty() {
      return this.a.length == 0;
   }

   public void getElements(int from, long[] a, int offset, int length) {
      LongArrays.ensureOffsetLength(a, offset, length);
      System.arraycopy(this.a, from, a, offset, length);
   }

   public void forEach(java.util.function.LongConsumer action) {
      for(int i = 0; i < this.a.length; ++i) {
         action.accept(this.a[i]);
      }

   }

   public long[] toLongArray() {
      return this.a.length == 0 ? LongArrays.EMPTY_ARRAY : (long[])this.a.clone();
   }

   public long[] toArray(long[] a) {
      if (a == null || a.length < this.size()) {
         a = new long[this.a.length];
      }

      System.arraycopy(this.a, 0, a, 0, a.length);
      return a;
   }

   public LongListIterator listIterator(final int index) {
      this.ensureIndex(index);
      return new LongListIterator() {
         int pos = index;

         public boolean hasNext() {
            return this.pos < LongImmutableList.this.a.length;
         }

         public boolean hasPrevious() {
            return this.pos > 0;
         }

         public long nextLong() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return LongImmutableList.this.a[this.pos++];
            }
         }

         public long previousLong() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException();
            } else {
               return LongImmutableList.this.a[--this.pos];
            }
         }

         public int nextIndex() {
            return this.pos;
         }

         public int previousIndex() {
            return this.pos - 1;
         }

         public void forEachRemaining(java.util.function.LongConsumer action) {
            while(this.pos < LongImmutableList.this.a.length) {
               action.accept(LongImmutableList.this.a[this.pos++]);
            }

         }

         public void add(long k) {
            throw new UnsupportedOperationException();
         }

         public void set(long k) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         public int back(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int remaining = LongImmutableList.this.a.length - this.pos;
               if (n < remaining) {
                  this.pos -= n;
               } else {
                  n = remaining;
                  this.pos = 0;
               }

               return n;
            }
         }

         public int skip(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int remaining = LongImmutableList.this.a.length - this.pos;
               if (n < remaining) {
                  this.pos += n;
               } else {
                  n = remaining;
                  this.pos = LongImmutableList.this.a.length;
               }

               return n;
            }
         }
      };
   }

   public LongSpliterator spliterator() {
      return new LongImmutableList.Spliterator();
   }

   public LongList subList(int from, int to) {
      if (from == 0 && to == this.size()) {
         return this;
      } else {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from == to) {
            return EMPTY;
         } else if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return new LongImmutableList.ImmutableSubList(this, from, to);
         }
      }
   }

   public LongImmutableList clone() {
      return this;
   }

   public boolean equals(LongImmutableList l) {
      if (l == this) {
         return true;
      } else if (this.a == l.a) {
         return true;
      } else {
         int s = this.size();
         if (s != l.size()) {
            return false;
         } else {
            long[] a1 = this.a;
            long[] a2 = l.a;
            return Arrays.equals(a1, a2);
         }
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (o == null) {
         return false;
      } else if (!(o instanceof List)) {
         return false;
      } else if (o instanceof LongImmutableList) {
         return this.equals((LongImmutableList)o);
      } else {
         return o instanceof LongImmutableList.ImmutableSubList ? ((LongImmutableList.ImmutableSubList)o).equals(this) : super.equals(o);
      }
   }

   public int compareTo(LongImmutableList l) {
      if (this.a == l.a) {
         return 0;
      } else {
         int s1 = this.size();
         int s2 = l.size();
         long[] a1 = this.a;
         long[] a2 = l.a;

         int i;
         for(i = 0; i < s1 && i < s2; ++i) {
            long e1 = a1[i];
            long e2 = a2[i];
            int r;
            if ((r = Long.compare(e1, e2)) != 0) {
               return r;
            }
         }

         return i < s2 ? -1 : (i < s1 ? 1 : 0);
      }
   }

   public int compareTo(List<? extends Long> l) {
      if (l instanceof LongImmutableList) {
         return this.compareTo((LongImmutableList)l);
      } else if (l instanceof LongImmutableList.ImmutableSubList) {
         LongImmutableList.ImmutableSubList other = (LongImmutableList.ImmutableSubList)l;
         return -other.compareTo((List)this);
      } else {
         return super.compareTo(l);
      }
   }

   static {
      EMPTY = new LongImmutableList(LongArrays.EMPTY_ARRAY);
   }

   private final class Spliterator implements LongSpliterator {
      int pos;
      int max;

      public Spliterator() {
         this(0, LongImmutableList.this.a.length);
      }

      private Spliterator(int pos, int max) {
         assert pos <= max : "pos " + pos + " must be <= max " + max;

         this.pos = pos;
         this.max = max;
      }

      public int characteristics() {
         return 17744;
      }

      public long estimateSize() {
         return (long)(this.max - this.pos);
      }

      public boolean tryAdvance(java.util.function.LongConsumer action) {
         if (this.pos >= this.max) {
            return false;
         } else {
            action.accept(LongImmutableList.this.a[this.pos++]);
            return true;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         while(this.pos < this.max) {
            action.accept(LongImmutableList.this.a[this.pos]);
            ++this.pos;
         }

      }

      public long skip(long n) {
         if (n < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (this.pos >= this.max) {
            return 0L;
         } else {
            int remaining = this.max - this.pos;
            if (n < (long)remaining) {
               this.pos = SafeMath.safeLongToInt((long)this.pos + n);
               return n;
            } else {
               n = (long)remaining;
               this.pos = this.max;
               return n;
            }
         }
      }

      public LongSpliterator trySplit() {
         int retLen = this.max - this.pos >> 1;
         if (retLen <= 1) {
            return null;
         } else {
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            return LongImmutableList.this.new Spliterator(oldPos, myNewPos);
         }
      }
   }

   private static final class ImmutableSubList extends LongLists.ImmutableListBase implements Serializable, RandomAccess {
      private static final long serialVersionUID = 7054639518438982401L;
      final LongImmutableList innerList;
      final int from;
      final int to;
      final transient long[] a;

      ImmutableSubList(LongImmutableList innerList, int from, int to) {
         this.innerList = innerList;
         this.from = from;
         this.to = to;
         this.a = innerList.a;
      }

      public long getLong(int index) {
         this.ensureRestrictedIndex(index);
         return this.a[index + this.from];
      }

      public int indexOf(long k) {
         for(int i = this.from; i < this.to; ++i) {
            if (k == this.a[i]) {
               return i - this.from;
            }
         }

         return -1;
      }

      public int lastIndexOf(long k) {
         int i = this.to;

         do {
            if (i-- == this.from) {
               return -1;
            }
         } while(k != this.a[i]);

         return i - this.from;
      }

      public int size() {
         return this.to - this.from;
      }

      public boolean isEmpty() {
         return this.to <= this.from;
      }

      public void getElements(int fromSublistIndex, long[] a, int offset, int length) {
         LongArrays.ensureOffsetLength(a, offset, length);
         this.ensureRestrictedIndex(fromSublistIndex);
         if (this.from + length > this.to) {
            throw new IndexOutOfBoundsException("Final index " + (this.from + length) + " (startingIndex: " + this.from + " + length: " + length + ") is greater then list length " + this.size());
         } else {
            System.arraycopy(this.a, fromSublistIndex + this.from, a, offset, length);
         }
      }

      public void forEach(java.util.function.LongConsumer action) {
         for(int i = this.from; i < this.to; ++i) {
            action.accept(this.a[i]);
         }

      }

      public long[] toLongArray() {
         return Arrays.copyOfRange(this.a, this.from, this.to);
      }

      public long[] toArray(long[] a) {
         if (a == null || a.length < this.size()) {
            a = new long[this.size()];
         }

         System.arraycopy(this.a, this.from, a, 0, this.size());
         return a;
      }

      public LongListIterator listIterator(final int index) {
         this.ensureIndex(index);
         return new LongListIterator() {
            int pos = index;

            public boolean hasNext() {
               return this.pos < ImmutableSubList.this.to;
            }

            public boolean hasPrevious() {
               return this.pos > ImmutableSubList.this.from;
            }

            public long nextLong() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  return ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from];
               }
            }

            public long previousLong() {
               if (!this.hasPrevious()) {
                  throw new NoSuchElementException();
               } else {
                  return ImmutableSubList.this.a[--this.pos + ImmutableSubList.this.from];
               }
            }

            public int nextIndex() {
               return this.pos;
            }

            public int previousIndex() {
               return this.pos - 1;
            }

            public void forEachRemaining(java.util.function.LongConsumer action) {
               while(this.pos < ImmutableSubList.this.to) {
                  action.accept(ImmutableSubList.this.a[this.pos++ + ImmutableSubList.this.from]);
               }

            }

            public void add(long k) {
               throw new UnsupportedOperationException();
            }

            public void set(long k) {
               throw new UnsupportedOperationException();
            }

            public void remove() {
               throw new UnsupportedOperationException();
            }

            public int back(int n) {
               if (n < 0) {
                  throw new IllegalArgumentException("Argument must be nonnegative: " + n);
               } else {
                  int remaining = ImmutableSubList.this.to - this.pos;
                  if (n < remaining) {
                     this.pos -= n;
                  } else {
                     n = remaining;
                     this.pos = 0;
                  }

                  return n;
               }
            }

            public int skip(int n) {
               if (n < 0) {
                  throw new IllegalArgumentException("Argument must be nonnegative: " + n);
               } else {
                  int remaining = ImmutableSubList.this.to - this.pos;
                  if (n < remaining) {
                     this.pos += n;
                  } else {
                     n = remaining;
                     this.pos = ImmutableSubList.this.to;
                  }

                  return n;
               }
            }
         };
      }

      public LongSpliterator spliterator() {
         return new LongImmutableList.ImmutableSubList.SubListSpliterator();
      }

      boolean contentsEquals(long[] otherA, int otherAFrom, int otherATo) {
         if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
            return true;
         } else if (otherATo - otherAFrom != this.size()) {
            return false;
         } else {
            int pos = this.from;
            int var5 = otherAFrom;

            do {
               if (pos >= this.to) {
                  return true;
               }
            } while(this.a[pos++] == otherA[var5++]);

            return false;
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o == null) {
            return false;
         } else if (!(o instanceof List)) {
            return false;
         } else if (o instanceof LongImmutableList) {
            LongImmutableList other = (LongImmutableList)o;
            return this.contentsEquals(other.a, 0, other.size());
         } else if (o instanceof LongImmutableList.ImmutableSubList) {
            LongImmutableList.ImmutableSubList other = (LongImmutableList.ImmutableSubList)o;
            return this.contentsEquals(other.a, other.from, other.to);
         } else {
            return super.equals(o);
         }
      }

      int contentsCompareTo(long[] otherA, int otherAFrom, int otherATo) {
         if (this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
            return 0;
         } else {
            int i = this.from;

            for(int j = otherAFrom; i < this.to && i < otherATo; ++j) {
               long e1 = this.a[i];
               long e2 = otherA[j];
               int r;
               if ((r = Long.compare(e1, e2)) != 0) {
                  return r;
               }

               ++i;
            }

            return i < otherATo ? -1 : (i < this.to ? 1 : 0);
         }
      }

      public int compareTo(List<? extends Long> l) {
         if (l instanceof LongImmutableList) {
            LongImmutableList other = (LongImmutableList)l;
            return this.contentsCompareTo(other.a, 0, other.size());
         } else if (l instanceof LongImmutableList.ImmutableSubList) {
            LongImmutableList.ImmutableSubList other = (LongImmutableList.ImmutableSubList)l;
            return this.contentsCompareTo(other.a, other.from, other.to);
         } else {
            return super.compareTo(l);
         }
      }

      private Object readResolve() throws ObjectStreamException {
         try {
            return this.innerList.subList(this.from, this.to);
         } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
         }
      }

      public LongList subList(int from, int to) {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from == to) {
            return LongImmutableList.EMPTY;
         } else if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return new LongImmutableList.ImmutableSubList(this.innerList, from + this.from, to + this.from);
         }
      }

      private final class SubListSpliterator extends LongSpliterators.EarlyBindingSizeIndexBasedSpliterator {
         SubListSpliterator() {
            super(ImmutableSubList.this.from, ImmutableSubList.this.to);
         }

         private SubListSpliterator(int pos, int maxPos) {
            super(pos, maxPos);
         }

         protected final long get(int i) {
            return ImmutableSubList.this.a[i];
         }

         protected final LongImmutableList.ImmutableSubList.SubListSpliterator makeForSplit(int pos, int maxPos) {
            return ImmutableSubList.this.new SubListSpliterator(pos, maxPos);
         }

         public boolean tryAdvance(java.util.function.LongConsumer action) {
            if (this.pos >= this.maxPos) {
               return false;
            } else {
               action.accept(ImmutableSubList.this.a[this.pos++]);
               return true;
            }
         }

         public void forEachRemaining(java.util.function.LongConsumer action) {
            int max = this.maxPos;

            while(this.pos < max) {
               action.accept(ImmutableSubList.this.a[this.pos++]);
            }

         }

         public int characteristics() {
            return 17744;
         }
      }
   }
}
