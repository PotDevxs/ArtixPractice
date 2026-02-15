package dev.artixdev.libs.com.mongodb.internal.async.function;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

@FunctionalInterface
public interface AsyncCallbackFunction<P, R> {
   void apply(P var1, SingleResultCallback<R> var2);
}
