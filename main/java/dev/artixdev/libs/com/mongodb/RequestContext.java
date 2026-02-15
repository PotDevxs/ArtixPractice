package dev.artixdev.libs.com.mongodb;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface RequestContext {
   <T> T get(Object var1);

   default <T> T get(Class<T> key) {
      T v = this.get((Object)key);
      if (key.isInstance(v)) {
         return v;
      } else {
         throw new NoSuchElementException("Context does not contain a value of type " + key.getName());
      }
   }

   @Nullable
   default <T> T getOrDefault(Object key, @Nullable T defaultValue) {
      return !this.hasKey(key) ? defaultValue : this.get(key);
   }

   default <T> Optional<T> getOrEmpty(Object key) {
      return this.hasKey(key) ? Optional.of(this.get(key)) : Optional.empty();
   }

   boolean hasKey(Object var1);

   boolean isEmpty();

   void put(Object var1, Object var2);

   default void putNonNull(Object key, @Nullable Object valueOrNull) {
      if (valueOrNull != null) {
         this.put(key, valueOrNull);
      }

   }

   void delete(Object var1);

   int size();

   Stream<Entry<Object, Object>> stream();
}
