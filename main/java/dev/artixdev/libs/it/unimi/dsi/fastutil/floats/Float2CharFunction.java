package dev.artixdev.libs.it.unimi.dsi.fastutil.floats;

import java.util.function.DoubleToIntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2FloatFunction;

@FunctionalInterface
public interface Float2CharFunction extends DoubleToIntFunction, Function<Float, Character> {
   /** @deprecated */
   @Deprecated
   default int applyAsInt(double operand) {
      return this.get(SafeMath.safeDoubleToFloat(operand));
   }

   default char put(float key, char value) {
      throw new UnsupportedOperationException();
   }

   char get(float var1);

   default char getOrDefault(float key, char defaultValue) {
      char v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default char remove(float key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Character put(Float key, Character value) {
      float k = key;
      boolean containsKey = this.containsKey(k);
      char v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Character get(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         char v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Character getOrDefault(Object key, Character defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         float k = (Float)key;
         char v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Character remove(Object key) {
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

   default void defaultReturnValue(char rv) {
      throw new UnsupportedOperationException();
   }

   default char defaultReturnValue() {
      return '\u0000';
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Float> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Float2ByteFunction andThenByte(Char2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2CharFunction composeByte(Byte2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2ShortFunction andThenShort(Char2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2CharFunction composeShort(Short2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2IntFunction andThenInt(Char2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2CharFunction composeInt(Int2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2LongFunction andThenLong(Char2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2CharFunction composeLong(Long2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2CharFunction andThenChar(Char2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2CharFunction composeChar(Char2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2FloatFunction andThenFloat(Char2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2CharFunction composeFloat(Float2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2DoubleFunction andThenDouble(Char2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2CharFunction composeDouble(Double2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Float2ObjectFunction<T> andThenObject(Char2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2CharFunction<T> composeObject(Object2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }

   default <T> Float2ReferenceFunction<T> andThenReference(Char2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2CharFunction<T> composeReference(Reference2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }
}
