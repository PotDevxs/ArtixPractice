package dev.artixdev.libs.com.mongodb.internal.async.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;

@NotThreadSafe
public final class RetryingSyncSupplier<R> implements Supplier<R> {
   private final RetryState state;
   private final BiPredicate<RetryState, Throwable> retryPredicate;
   private final BiFunction<Throwable, Throwable, Throwable> failedResultTransformer;
   private final Supplier<R> syncFunction;

   public RetryingSyncSupplier(RetryState state, BiFunction<Throwable, Throwable, Throwable> failedResultTransformer, BiPredicate<RetryState, Throwable> retryPredicate, Supplier<R> syncFunction) {
      this.state = state;
      this.retryPredicate = retryPredicate;
      this.failedResultTransformer = failedResultTransformer;
      this.syncFunction = syncFunction;
   }

   public R get() {
      while(true) {
         try {
            return this.syncFunction.get();
         } catch (RuntimeException runtimeException) {
            this.state.advanceOrThrow(runtimeException, this.failedResultTransformer, this.retryPredicate);
         } catch (Exception exception) {
            this.state.advanceOrThrow(new RuntimeException(exception), this.failedResultTransformer, this.retryPredicate);
         }
      }
   }
}
