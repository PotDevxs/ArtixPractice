package dev.artixdev.libs.it.unimi.dsi.fastutil.objects;

import java.util.function.ToIntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
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
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;

@FunctionalInterface
public interface Object2ByteFunction<K> extends ToIntFunction<K>, Function<K, Byte> {
   default int applyAsInt(K operand) {
      return this.getByte(operand);
   }

   default byte put(K key, byte value) {
      throw new UnsupportedOperationException();
   }

   byte getByte(Object var1);

   default byte getOrDefault(Object key, byte defaultValue) {
      byte v;
      return (v = this.getByte(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default byte removeByte(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Byte put(K key, Byte value) {
      boolean containsKey = this.containsKey(key);
      byte v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Byte get(Object key) {
      byte v;
      return (v = this.getByte(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Byte getOrDefault(Object key, Byte defaultValue) {
      byte v = this.getByte(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Byte remove(Object key) {
      return this.containsKey(key) ? this.removeByte(key) : null;
   }

   default void defaultReturnValue(byte rv) {
      throw new UnsupportedOperationException();
   }

   default byte defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Object2ByteFunction<K> andThenByte(Byte2ByteFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Byte2ByteFunction composeByte(Byte2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2ShortFunction<K> andThenShort(Byte2ShortFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Short2ByteFunction composeShort(Short2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2IntFunction<K> andThenInt(Byte2IntFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Int2ByteFunction composeInt(Int2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2LongFunction<K> andThenLong(Byte2LongFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Long2ByteFunction composeLong(Long2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2CharFunction<K> andThenChar(Byte2CharFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Char2ByteFunction composeChar(Char2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2FloatFunction<K> andThenFloat(Byte2FloatFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Float2ByteFunction composeFloat(Float2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2DoubleFunction<K> andThenDouble(Byte2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Double2ByteFunction composeDouble(Double2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default <T> Object2ObjectFunction<K, T> andThenObject(Byte2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default <T> Object2ByteFunction<T> composeObject(Object2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default <T> Object2ReferenceFunction<K, T> andThenReference(Byte2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default <T> Reference2ByteFunction<T> composeReference(Reference2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }
}
