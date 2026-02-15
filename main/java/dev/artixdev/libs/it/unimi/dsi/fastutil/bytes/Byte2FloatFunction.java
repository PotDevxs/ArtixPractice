package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.function.IntToDoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2FloatFunction;

@FunctionalInterface
public interface Byte2FloatFunction extends IntToDoubleFunction, Function<Byte, Float> {
   /** @deprecated */
   @Deprecated
   default double applyAsDouble(int operand) {
      return (double)this.get(SafeMath.safeIntToByte(operand));
   }

   default float put(byte key, float value) {
      throw new UnsupportedOperationException();
   }

   float get(byte var1);

   default float getOrDefault(byte key, float defaultValue) {
      float v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default float remove(byte key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Float put(Byte key, Float value) {
      byte k = key;
      boolean containsKey = this.containsKey(k);
      float v = this.put(k, value.floatValue());
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Float get(Object key) {
      if (key == null) {
         return null;
      } else {
         byte k = (Byte)key;
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
         byte k = (Byte)key;
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

   default void defaultReturnValue(float rv) {
      throw new UnsupportedOperationException();
   }

   default float defaultReturnValue() {
      return 0.0F;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Byte> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Byte2ByteFunction andThenByte(Float2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2FloatFunction composeByte(Byte2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2ShortFunction andThenShort(Float2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2FloatFunction composeShort(Short2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2IntFunction andThenInt(Float2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2FloatFunction composeInt(Int2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2LongFunction andThenLong(Float2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2FloatFunction composeLong(Long2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2CharFunction andThenChar(Float2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2FloatFunction composeChar(Char2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2FloatFunction andThenFloat(Float2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2FloatFunction composeFloat(Float2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2DoubleFunction andThenDouble(Float2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2FloatFunction composeDouble(Double2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Byte2ObjectFunction<T> andThenObject(Float2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2FloatFunction<T> composeObject(Object2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }

   default <T> Byte2ReferenceFunction<T> andThenReference(Float2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2FloatFunction<T> composeReference(Reference2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }
}
