package dev.artixdev.libs.com.mongodb.internal.async.function;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

@FunctionalInterface
public interface AsyncCallbackRunnable {
   void run(SingleResultCallback<Void> var1);

   default AsyncCallbackSupplier<Void> asSupplier() {
      return this::run;
   }

   default AsyncCallbackRunnable whenComplete(Runnable after) {
      return (callback) -> {
         this.asSupplier().whenComplete(after).get(callback);
      };
   }
}
