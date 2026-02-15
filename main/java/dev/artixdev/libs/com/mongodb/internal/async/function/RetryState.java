package dev.artixdev.libs.com.mongodb.internal.async.function;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public final class RetryState {
   public static final int RETRIES = 1;
   private static final int INFINITE_ATTEMPTS = Integer.MAX_VALUE;
   private final LoopState loopState;
   private final int attempts;
   @Nullable
   private Throwable exception;

   public RetryState(int retries) {
      Assertions.assertTrue(retries >= 0);
      this.loopState = new LoopState();
      this.attempts = retries == Integer.MAX_VALUE ? Integer.MAX_VALUE : retries + 1;
   }

   public RetryState() {
      this(Integer.MAX_VALUE);
   }

   void advanceOrThrow(RuntimeException attemptException, BiFunction<Throwable, Throwable, Throwable> exceptionTransformer, BiPredicate<RetryState, Throwable> retryPredicate) throws RuntimeException {
      try {
         this.doAdvanceOrThrow(attemptException, exceptionTransformer, retryPredicate, true);
      } catch (Error | RuntimeException rethrow) {
         throw rethrow;
      } catch (Throwable throwable) {
         throw new AssertionError(throwable);
      }
   }

   void advanceOrThrow(Throwable attemptException, BiFunction<Throwable, Throwable, Throwable> exceptionTransformer, BiPredicate<RetryState, Throwable> retryPredicate) throws Throwable {
      this.doAdvanceOrThrow(attemptException, exceptionTransformer, retryPredicate, false);
   }

   private void doAdvanceOrThrow(Throwable attemptException, BiFunction<Throwable, Throwable, Throwable> exceptionTransformer, BiPredicate<RetryState, Throwable> retryPredicate, boolean onlyRuntimeExceptions) throws Throwable {
      Assertions.assertTrue(this.attempt() < this.attempts);
      Assertions.assertNotNull(attemptException);
      if (onlyRuntimeExceptions) {
         Assertions.assertTrue(isRuntime(attemptException));
      }

      Assertions.assertTrue(!this.isFirstAttempt() || this.exception == null);
      Throwable newlyChosenException = transformException(this.exception, attemptException, onlyRuntimeExceptions, exceptionTransformer);
      if (this.isLastAttempt()) {
         this.exception = newlyChosenException;
         throw this.exception;
      } else {
         boolean retry = this.shouldRetry(this, attemptException, newlyChosenException, onlyRuntimeExceptions, retryPredicate);
         this.exception = newlyChosenException;
         if (retry) {
            Assertions.assertTrue(this.loopState.advance());
         } else {
            throw this.exception;
         }
      }
   }

   private static Throwable transformException(@Nullable Throwable previouslyChosenException, Throwable attemptException, boolean onlyRuntimeExceptions, BiFunction<Throwable, Throwable, Throwable> exceptionTransformer) {
      if (onlyRuntimeExceptions && previouslyChosenException != null) {
         Assertions.assertTrue(isRuntime(previouslyChosenException));
      }

      try {
         Throwable result = (Throwable)Assertions.assertNotNull((Throwable)exceptionTransformer.apply(previouslyChosenException, attemptException));
         if (onlyRuntimeExceptions) {
            Assertions.assertTrue(isRuntime(result));
         }

         return result;
      } catch (Throwable throwable) {
         if (onlyRuntimeExceptions && !isRuntime(throwable)) {
            throw throwable;
         } else {
            if (previouslyChosenException != null) {
               throwable.addSuppressed(previouslyChosenException);
            }

            throwable.addSuppressed(attemptException);
            throw throwable;
         }
      }
   }

   private boolean shouldRetry(RetryState readOnlyRetryState, Throwable attemptException, Throwable newlyChosenException, boolean onlyRuntimeExceptions, BiPredicate<RetryState, Throwable> retryPredicate) {
      try {
         return retryPredicate.test(readOnlyRetryState, attemptException);
      } catch (Throwable throwable) {
         if (onlyRuntimeExceptions && !isRuntime(throwable)) {
            throw throwable;
         } else {
            throwable.addSuppressed(newlyChosenException);
            throw throwable;
         }
      }
   }

   private static boolean isRuntime(@Nullable Throwable exception) {
      return exception instanceof RuntimeException;
   }

   public void breakAndThrowIfRetryAnd(Supplier<Boolean> predicate) throws RuntimeException {
      Assertions.assertFalse(this.loopState.isLastIteration());
      if (!this.isFirstAttempt()) {
         Assertions.assertNotNull(this.exception);
         Assertions.assertTrue(this.exception instanceof RuntimeException);
         RuntimeException localException = (RuntimeException)this.exception;

         try {
            if ((Boolean)predicate.get()) {
               this.loopState.markAsLastIteration();
            }
         } catch (Exception exception) {
            exception.addSuppressed(localException);
            throw exception;
         }

         if (this.loopState.isLastIteration()) {
            throw localException;
         }
      }

   }

   public boolean breakAndCompleteIfRetryAnd(Supplier<Boolean> predicate, SingleResultCallback<?> callback) {
      try {
         this.breakAndThrowIfRetryAnd(predicate);
         return false;
      } catch (Throwable throwable) {
         callback.onResult(null, throwable);
         return true;
      }
   }

   public void markAsLastAttempt() {
      this.loopState.markAsLastIteration();
   }

   public boolean isFirstAttempt() {
      return this.loopState.isFirstIteration();
   }

   public boolean isLastAttempt() {
      return this.attempt() == this.attempts - 1 || this.loopState.isLastIteration();
   }

   public int attempt() {
      return this.loopState.iteration();
   }

   public int attempts() {
      return this.attempts == Integer.MAX_VALUE ? 0 : this.attempts;
   }

   public Optional<Throwable> exception() {
      Assertions.assertTrue(this.exception == null || !this.isFirstAttempt());
      return Optional.ofNullable(this.exception);
   }

   public <V> RetryState attach(LoopState.AttachmentKey<V> key, V value, boolean autoRemove) {
      this.loopState.attach(key, value, autoRemove);
      return this;
   }

   public <V> Optional<V> attachment(LoopState.AttachmentKey<V> key) {
      return this.loopState.attachment(key);
   }

   public String toString() {
      return "RetryState{loopState=" + this.loopState + ", attempts=" + (this.attempts == Integer.MAX_VALUE ? "infinite" : this.attempts) + ", exception=" + this.exception + '}';
   }
}
