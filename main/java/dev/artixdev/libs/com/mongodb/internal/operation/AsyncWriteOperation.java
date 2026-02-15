package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;

public interface AsyncWriteOperation<T> {
   void executeAsync(AsyncWriteBinding var1, SingleResultCallback<T> var2);
}
