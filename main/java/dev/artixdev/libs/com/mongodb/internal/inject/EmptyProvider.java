package dev.artixdev.libs.com.mongodb.internal.inject;

import java.util.Optional;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class EmptyProvider<T> implements OptionalProvider<T> {
   private static final EmptyProvider<?> INSTANCE = new EmptyProvider();

   private EmptyProvider() {
   }

   public Optional<T> optional() {
      return Optional.empty();
   }

   @SuppressWarnings("unchecked")
   public static <T> EmptyProvider<T> instance() {
      return (EmptyProvider<T>) INSTANCE;
   }
}
