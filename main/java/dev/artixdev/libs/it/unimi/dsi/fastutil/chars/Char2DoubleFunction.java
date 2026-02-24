package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.function.IntToDoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;

@FunctionalInterface
public interface Char2DoubleFunction extends IntToDoubleFunction, Function<Character, Double> {
   /** @deprecated */
   @Deprecated
   default double applyAsDouble(int operand) {
      return this.get(SafeMath.safeIntToChar(operand));
   }

   default double put(char key, double value) {
      throw new UnsupportedOperationException();
   }

   double get(char var1);

   default double getOrDefault(char key, double defaultValue) {
      double v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default double remove(char key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Double put(Character key, Double value) {
      char k = key;
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
         char k = (Character)key;
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
         char k = (Character)key;
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
         char k = (Character)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(char key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Character)key);
   }

   default void defaultReturnValue(double rv) {
      throw new UnsupportedOperationException();
   }

   default double defaultReturnValue() {
      return 0.0D;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Character> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Char2ByteFunction andThenByte(Double2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2DoubleFunction composeByte(Byte2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2ShortFunction andThenShort(Double2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2DoubleFunction composeShort(Short2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2IntFunction andThenInt(Double2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2DoubleFunction composeInt(Int2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2LongFunction andThenLong(Double2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2DoubleFunction composeLong(Long2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2CharFunction andThenChar(Double2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2DoubleFunction composeChar(Char2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2FloatFunction andThenFloat(Double2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2DoubleFunction composeFloat(Float2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2DoubleFunction andThenDouble(Double2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2DoubleFunction composeDouble(Double2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Char2ObjectFunction<T> andThenObject(Double2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2DoubleFunction<T> composeObject(Object2CharFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getChar(k));
      };
   }

   default <T> Char2ReferenceFunction<T> andThenReference(Double2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2DoubleFunction<T> composeReference(Reference2CharFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getChar(k));
      };
   }
}
