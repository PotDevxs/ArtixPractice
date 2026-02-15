package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;

public interface WriteOperation<T> {
   T execute(WriteBinding var1);
}
