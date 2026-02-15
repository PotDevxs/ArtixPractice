package dev.artixdev.libs.com.mongodb.internal.async.function;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

@FunctionalInterface
public interface AsyncCallbackBiFunction<P1, P2, R> {
   void apply(P1 var1, P2 var2, SingleResultCallback<R> var3);
}
