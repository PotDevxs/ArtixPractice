package dev.artixdev.libs.it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleUnaryOperator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2FloatFunction;

@FunctionalInterface
public interface Double2FloatFunction extends DoubleUnaryOperator, Function<Double, Float> {
   default double applyAsDouble(double operand) {
      return (double)this.get(operand);
   }

   default float put(double key, float value) {
      throw new UnsupportedOperationException();
   }

   float get(double var1);

   default float getOrDefault(double key, float defaultValue) {
      float v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default float remove(double key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Float put(Double key, Float value) {
      double k = key;
      boolean containsKey = this.containsKey(k);
      float v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Float get(Object key) {
      if (key == null) {
         return null;
      } else {
         double k = (Double)key;
         float v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Float getOrDefault(Object key, Float defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         double k = (Double)key;
         float v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Float remove(Object key) {
      if (key == null) {
         return null;
      } else {
         double k = (Double)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(double key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Double)key);
   }

   default void defaultReturnValue(float rv) {
      throw new UnsupportedOperationException();
   }

   default float defaultReturnValue() {
      return 0.0F;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Double> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Double2ByteFunction andThenByte(Float2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2FloatFunction composeByte(Byte2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2ShortFunction andThenShort(Float2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2FloatFunction composeShort(Short2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2IntFunction andThenInt(Float2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2FloatFunction composeInt(Int2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2LongFunction andThenLong(Float2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2FloatFunction composeLong(Long2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2CharFunction andThenChar(Float2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2FloatFunction composeChar(Char2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2FloatFunction andThenFloat(Float2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2FloatFunction composeFloat(Float2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2DoubleFunction andThenDouble(Float2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2FloatFunction composeDouble(Double2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Double2ObjectFunction<T> andThenObject(Float2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2FloatFunction<T> composeObject(Object2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }

   default <T> Double2ReferenceFunction<T> andThenReference(Float2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2FloatFunction<T> composeReference(Reference2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }
}
