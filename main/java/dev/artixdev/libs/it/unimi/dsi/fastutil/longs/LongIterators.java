package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.Consumer;
import dev.artixdev.libs.it.unimi.dsi.fastutil.BigArrays;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.ByteIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.CharIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.IntIterator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.ShortIterator;

public final class LongIterators {
   public static final LongIterators.EmptyIterator EMPTY_ITERATOR = new LongIterators.EmptyIterator();

   private LongIterators() {
   }

   public static LongListIterator singleton(long element) {
      return new LongIterators.SingletonIterator(element);
   }

   public static LongListIterator wrap(long[] array, int offset, int length) {
      LongArrays.ensureOffsetLength(array, offset, length);
      return new LongIterators.ArrayIterator(array, offset, length);
   }

   public static LongListIterator wrap(long[] array) {
      return new LongIterators.ArrayIterator(array, 0, array.length);
   }

   public static int unwrap(LongIterator i, long[] array, int offset, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0 && offset + max <= array.length) {
         int j;
         for(j = max; j-- != 0 && i.hasNext(); array[offset++] = i.nextLong()) {
         }

         return max - j - 1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int unwrap(LongIterator i, long[] array) {
      return unwrap(i, array, 0, array.length);
   }

   public static long[] unwrap(LongIterator i, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         long[] array = new long[16];

         int j;
         for(j = 0; max-- != 0 && i.hasNext(); array[j++] = i.nextLong()) {
            if (j == array.length) {
               array = LongArrays.grow(array, j + 1);
            }
         }

         return LongArrays.trim(array, j);
      }
   }

   public static long[] unwrap(LongIterator i) {
      return unwrap(i, Integer.MAX_VALUE);
   }

