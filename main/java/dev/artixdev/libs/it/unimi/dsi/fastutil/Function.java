package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * Base type for fastutil function interfaces. Subinterfaces extend
 * {@link java.util.function.Function} (via default {@code apply} delegating to primitive
 * {@code get}) and use this base for {@link #compose} and {@link #andThen} chaining.
 *
 * @param <K> the type of the input to the function
 * @param <V> the type of the output of the function
 */
public interface Function<K, V> {

   @SuppressWarnings("unchecked")
   default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends K> before) {
      java.util.function.Function<K, V> self = (java.util.function.Function<K, V>) this;
      return t -> self.apply(before.apply(t));
   }

   @SuppressWarnings("unchecked")
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super V, ? extends T> after) {
      java.util.function.Function<K, V> self = (java.util.function.Function<K, V>) this;
      return k -> after.apply(self.apply(k));
   }
}
