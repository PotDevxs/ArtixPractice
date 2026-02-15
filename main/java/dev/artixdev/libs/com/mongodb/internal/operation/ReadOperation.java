package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;

public interface ReadOperation<T> {
   T execute(ReadBinding var1);
}
