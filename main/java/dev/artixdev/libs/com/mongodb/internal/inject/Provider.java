package dev.artixdev.libs.com.mongodb.internal.inject;

import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface Provider<T> extends Supplier<T>, OptionalProvider<T> {
   T get();

   Optional<T> optional();
}
