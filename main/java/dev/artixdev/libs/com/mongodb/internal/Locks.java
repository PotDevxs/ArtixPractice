package dev.artixdev.libs.com.mongodb.internal;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;

public final class Locks {
   public static void withLock(Lock lock, Runnable action) {
      withLock(lock, () -> {
         action.run();
         return null;
      });
   }

   public static <V> V withLock(Lock lock, Supplier<V> supplier) {
      Objects.requireNonNull(supplier);
      return checkedWithLock(lock, supplier::get);
   }

   public static <V, E extends Exception> V checkedWithLock(Lock lock, CheckedSupplier<V, E> supplier) throws E {
      lock.lock();

      V var2;
      try {
         var2 = supplier.get();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public static void withInterruptibleLock(Lock lock, Runnable action) throws MongoInterruptedException {
      withInterruptibleLock(lock, () -> {
         action.run();
         return null;
      });
   }

   public static <V> V withInterruptibleLock(Lock lock, Supplier<V> supplier) throws MongoInterruptedException {
      Objects.requireNonNull(supplier);
      return checkedWithInterruptibleLock(lock, supplier::get);
   }

   public static <V, E extends Exception> V checkedWithInterruptibleLock(Lock lock, CheckedSupplier<V, E> supplier) throws MongoInterruptedException, E {
      lockInterruptibly(lock);

      V var2;
      try {
         var2 = supplier.get();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   public static void lockInterruptibly(Lock lock) throws MongoInterruptedException {
      try {
         lock.lockInterruptibly();
      } catch (InterruptedException e) {
         throw InterruptionUtil.interruptAndCreateMongoInterruptedException("Interrupted waiting for lock", e);
      }
   }

   public static void withUnfairLock(ReentrantLock lock, Runnable action) {
      withUnfairLock(lock, () -> {
         action.run();
         return null;
      });
   }

   public static <V> V withUnfairLock(ReentrantLock lock, Supplier<V> supplier) {
      lockUnfair(lock);

      V var2;
      try {
         var2 = supplier.get();
      } finally {
         lock.unlock();
      }

      return var2;
   }

   private static void lockUnfair(ReentrantLock lock) {
      if (!lock.tryLock()) {
         lock.lock();
      }

   }

   public static void lockInterruptiblyUnfair(ReentrantLock lock) throws MongoInterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw InterruptionUtil.interruptAndCreateMongoInterruptedException((String)null, (InterruptedException)null);
      } else {
         if (!lock.tryLock()) {
            lockInterruptibly(lock);
         }

      }
   }

   private Locks() {
   }
}
