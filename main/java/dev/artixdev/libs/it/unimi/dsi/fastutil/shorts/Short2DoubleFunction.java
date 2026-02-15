package dev.artixdev.libs.it.unimi.dsi.fastutil.shorts;

import java.util.function.IntToDoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ShortFunction;
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
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ShortFunction;

@FunctionalInterface
public interface Short2DoubleFunction extends IntToDoubleFunction, Function<Short, Double> {
   /** @deprecated */
   @Deprecated
   default double applyAsDouble(int operand) {
      return this.get(SafeMath.safeIntToShort(operand));
   }

   default double put(short key, double value) {
      throw new UnsupportedOperationException();
   }

   double get(short var1);

   default double getOrDefault(short key, double defaultValue) {
      double v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default double remove(short key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Double put(Short key, Double value) {
      short k = key;
      boolean containsKey = this.containsKey(k);
      double v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Double get(Object key) {
      if (key == null) {
         return null;
      } else {
         short k = (Short)key;
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
         short k = (Short)key;
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

   default void defaultReturnValue(double rv) {
      throw new UnsupportedOperationException();
   }

   default double defaultReturnValue() {
      return 0.0D;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Short> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Short2ByteFunction andThenByte(Double2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2DoubleFunction composeByte(Byte2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2ShortFunction andThenShort(Double2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2DoubleFunction composeShort(Short2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2IntFunction andThenInt(Double2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2DoubleFunction composeInt(Int2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2LongFunction andThenLong(Double2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2DoubleFunction composeLong(Long2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2CharFunction andThenChar(Double2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2DoubleFunction composeChar(Char2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2FloatFunction andThenFloat(Double2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2DoubleFunction composeFloat(Float2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2DoubleFunction andThenDouble(Double2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2DoubleFunction composeDouble(Double2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Short2ObjectFunction<T> andThenObject(Double2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2DoubleFunction<T> composeObject(Object2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }

   default <T> Short2ReferenceFunction<T> andThenReference(Double2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2DoubleFunction<T> composeReference(Reference2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }
}
