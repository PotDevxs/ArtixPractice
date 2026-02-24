package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;

@FunctionalInterface
public interface Reference2ReferenceFunction<K, V> extends Function<K, V> {
   default V put(K key, V value) {
      throw new UnsupportedOperationException();
   }

   V get(Object var1);

   default boolean containsKey(Object key) {
      return this.get(key) != this.defaultReturnValue();
   }

   default V getOrDefault(Object key, V defaultValue) {
      V v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default V remove(Object key) {
      throw new UnsupportedOperationException();
   }

   default void defaultReturnValue(V rv) {
      throw new UnsupportedOperationException();
   }

   default V defaultReturnValue() {
      return null;
   }

   default Reference2ByteFunction<K> andThenByte(Reference2ByteFunction<V> after) {
      return (k) -> {
         return after.getByte(this.get(k));
      };
   }

   default Byte2ReferenceFunction<V> composeByte(Byte2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2ShortFunction<K> andThenShort(Reference2ShortFunction<V> after) {
      return (k) -> {
         return after.getShort(this.get(k));
      };
   }

   default Short2ReferenceFunction<V> composeShort(Short2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2IntFunction<K> andThenInt(Reference2IntFunction<V> after) {
      return (k) -> {
         return after.getInt(this.get(k));
      };
   }

   default Int2ReferenceFunction<V> composeInt(Int2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2LongFunction<K> andThenLong(Reference2LongFunction<V> after) {
      return (k) -> {
         return after.getLong(this.get(k));
      };
   }

   default Long2ReferenceFunction<V> composeLong(Long2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2CharFunction<K> andThenChar(Reference2CharFunction<V> after) {
      return (k) -> {
         return after.getChar(this.get(k));
      };
   }

   default Char2ReferenceFunction<V> composeChar(Char2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2FloatFunction<K> andThenFloat(Reference2FloatFunction<V> after) {
      return (k) -> {
         return after.getFloat(this.get(k));
      };
   }

   default Float2ReferenceFunction<V> composeFloat(Float2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2DoubleFunction<K> andThenDouble(Reference2DoubleFunction<V> after) {
      return (k) -> {
         return after.getDouble(this.get(k));
      };
   }

   default Double2ReferenceFunction<V> composeDouble(Double2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Reference2ObjectFunction<K, T> andThenObject(Reference2ObjectFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ReferenceFunction<T, V> composeObject(Object2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<K, T> andThenReference(Reference2ReferenceFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<T, V> composeReference(Reference2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }
}
