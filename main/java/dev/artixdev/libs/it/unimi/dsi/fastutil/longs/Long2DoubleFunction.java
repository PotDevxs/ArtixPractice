package dev.artixdev.libs.it.unimi.dsi.fastutil.longs;

import java.util.function.LongToDoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2LongFunction;

@FunctionalInterface
public interface Long2DoubleFunction extends LongToDoubleFunction, Function<Long, Double> {
   default double applyAsDouble(long operand) {
      return this.get(operand);
   }

   default double put(long key, double value) {
      throw new UnsupportedOperationException();
   }

   double get(long var1);

   default double getOrDefault(long key, double defaultValue) {
      double v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default double remove(long key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Double put(Long key, Double value) {
      long k = key;
      boolean containsKey = this.containsKey(k);
      double v = this.put(k, value.doubleValue());
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Double get(Object key) {
      if (key == null) {
         return null;
      } else {
         long k = (Long)key;
         double v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Double getOrDefault(Object key, Double defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         long k = (Long)key;
         double v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Double remove(Object key) {
      if (key == null) {
         return null;
      } else {
         long k = (Long)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(long key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Long)key);
   }

   default void defaultReturnValue(double rv) {
      throw new UnsupportedOperationException();
   }

   default double defaultReturnValue() {
      return 0.0D;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Long> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Long2ByteFunction andThenByte(Double2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2DoubleFunction composeByte(Byte2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2ShortFunction andThenShort(Double2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2DoubleFunction composeShort(Short2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2IntFunction andThenInt(Double2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2DoubleFunction composeInt(Int2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2LongFunction andThenLong(Double2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2DoubleFunction composeLong(Long2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2CharFunction andThenChar(Double2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2DoubleFunction composeChar(Char2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2FloatFunction andThenFloat(Double2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2DoubleFunction composeFloat(Float2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2DoubleFunction andThenDouble(Double2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2DoubleFunction composeDouble(Double2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Long2ObjectFunction<T> andThenObject(Double2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2DoubleFunction<T> composeObject(Object2LongFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getLong(k));
      };
   }

   default <T> Long2ReferenceFunction<T> andThenReference(Double2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2DoubleFunction<T> composeReference(Reference2LongFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getLong(k));
      };
   }
}