   public static long unwrap(LongIterator i, long[][] array, long offset, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0L && offset + max <= BigArrays.length(array)) {
         long j = max;

         while(j-- != 0L && i.hasNext()) {
            BigArrays.set(array, offset++, i.nextLong());
         }

         return max - j - 1L;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static long unwrap(LongIterator i, long[][] array) {
      return unwrap(i, array, 0L, BigArrays.length(array));
   }

   public static int unwrap(LongIterator i, LongCollection c, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            c.add(i.nextLong());
         }

         return max - j - 1;
      }
   }

   public static long[][] unwrapBig(LongIterator i, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         long[][] array = LongBigArrays.newBigArray(16L);

         long j;
         for(j = 0L; max-- != 0L && i.hasNext(); BigArrays.set(array, j++, i.nextLong())) {
            if (j == BigArrays.length(array)) {
               array = BigArrays.grow(array, j + 1L);
            }
         }

         return BigArrays.trim(array, j);
      }
   }

   public static long[][] unwrapBig(LongIterator i) {
      return unwrapBig(i, Long.MAX_VALUE);
   }

   public static long unwrap(LongIterator i, LongCollection c) {
      long n;
      for(n = 0L; i.hasNext(); ++n) {
         c.add(i.nextLong());
      }

      return n;
   }

   public static int pour(LongIterator i, LongCollection s, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            s.add(i.nextLong());
         }

         return max - j - 1;
      }
   }

   public static int pour(LongIterator i, LongCollection s) {
      return pour(i, s, Integer.MAX_VALUE);
   }

   public static LongList pour(LongIterator i, int max) {
      LongArrayList l = new LongArrayList();
      pour(i, l, max);
      l.trim();
      return l;
   }

   public static LongList pour(LongIterator i) {
      return pour(i, Integer.MAX_VALUE);
   }

   public static LongIterator asLongIterator(Iterator i) {
      if (i instanceof LongIterator) {
         return (LongIterator)i;
      } else {
         return (LongIterator)(i instanceof OfLong ? new LongIterators.PrimitiveIteratorWrapper((OfLong)i) : new LongIterators.IteratorWrapper(i));
      }
   }

   public static LongListIterator asLongIterator(ListIterator i) {
      return (LongListIterator)(i instanceof LongListIterator ? (LongListIterator)i : new LongIterators.ListIteratorWrapper(i));
   }

   public static boolean any(LongIterator iterator, java.util.function.LongPredicate predicate) {
      return indexOf(iterator, predicate) != -1;
   }

   public static boolean all(LongIterator iterator, java.util.function.LongPredicate predicate) {
      Objects.requireNonNull(predicate);

      while(iterator.hasNext()) {
         if (!predicate.test(iterator.nextLong())) {
            return false;
         }
      }

      return true;
   }

   public static int indexOf(LongIterator iterator, java.util.function.LongPredicate predicate) {
      Objects.requireNonNull(predicate);

      for(int i = 0; iterator.hasNext(); ++i) {
         if (predicate.test(iterator.nextLong())) {
            return i;
         }
      }

      return -1;
   }

   public static LongBidirectionalIterator fromTo(long from, long to) {
      return new LongIterators.IntervalIterator(from, to);
   }

   public static LongIterator concat(LongIterator... a) {
      return concat(a, 0, a.length);
   }

   public static LongIterator concat(LongIterator[] a, int offset, int length) {
      return new LongIterators.IteratorConcatenator(a, offset, length);
   }

   public static LongIterator unmodifiable(LongIterator i) {
      return new LongIterators.UnmodifiableIterator(i);
   }

   public static LongBidirectionalIterator unmodifiable(LongBidirectionalIterator i) {
      return new LongIterators.UnmodifiableBidirectionalIterator(i);
   }

   public static LongListIterator unmodifiable(LongListIterator i) {
      return new LongIterators.UnmodifiableListIterator(i);
   }

   public static LongIterator wrap(ByteIterator iterator) {
      return new LongIterators.ByteIteratorWrapper(iterator);
   }

   public static LongIterator wrap(ShortIterator iterator) {
      return new LongIterators.ShortIteratorWrapper(iterator);
   }

   public static LongIterator wrap(CharIterator iterator) {
      return new LongIterators.CharIteratorWrapper(iterator);
   }

   public static LongIterator wrap(IntIterator iterator) {
      return new LongIterators.IntIteratorWrapper(iterator);
   }

   private static class SingletonIterator implements LongListIterator {
      private final long element;
      private byte curr;

      public SingletonIterator(long element) {
         this.element = element;
      }

      public boolean hasNext() {
         return this.curr == 0;
      }

      public boolean hasPrevious() {
         return this.curr == 1;
      }

      public long nextLong() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 1;
            return this.element;
         }
      }

      public long previousLong() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 0;
            return this.element;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         if (this.curr == 0) {
            action.accept(this.element);
            this.curr = 1;
         }

      }

      public int nextIndex() {
         return this.curr;
      }

      public int previousIndex() {
         return this.curr - 1;
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n != 0 && this.curr >= 1) {
            this.curr = 1;
            return 1;
         } else {
            return 0;
         }
      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n != 0 && this.curr <= 0) {
            this.curr = 0;
            return 1;
         } else {
            return 0;
         }
      }
   }

   private static class ArrayIterator implements LongListIterator {
      private final long[] array;
      private final int offset;
      private final int length;
      private int curr;

      public ArrayIterator(long[] array, int offset, int length) {
         this.array = array;
         this.offset = offset;
         this.length = length;
      }

      public boolean hasNext() {
         return this.curr < this.length;
      }

      public boolean hasPrevious() {
         return this.curr > 0;
      }

      public long nextLong() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + this.curr++];
         }
      }

      public long previousLong() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + --this.curr];
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);

         while(this.curr < this.length) {
            action.accept(this.array[this.offset + this.curr]);
            ++this.curr;
         }

      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n <= this.length - this.curr) {
            this.curr += n;
            return n;
         } else {
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
         }
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n <= this.curr) {
            this.curr -= n;
            return n;
         } else {
            n = this.curr;
            this.curr = 0;
            return n;
         }
      }

      public int nextIndex() {
         return this.curr;
      }

      public int previousIndex() {
         return this.curr - 1;
      }
   }

   private static class PrimitiveIteratorWrapper implements LongIterator {
      final OfLong i;

      public PrimitiveIteratorWrapper(OfLong i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public void remove() {
         this.i.remove();
      }

      public long nextLong() {
         return this.i.nextLong();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class IteratorWrapper implements LongIterator {
      final Iterator<Long> i;

      public IteratorWrapper(Iterator<Long> i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public void remove() {
         this.i.remove();
      }

      public long nextLong() {
         return (Long)this.i.next();
      }

      public void forEachRemaining(LongConsumer action) {
         this.i.forEachRemaining(action);
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         Iterator var10000 = this.i;
         Consumer var10001;
         if (action instanceof Consumer) {
            var10001 = (Consumer)action;
         } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
         }

         var10000.forEachRemaining(var10001);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class ListIteratorWrapper implements LongListIterator {
      final ListIterator<Long> i;

      public ListIteratorWrapper(ListIterator<Long> i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public int nextIndex() {
         return this.i.nextIndex();
      }

      public int previousIndex() {
         return this.i.previousIndex();
      }

      public void set(long k) {
         this.i.set(k);
      }

      public void add(long k) {
         this.i.add(k);
      }

      public void remove() {
         this.i.remove();
      }

      public long nextLong() {
         return (Long)this.i.next();
      }

      public long previousLong() {
         return (Long)this.i.previous();
      }

      public void forEachRemaining(LongConsumer action) {
         this.i.forEachRemaining(action);
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         ListIterator var10000 = this.i;
         Consumer var10001;
         if (action instanceof Consumer) {
            var10001 = (Consumer)action;
         } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
         }

         var10000.forEachRemaining(var10001);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class IntervalIterator implements LongBidirectionalIterator {
      private final long from;
      private final long to;
      long curr;

      public IntervalIterator(long from, long to) {
         this.from = this.curr = from;
         this.to = to;
      }

      public boolean hasNext() {
         return this.curr < this.to;
      }

      public boolean hasPrevious() {
         return this.curr > this.from;
      }

      public long nextLong() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return (long)(this.curr++);
         }
      }

      public long previousLong() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return --this.curr;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);

         while(this.curr < this.to) {
            action.accept(this.curr);
            ++this.curr;
         }

      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (this.curr + (long)n <= this.to) {
            this.curr += (long)n;
            return n;
         } else {
            n = (int)(this.to - this.curr);
            this.curr = this.to;
            return n;
         }
      }

      public int back(int n) {
         if (this.curr - (long)n >= this.from) {
            this.curr -= (long)n;
            return n;
         } else {
            n = (int)(this.curr - this.from);
            this.curr = this.from;
            return n;
         }
      }
   }

   private static class IteratorConcatenator implements LongIterator {
      final LongIterator[] a;
      int offset;
      int length;
      int lastOffset = -1;

      public IteratorConcatenator(LongIterator[] a, int offset, int length) {
         this.a = a;
         this.offset = offset;
         this.length = length;
         this.advance();
      }

      private void advance() {
         while(this.length != 0 && !this.a[this.offset].hasNext()) {
            --this.length;
            ++this.offset;
         }

      }

      public boolean hasNext() {
         return this.length > 0;
      }

      public long nextLong() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            long next = this.a[this.lastOffset = this.offset].nextLong();
            this.advance();
            return next;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         while(this.length > 0) {
            this.a[this.lastOffset = this.offset].forEachRemaining((java.util.function.LongConsumer)action);
            this.advance();
         }

      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         while(this.length > 0) {
            this.a[this.lastOffset = this.offset].forEachRemaining(action);
            this.advance();
         }

      }

      public void remove() {
         if (this.lastOffset == -1) {
            throw new IllegalStateException();
         } else {
            this.a[this.lastOffset].remove();
         }
      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            this.lastOffset = -1;

            int skipped;
            for(skipped = 0; skipped < n && this.length != 0; ++this.offset) {
               skipped += this.a[this.offset].skip(n - skipped);
               if (this.a[this.offset].hasNext()) {
                  break;
               }

               --this.length;
            }

            return skipped;
         }
      }
   }

   public static class UnmodifiableIterator implements LongIterator {
      protected final LongIterator i;

      public UnmodifiableIterator(LongIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public long nextLong() {
         return this.i.nextLong();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         this.i.forEachRemaining((java.util.function.LongConsumer)action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableBidirectionalIterator implements LongBidirectionalIterator {
      protected final LongBidirectionalIterator i;

      public UnmodifiableBidirectionalIterator(LongBidirectionalIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public long nextLong() {
         return this.i.nextLong();
      }

      public long previousLong() {
         return this.i.previousLong();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableListIterator implements LongListIterator {
      protected final LongListIterator i;

      public UnmodifiableListIterator(LongListIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public long nextLong() {
         return this.i.nextLong();
      }

      public long previousLong() {
         return this.i.previousLong();
      }

      public int nextIndex() {
         return this.i.nextIndex();
      }

      public int previousIndex() {
         return this.i.previousIndex();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static final class ByteIteratorWrapper implements LongIterator {
      final ByteIterator iterator;

      public ByteIteratorWrapper(ByteIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Long next() {
         return (long)this.iterator.nextByte();
      }

      public long nextLong() {
         return (long)this.iterator.nextByte();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         ByteIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   private static final class ShortIteratorWrapper implements LongIterator {
      final ShortIterator iterator;

      public ShortIteratorWrapper(ShortIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Long next() {
         return (long)this.iterator.nextShort();
      }

      public long nextLong() {
         return (long)this.iterator.nextShort();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         ShortIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   private static final class CharIteratorWrapper implements LongIterator {
      final CharIterator iterator;

      public CharIteratorWrapper(CharIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Long next() {
         return (long)this.iterator.nextChar();
      }

      public long nextLong() {
         return (long)this.iterator.nextChar();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         CharIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   private static final class IntIteratorWrapper implements LongIterator {
      final IntIterator iterator;

      public IntIteratorWrapper(IntIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Long next() {
         return (long)this.iterator.nextInt();
      }

      public long nextLong() {
         return (long)this.iterator.nextInt();
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         Objects.requireNonNull(action);
         IntIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   public static class EmptyIterator implements Serializable, Cloneable, LongListIterator {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyIterator() {
      }

      public boolean hasNext() {
         return false;
      }

      public boolean hasPrevious() {
         return false;
      }

      public long nextLong() {
         throw new NoSuchElementException();
      }

      public long previousLong() {
         throw new NoSuchElementException();
      }

      public int nextIndex() {
         return 0;
      }

      public int previousIndex() {
         return -1;
      }

      public int skip(int n) {
         return 0;
      }

      public int back(int n) {
         return 0;
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Long> action) {
      }

      public Object clone() {
         return LongIterators.EMPTY_ITERATOR;
      }

      private Object readResolve() {
         return LongIterators.EMPTY_ITERATOR;
      }
   }

   public abstract static class AbstractIndexBasedListIterator extends LongIterators.AbstractIndexBasedIterator implements LongListIterator {
      protected AbstractIndexBasedListIterator(int minPos, int initialPos) {
         super(minPos, initialPos);
      }

      protected abstract void add(int var1, long var2);

      protected abstract void set(int var1, long var2);

      public boolean hasPrevious() {
         return this.pos > this.minPos;
      }

      public long previousLong() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.get(this.lastReturned = --this.pos);
         }
      }

      public int nextIndex() {
         return this.pos;
      }

      public int previousIndex() {
         return this.pos - 1;
      }

      public void add(long k) {
         this.add(this.pos++, k);
         this.lastReturned = -1;
      }

      public void set(long k) {
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            this.set(this.lastReturned, k);
         }
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int remaining = this.pos - this.minPos;
            if (n < remaining) {
               this.pos -= n;
            } else {
               n = remaining;
               this.pos = this.minPos;
            }

            this.lastReturned = this.pos;
            return n;
         }
      }
   }

   public abstract static class AbstractIndexBasedIterator extends AbstractLongIterator {
      protected final int minPos;
      protected int pos;
      protected int lastReturned;

      protected AbstractIndexBasedIterator(int minPos, int initialPos) {
         this.minPos = minPos;
         this.pos = initialPos;
      }

      protected abstract long get(int var1);

      protected abstract void remove(int var1);

      protected abstract int getMaxPos();

      public boolean hasNext() {
         return this.pos < this.getMaxPos();
      }

      public long nextLong() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.get(this.lastReturned = this.pos++);
         }
      }

      public void remove() {
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            this.remove(this.lastReturned);
            if (this.lastReturned < this.pos) {
               --this.pos;
            }

            this.lastReturned = -1;
         }
      }

      public void forEachRemaining(java.util.function.LongConsumer action) {
         while(this.pos < this.getMaxPos()) {
            action.accept(this.get(this.lastReturned = this.pos++));
         }

      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int max = this.getMaxPos();
            int remaining = max - this.pos;
            if (n < remaining) {
               this.pos += n;
            } else {
               n = remaining;
               this.pos = max;
            }

            this.lastReturned = this.pos - 1;
            return n;
         }
      }
   }
}
