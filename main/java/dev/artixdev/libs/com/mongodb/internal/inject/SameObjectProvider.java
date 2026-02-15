package dev.artixdev.libs.com.mongodb.internal.inject;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@ThreadSafe
public final class SameObjectProvider<T> implements Provider<T> {
   private final AtomicReference<T> object = new AtomicReference();

   private SameObjectProvider(@Nullable T o) {
      if (o != null) {
         this.initialize(o);
      }

   }

   public T get() {
      return Assertions.assertNotNull(this.object.get());
   }

   public Optional<T> optional() {
      return Optional.of(this.get());
   }

   public void initialize(T o) {
      Assertions.assertTrue(this.object.compareAndSet(null, o));
   }

   public static <T> SameObjectProvider<T> initialized(T o) {
      return new SameObjectProvider(o);
   }

   public static <T> SameObjectProvider<T> uninitialized() {
      return new SameObjectProvider((Object)null);
   }
}
