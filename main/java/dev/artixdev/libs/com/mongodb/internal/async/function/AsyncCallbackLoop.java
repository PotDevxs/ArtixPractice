package dev.artixdev.libs.com.mongodb.internal.async.function;

import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public final class AsyncCallbackLoop implements AsyncCallbackRunnable {
   private final LoopState state;
   private final AsyncCallbackRunnable body;

   public AsyncCallbackLoop(LoopState state, AsyncCallbackRunnable body) {
      this.state = state;
      this.body = body;
   }

   public void run(SingleResultCallback<Void> callback) {
      this.body.run(new AsyncCallbackLoop.LoopingCallback(callback));
   }

   @NotThreadSafe
   private class LoopingCallback implements SingleResultCallback<Void> {
      private final SingleResultCallback<Void> wrapped;

      LoopingCallback(SingleResultCallback<Void> callback) {
         this.wrapped = callback;
      }

      public void onResult(@Nullable Void result, @Nullable Throwable t) {
         if (t != null) {
            this.wrapped.onResult(null, t);
         } else {
            boolean continueLooping;
            try {
               continueLooping = AsyncCallbackLoop.this.state.advance();
            } catch (Throwable throwable) {
               this.wrapped.onResult(null, throwable);
               return;
            }

            if (continueLooping) {
               AsyncCallbackLoop.this.body.run(this);
            } else {
               this.wrapped.onResult(result, (Throwable)null);
            }
         }

      }
   }
}
