package dev.artixdev.libs.it.unimi.dsi.fastutil.floats;

import java.util.function.DoubleToLongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2LongFunction;

@FunctionalInterface
public interface Float2LongFunction extends DoubleToLongFunction, Function<Float, Long> {
   /** @deprecated */
   @Deprecated
   default long applyAsLong(double operand) {
      return this.get(SafeMath.safeDoubleToFloat(operand));
   }

   default long put(float key, long value) {
      throw new UnsupportedOperationException();
   }

   long get(float var1);

   default long getOrDefault(float key, long defaultValue) {
      long v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default long remove(float key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Long put(Float key, Long value) {
      float k = key;
      boolean containsKey = this.containsKey(k);
      long v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Long get(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         long v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Long getOrDefault(Object key, Long defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         float k = (Float)key;
         long v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Long remove(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(float key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Float)key);
   }

   default void defaultReturnValue(long rv) {
      throw new UnsupportedOperationException();
   }

   default long defaultReturnValue() {
      return 0L;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Float> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Float2ByteFunction andThenByte(Long2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2LongFunction composeByte(Byte2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2ShortFunction andThenShort(Long2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2LongFunction composeShort(Short2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2IntFunction andThenInt(Long2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2LongFunction composeInt(Int2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2LongFunction andThenLong(Long2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2LongFunction composeLong(Long2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2CharFunction andThenChar(Long2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2LongFunction composeChar(Char2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2FloatFunction andThenFloat(Long2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2LongFunction composeFloat(Float2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2DoubleFunction andThenDouble(Long2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2LongFunction composeDouble(Double2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Float2ObjectFunction<T> andThenObject(Long2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2LongFunction<T> composeObject(Object2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }

   default <T> Float2ReferenceFunction<T> andThenReference(Long2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2LongFunction<T> composeReference(Reference2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }
}
