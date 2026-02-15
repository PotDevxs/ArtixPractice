package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoServerUnavailableException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class ConcurrentPool<T> implements Pool<T> {
   public static final int INFINITE_SIZE = Integer.MAX_VALUE;
   private final int maxSize;
   private final ConcurrentPool.ItemFactory<T> itemFactory;
   private final Deque<T> available;
   private final ConcurrentPool.StateAndPermits stateAndPermits;
   private final String poolClosedMessage;

   public ConcurrentPool(int maxSize, ConcurrentPool.ItemFactory<T> itemFactory) {
      this(maxSize, itemFactory, "The pool is closed");
   }

   public ConcurrentPool(int maxSize, ConcurrentPool.ItemFactory<T> itemFactory, String poolClosedMessage) {
      this.available = new ConcurrentLinkedDeque();
      Assertions.assertTrue(maxSize > 0);
      this.maxSize = maxSize;
      this.itemFactory = itemFactory;
      this.stateAndPermits = new ConcurrentPool.StateAndPermits(maxSize, this::poolClosedException);
      this.poolClosedMessage = (String)Assertions.notNull("poolClosedMessage", poolClosedMessage);
   }

   public void release(T t) {
      this.release(t, false);
   }

   public void release(T t, boolean prune) {
      if (t == null) {
         throw new IllegalArgumentException("Can not return a null item to the pool");
      } else if (this.stateAndPermits.closed()) {
         this.close(t);
      } else {
         if (prune) {
            this.close(t);
         } else {
            this.available.addLast(t);
         }

         this.stateAndPermits.releasePermit();
      }
   }

   public T get() {
      return this.get(-1L, TimeUnit.MILLISECONDS);
   }

   public T get(long timeout, TimeUnit timeUnit) {
      if (!this.stateAndPermits.acquirePermit(timeout, timeUnit)) {
         throw new MongoTimeoutException(String.format("Timeout waiting for a pooled item after %d %s", timeout, timeUnit));
      } else {
         T t = this.available.pollLast();
         if (t == null) {
            t = this.createNewAndReleasePermitIfFailure();
         }

         return t;
      }
   }

   @Nullable
   T getImmediateUnfair() {
      T element = null;
      if (this.stateAndPermits.acquirePermitImmediateUnfair()) {
         element = this.available.pollLast();
         if (element == null) {
            this.stateAndPermits.releasePermit();
         }
      }

      return element;
   }

   public void prune() {
      int maxIterations = this.available.size();
      int numIterations = 0;
      Iterator<T> var3 = this.available.iterator();

      while(var3.hasNext()) {
         T cur = var3.next();
         if (this.itemFactory.shouldPrune(cur) && this.available.remove(cur)) {
            this.close(cur);
         }

         ++numIterations;
         if (numIterations == maxIterations) {
            break;
         }
      }

   }

   public void ensureMinSize(int minSize, Consumer<T> initAndRelease) {
      while(this.getCount() < minSize && this.stateAndPermits.acquirePermit(0L, TimeUnit.MILLISECONDS)) {
         initAndRelease.accept(this.createNewAndReleasePermitIfFailure());
      }

   }

   private T createNewAndReleasePermitIfFailure() {
      try {
         T newMember = this.itemFactory.create();
         if (newMember == null) {
            throw new MongoInternalException("The factory for the pool created a null item");
         } else {
            return newMember;
         }
      } catch (Exception exception) {
         this.stateAndPermits.releasePermit();
         throw exception;
      }
   }

   boolean acquirePermit(long timeout, TimeUnit timeUnit) {
      return this.stateAndPermits.acquirePermit(timeout, timeUnit);
   }

   public void close() {
      if (this.stateAndPermits.close()) {
         Iterator<T> iter = this.available.iterator();

         while(iter.hasNext()) {
            T t = iter.next();
            this.close(t);
            iter.remove();
         }
      }

   }

   int getMaxSize() {
      return this.maxSize;
   }

   public int getInUseCount() {
      return this.maxSize - this.stateAndPermits.permits();
   }

   public int getAvailableCount() {
      return this.available.size();
   }

   public int getCount() {
      return this.getInUseCount() + this.getAvailableCount();
   }

   public String toString() {
      return "pool:  maxSize: " + sizeToString(this.maxSize) + " availableCount " + this.getAvailableCount() + " inUseCount " + this.getInUseCount();
   }

   private void close(T t) {
      try {
         this.itemFactory.close(t);
      } catch (Exception ignored) {
      }

   }

   void ready() {
      this.stateAndPermits.ready();
   }

   void pause(Supplier<MongoException> causeSupplier) {
      this.stateAndPermits.pause(causeSupplier);
   }

   MongoServerUnavailableException poolClosedException() {
      return new MongoServerUnavailableException(this.poolClosedMessage);
   }

   static boolean isPoolClosedException(Throwable e) {
      return e instanceof MongoServerUnavailableException;
   }

   static String sizeToString(int size) {
      return size == Integer.MAX_VALUE ? "infinite" : Integer.toString(size);
   }

   public interface ItemFactory<T> {
      T create();

      void close(T var1);

      boolean shouldPrune(T var1);
   }

   @ThreadSafe
   private static final class StateAndPermits {
      private final Supplier<MongoServerUnavailableException> poolClosedExceptionSupplier;
      private final ReentrantLock lock;
      private final Condition permitAvailableOrClosedOrPausedCondition;
      private volatile boolean paused;
      private volatile boolean closed;
      private final int maxPermits;
      private volatile int permits;
      private final AtomicInteger waitersEstimate;
      @Nullable
      private Supplier<MongoException> causeSupplier;

      StateAndPermits(int maxPermits, Supplier<MongoServerUnavailableException> poolClosedExceptionSupplier) {
         this.poolClosedExceptionSupplier = poolClosedExceptionSupplier;
         this.lock = new ReentrantLock(true);
         this.permitAvailableOrClosedOrPausedCondition = this.lock.newCondition();
         this.paused = false;
         this.closed = false;
         this.maxPermits = maxPermits;
         this.permits = maxPermits;
         this.waitersEstimate = new AtomicInteger();
         this.causeSupplier = null;
      }

      int permits() {
         return this.permits;
      }

      boolean acquirePermitImmediateUnfair() {
         return (Boolean)Locks.withUnfairLock(this.lock, () -> {
            this.throwIfClosedOrPaused();
            if (this.permits > 0) {
               --this.permits;
               return true;
            } else {
               return false;
            }
         });
      }

      boolean acquirePermit(long timeout, TimeUnit unit) throws MongoInterruptedException {
         long remainingNanos = unit.toNanos(timeout);
         if (this.waitersEstimate.get() == 0) {
            Locks.lockInterruptiblyUnfair(this.lock);
         } else {
            Locks.lockInterruptibly(this.lock);
         }

         boolean var18;
         try {
            while(this.permits == 0 & !this.throwIfClosedOrPaused()) {
               try {
                  try {
                     this.waitersEstimate.incrementAndGet();
                     if (timeout >= 0L && remainingNanos != Long.MAX_VALUE) {
                        if (remainingNanos < 0L) {
                           return false;
                        }

                        remainingNanos = this.permitAvailableOrClosedOrPausedCondition.awaitNanos(remainingNanos);
                     } else {
                        this.permitAvailableOrClosedOrPausedCondition.await();
                     }
                  } catch (InterruptedException e) {
                     throw InterruptionUtil.interruptAndCreateMongoInterruptedException((String)null, e);
                  }
               } finally {
                  this.waitersEstimate.decrementAndGet();
               }
            }

            Assertions.assertTrue(this.permits > 0);
            --this.permits;
            var18 = true;
         } finally {
            this.lock.unlock();
         }

         return var18;
      }

      void releasePermit() {
         Locks.withUnfairLock(this.lock, () -> {
            Assertions.assertTrue(this.permits < this.maxPermits);
            ++this.permits;
            this.permitAvailableOrClosedOrPausedCondition.signal();
         });
      }

      void pause(Supplier<MongoException> causeSupplier) {
         Locks.withUnfairLock(this.lock, () -> {
            if (!this.paused) {
               this.paused = true;
               this.permitAvailableOrClosedOrPausedCondition.signalAll();
            }

            this.causeSupplier = (Supplier)Assertions.assertNotNull(causeSupplier);
         });
      }

      void ready() {
         if (this.paused) {
            Locks.withUnfairLock(this.lock, () -> {
               this.paused = false;
               this.causeSupplier = null;
            });
         }

      }

      boolean close() {
         return !this.closed ? (Boolean)Locks.withUnfairLock(this.lock, () -> {
            if (!this.closed) {
               this.closed = true;
               this.permitAvailableOrClosedOrPausedCondition.signalAll();
               return true;
            } else {
               return false;
            }
         }) : false;
      }

      boolean throwIfClosedOrPaused() {
         if (this.closed) {
            throw (MongoServerUnavailableException)this.poolClosedExceptionSupplier.get();
         } else if (this.paused) {
            throw (MongoException)Assertions.assertNotNull((MongoException)((Supplier)Assertions.assertNotNull(this.causeSupplier)).get());
         } else {
            return false;
         }
      }

      boolean closed() {
         return this.closed;
      }
   }
}
