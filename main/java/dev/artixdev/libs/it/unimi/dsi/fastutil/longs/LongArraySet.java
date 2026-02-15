package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;

public class LongArraySet extends AbstractLongSet implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   protected transient long[] a;
   protected int size;

   public LongArraySet(long[] a) {
      this.a = a;
      this.size = a.length;
   }

   public LongArraySet() {
      this.a = LongArrays.EMPTY_ARRAY;
   }

   public LongArraySet(int capacity) {
      this.a = new long[capacity];
   }

   public LongArraySet(LongCollection c) {
      this(c.size());
      this.addAll(c);
   }

   public LongArraySet(Collection<? extends Long> c) {
      this(c.size());
      this.addAll(c);
   }

   public LongArraySet(LongSet c) {
      this(c.size());
      int i = 0;

      for(LongIterator var3 = c.iterator(); var3.hasNext(); ++i) {
         long x = (Long)var3.next();
         this.a[i] = x;
      }

      this.size = i;
   }

   public LongArraySet(Set<? extends Long> c) {
      this(c.size());
      int i = 0;

      for(Iterator var3 = c.iterator(); var3.hasNext(); ++i) {
         Long x = (Long)var3.next();
         this.a[i] = x;
      }

      this.size = i;
   }

   public LongArraySet(long[] a, int size) {
      this.a = a;
      this.size = size;
      if (size > a.length) {
         throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
      }
   }

   public static LongArraySet of() {
      return ofUnchecked();
   }

   public static LongArraySet of(long e) {
      return ofUnchecked(e);
   }

   public static LongArraySet of(long... a) {
      if (a.length == 2) {
         if (a[0] == a[1]) {
            throw new IllegalArgumentException("Duplicate element: " + a[1]);
         }
      } else if (a.length > 2) {
         LongOpenHashSet.of(a);
      }

      return ofUnchecked(a);
   }

   public static LongArraySet ofUnchecked() {
      return new LongArraySet();
   }

   public static LongArraySet ofUnchecked(long... a) {
      return new LongArraySet(a);
   }

   private int findKey(long o) {
      int i = this.size;

      do {
         if (i-- == 0) {
            return -1;
         }
      } while(this.a[i] != o);

      return i;
   }

   public LongIterator iterator() {
      return new LongIterator() {
         int next = 0;

         public boolean hasNext() {
            return this.next < LongArraySet.this.size;
         }

         public long nextLong() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return LongArraySet.this.a[this.next++];
            }
         }

         public void remove() {
            int tail = LongArraySet.this.size-- - this.next--;
            System.arraycopy(LongArraySet.this.a, this.next + 1, LongArraySet.this.a, this.next, tail);
         }

         public int skip(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int remaining = LongArraySet.this.size - this.next;
               if (n < remaining) {
                  this.next += n;
                  return n;
               } else {
                  this.next = LongArraySet.this.size;
                  return remaining;
               }
            }
         }
      };
   }

   public LongSpliterator spliterator() {
      return new LongArraySet.Spliterator();
   }

   public boolean contains(long k) {
      return this.findKey(k) != -1;
   }

   public int size() {
      return this.size;
   }

   public boolean remove(long k) {
      int pos = this.findKey(k);
      if (pos == -1) {
         return false;
      } else {
         int tail = this.size - pos - 1;

         for(int i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
         }

         --this.size;
         return true;
      }
   }

   public boolean add(long k) {
      int pos = this.findKey(k);
      if (pos != -1) {
         return false;
      } else {
         if (this.size == this.a.length) {
            long[] b = new long[this.size == 0 ? 2 : this.size * 2];

            for(int i = this.size; i-- != 0; b[i] = this.a[i]) {
            }

            this.a = b;
         }

         this.a[this.size++] = k;
         return true;
      }
   }

   public void clear() {
      this.size = 0;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public long[] toLongArray() {
      return this.size == 0 ? LongArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, this.size);
   }

   public long[] toArray(long[] a) {
      if (a == null || a.length < this.size) {
         a = new long[this.size];
      }

      System.arraycopy(this.a, 0, a, 0, this.size);
      return a;
   }

   public LongArraySet clone() {
      LongArraySet c;
      try {
         c = (LongArraySet)super.clone();
      } catch (CloneNotSupportedException e) {
         throw new InternalError(e);
      }

      c.a = (long[])this.a.clone();
      return c;
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for(int i = 0; i < this.size; ++i) {
         s.writeLong(this.a[i]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.a = new long[this.size];

      for(int i = 0; i < this.size; ++i) {
         this.a[i] = s.readLong();
      }

   }

   private final class Spliterator implements LongSpliterator {
      boolean hasSplit;
      int pos;
      int max;

      public Spliterator() {
         this(0, LongArraySet.this.size, false);
      }

      private Spliterator(int pos, int max, boolean hasSplit) {
         this.hasSplit = false;

         assert pos <= max : "pos " + pos + " must be <= max " + max;

         this.pos = pos;
         this.max = max;
         this.hasSplit = hasSplit;
      }

      private int getWorkingMax() {
         return this.hasSplit ? this.max : LongArraySet.this.size;
      }

      public int characteristics() {
         return 16721;
      }

      public long estimateSize() {
         return (long)(this.getWorkingMax() - this.pos);
      }

      public boolean tryAdvance(java.util.function.LongConsumer action) {
         if (this.pos >= this.getWorkingMax()) {
            return false;
         } else {
            action.accept(LongArraySet.this.a[this.pos++]);
            return true;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         for(int max = this.getWorkingMax(); this.pos < max; ++this.pos) {
            action.accept(LongArraySet.this.a[this.pos]);
         }

      }

      public long skip(long n) {
         if (n < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int max = this.getWorkingMax();
            if (this.pos >= max) {
               return 0L;
            } else {
               int remaining = max - this.pos;
               if (n < (long)remaining) {
                  this.pos = SafeMath.safeLongToInt((long)this.pos + n);
                  return n;
               } else {
                  n = (long)remaining;
                  this.pos = max;
                  return n;
               }
            }
         }
      }

      public LongSpliterator trySplit() {
         int max = this.getWorkingMax();
         int retLen = max - this.pos >> 1;
         if (retLen <= 1) {
            return null;
         } else {
            this.max = max;
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return LongArraySet.this.new Spliterator(oldPos, myNewPos, true);
         }
      }
   }
}
