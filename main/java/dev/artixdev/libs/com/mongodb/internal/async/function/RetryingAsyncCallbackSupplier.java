package dev.artixdev.libs.com.mongodb.internal.async.function;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public final class RetryingAsyncCallbackSupplier<R> implements AsyncCallbackSupplier<R> {
   private final RetryState state;
   private final BiPredicate<RetryState, Throwable> retryPredicate;
   private final BiFunction<Throwable, Throwable, Throwable> failedResultTransformer;
   private final AsyncCallbackSupplier<R> asyncFunction;

   public RetryingAsyncCallbackSupplier(RetryState state, BiFunction<Throwable, Throwable, Throwable> failedResultTransformer, BiPredicate<RetryState, Throwable> retryPredicate, AsyncCallbackSupplier<R> asyncFunction) {
      this.state = state;
      this.retryPredicate = retryPredicate;
      this.failedResultTransformer = failedResultTransformer;
      this.asyncFunction = asyncFunction;
   }

   public void get(SingleResultCallback<R> callback) {
      this.asyncFunction.get(new RetryingAsyncCallbackSupplier.RetryingCallback(callback));
   }

   @NotThreadSafe
   private class RetryingCallback implements SingleResultCallback<R> {
      private final SingleResultCallback<R> wrapped;

      RetryingCallback(SingleResultCallback<R> callback) {
         this.wrapped = callback;
      }

      public void onResult(@Nullable R result, @Nullable Throwable t) {
         if (t != null) {
            try {
               RetryingAsyncCallbackSupplier.this.state.advanceOrThrow(t, RetryingAsyncCallbackSupplier.this.failedResultTransformer, RetryingAsyncCallbackSupplier.this.retryPredicate);
            } catch (Throwable throwable) {
               this.wrapped.onResult(null, throwable);
               return;
            }

            RetryingAsyncCallbackSupplier.this.asyncFunction.get(this);
         } else {
            this.wrapped.onResult(result, (Throwable)null);
         }

      }
   }
}
