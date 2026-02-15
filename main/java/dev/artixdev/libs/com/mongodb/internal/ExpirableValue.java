package dev.artixdev.libs.com.mongodb.internal;

import java.time.Duration;
import java.util.Optional;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@ThreadSafe
public final class ExpirableValue<T> {
   private final T value;
   private final long deadline;

   public static <T> ExpirableValue<T> expired() {
      return new ExpirableValue((Object)null, Duration.ZERO, System.nanoTime());
   }

   public static <T> ExpirableValue<T> expirable(T value, Duration lifetime) {
      return expirable(value, lifetime, System.nanoTime());
   }

   public static <T> ExpirableValue<T> expirable(T value, Duration lifetime, long startNanoTime) {
      return new ExpirableValue(Assertions.assertNotNull(value), (Duration)Assertions.assertNotNull(lifetime), startNanoTime);
   }

   private ExpirableValue(@Nullable T value, Duration lifetime, long currentNanoTime) {
      this.value = value;
      this.deadline = currentNanoTime + lifetime.toNanos();
   }

   public Optional<T> getValue() {
      return this.getValue(System.nanoTime());
   }

   Optional<T> getValue(long currentNanoTime) {
      return currentNanoTime - this.deadline >= 0L ? Optional.empty() : Optional.of(this.value);
   }
}
