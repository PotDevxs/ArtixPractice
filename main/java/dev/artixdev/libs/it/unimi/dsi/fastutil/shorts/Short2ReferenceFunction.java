package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.function.IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ShortFunction;

@FunctionalInterface
public interface Short2ReferenceFunction<V> extends IntFunction<V>, Function<Short, V> {
   /** @deprecated */
   @Deprecated
   default V apply(int operand) {
      return this.get(SafeMath.safeIntToShort(operand));
   }

   default V put(short key, V value) {
      throw new UnsupportedOperationException();
   }

   V get(short var1);

   default V getOrDefault(short key, V defaultValue) {
      Object v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default V remove(short key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default V put(Short key, V value) {
      short k = key;
      boolean containsKey = this.containsKey(k);
      V v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default V get(Object key) {
      if (key == null) {
         return null;
      } else {
         short k = (Short)key;
         Object v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V getOrDefault(Object key, V defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         short k = (Short)key;
         V v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V remove(Object key) {
      if (key == null) {
         return null;
      } else {
         short k = (Short)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(short key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Short)key);
   }

   default void defaultReturnValue(V rv) {
      throw new UnsupportedOperationException();
   }

   default V defaultReturnValue() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Short> before) {
      return Function.super.compose(before);
   }

   default Short2ByteFunction andThenByte(Reference2ByteFunction<V> after) {
      return (k) -> {
         return after.getByte(this.get(k));
      };
   }

   default Byte2ReferenceFunction<V> composeByte(Byte2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2ShortFunction andThenShort(Reference2ShortFunction<V> after) {
      return (k) -> {
         return after.getShort(this.get(k));
      };
   }

   default Short2ReferenceFunction<V> composeShort(Short2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2IntFunction andThenInt(Reference2IntFunction<V> after) {
      return (k) -> {
         return after.getInt(this.get(k));
      };
   }

   default Int2ReferenceFunction<V> composeInt(Int2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2LongFunction andThenLong(Reference2LongFunction<V> after) {
      return (k) -> {
         return after.getLong(this.get(k));
      };
   }

   default Long2ReferenceFunction<V> composeLong(Long2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2CharFunction andThenChar(Reference2CharFunction<V> after) {
      return (k) -> {
         return after.getChar(this.get(k));
      };
   }

   default Char2ReferenceFunction<V> composeChar(Char2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2FloatFunction andThenFloat(Reference2FloatFunction<V> after) {
      return (k) -> {
         return after.getFloat(this.get(k));
      };
   }

   default Float2ReferenceFunction<V> composeFloat(Float2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2DoubleFunction andThenDouble(Reference2DoubleFunction<V> after) {
      return (k) -> {
         return after.getDouble(this.get(k));
      };
   }

   default Double2ReferenceFunction<V> composeDouble(Double2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Short2ObjectFunction<T> andThenObject(Reference2ObjectFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ReferenceFunction<T, V> composeObject(Object2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }

   default <T> Short2ReferenceFunction<T> andThenReference(Reference2ReferenceFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<T, V> composeReference(Reference2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }
}
