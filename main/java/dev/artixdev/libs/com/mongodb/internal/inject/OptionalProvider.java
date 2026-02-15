package dev.artixdev.libs.com.mongodb.internal.inject;

import java.util.Optional;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface OptionalProvider<T> {
   Optional<T> optional();
}
