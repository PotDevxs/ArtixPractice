package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.function.IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;

@FunctionalInterface
public interface Byte2ObjectFunction<V> extends IntFunction<V>, Function<Byte, V> {
   /** @deprecated */
   @Deprecated
   default V apply(int operand) {
      return this.get(SafeMath.safeIntToByte(operand));
   }

   default V put(byte key, V value) {
      throw new UnsupportedOperationException();
   }

   V get(byte var1);

   default V getOrDefault(byte key, V defaultValue) {
      V v = this.get(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default V remove(byte key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default V put(Byte key, V value) {
      byte k = key;
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
         byte k = (Byte)key;
         V v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V getOrDefault(Object key, V defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         byte k = (Byte)key;
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
         byte k = (Byte)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(byte key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Byte)key);
   }

   default void defaultReturnValue(V rv) {
      throw new UnsupportedOperationException();
   }

   default V defaultReturnValue() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Byte> before) {
      return Function.super.compose(before);
   }

   default Byte2ByteFunction andThenByte(Object2ByteFunction<V> after) {
      return (k) -> {
         return after.getByte(this.get(k));
      };
   }

   default Byte2ObjectFunction<V> composeByte(Byte2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2ShortFunction andThenShort(Object2ShortFunction<V> after) {
      return (k) -> {
         return after.getShort(this.get(k));
      };
   }

   default Short2ObjectFunction<V> composeShort(Short2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2IntFunction andThenInt(Object2IntFunction<V> after) {
      return (k) -> {
         return after.getInt(this.get(k));
      };
   }

   default Int2ObjectFunction<V> composeInt(Int2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2LongFunction andThenLong(Object2LongFunction<V> after) {
      return (k) -> {
         return after.getLong(this.get(k));
      };
   }

   default Long2ObjectFunction<V> composeLong(Long2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2CharFunction andThenChar(Object2CharFunction<V> after) {
      return (k) -> {
         return after.getChar(this.get(k));
      };
   }

   default Char2ObjectFunction<V> composeChar(Char2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2FloatFunction andThenFloat(Object2FloatFunction<V> after) {
      return (k) -> {
         return after.getFloat(this.get(k));
      };
   }

   default Float2ObjectFunction<V> composeFloat(Float2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2DoubleFunction andThenDouble(Object2DoubleFunction<V> after) {
      return (k) -> {
         return after.getDouble(this.get(k));
      };
   }

   default Double2ObjectFunction<V> composeDouble(Double2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Byte2ObjectFunction<T> andThenObject(Object2ObjectFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ObjectFunction<T, V> composeObject(Object2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }

   default <T> Byte2ReferenceFunction<T> andThenReference(Object2ReferenceFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ObjectFunction<T, V> composeReference(Reference2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }
}
