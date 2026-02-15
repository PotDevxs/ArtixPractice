package dev.artixdev.libs.com.mongodb.internal.async.function;

import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

@FunctionalInterface
public interface AsyncCallbackSupplier<R> {
   void get(SingleResultCallback<R> var1);

   default AsyncCallbackSupplier<R> whenComplete(Runnable after) {
      @NotThreadSafe
      final class MutableBoolean {
         private boolean value;
      }

      MutableBoolean afterExecuted = new MutableBoolean();
      Runnable trackableAfter = () -> {
         try {
            after.run();
         } finally {
            afterExecuted.value = true;
         }

      };
      return (callback) -> {
         SingleResultCallback<R> callbackThatCallsAfter = (result, t) -> {
            Throwable primaryException = t;

            try {
               trackableAfter.run();
            } catch (Throwable suppressed) {
               if (t == null) {
                  primaryException = suppressed;
               } else {
                  t.addSuppressed(suppressed);
               }

               callback.onResult(null, primaryException);
               return;
            }

            callback.onResult(result, t);
         };
         Throwable primaryUnexpectedException = null;

         try {
            this.get(callbackThatCallsAfter);
         } catch (Throwable throwable) {
            primaryUnexpectedException = throwable;
            throw throwable;
         } finally {
            if (primaryUnexpectedException != null && !afterExecuted.value) {
               try {
                  trackableAfter.run();
               } catch (Throwable suppressed) {
                  primaryUnexpectedException.addSuppressed(suppressed);
               }
            }

         }

      };
   }
}
