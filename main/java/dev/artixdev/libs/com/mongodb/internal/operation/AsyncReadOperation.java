package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadBinding;

public interface AsyncReadOperation<T> {
   void executeAsync(AsyncReadBinding var1, SingleResultCallback<T> var2);
}
