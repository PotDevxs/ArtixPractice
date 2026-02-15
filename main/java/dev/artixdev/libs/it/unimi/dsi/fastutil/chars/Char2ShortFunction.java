package dev.artixdev.libs.it.unimi.dsi.fastutil.chars;

import java.util.function.IntUnaryOperator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ShortFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2DoubleFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2FloatFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2LongFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ObjectFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ReferenceFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ShortFunction;

@FunctionalInterface
public interface Char2ShortFunction extends IntUnaryOperator, Function<Character, Short> {
   /** @deprecated */
   @Deprecated
   default int applyAsInt(int operand) {
      return this.get(SafeMath.safeIntToChar(operand));
   }

   default short put(char key, short value) {
      throw new UnsupportedOperationException();
   }

   short get(char var1);

   default short getOrDefault(char key, short defaultValue) {
      short v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default short remove(char key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Short put(Character key, Short value) {
      char k = key;
      boolean containsKey = this.containsKey(k);
      short v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Short get(Object key) {
      if (key == null) {
         return null;
      } else {
         char k = (Character)key;
         short v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Short getOrDefault(Object key, Short defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         char k = (Character)key;
         short v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Short remove(Object key) {
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

   default void defaultReturnValue(short rv) {
      throw new UnsupportedOperationException();
   }

   default short defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Character> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Character, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Char2ByteFunction andThenByte(Short2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2ShortFunction composeByte(Byte2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2ShortFunction andThenShort(Short2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2ShortFunction composeShort(Short2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2IntFunction andThenInt(Short2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2ShortFunction composeInt(Int2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2LongFunction andThenLong(Short2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2ShortFunction composeLong(Long2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2CharFunction andThenChar(Short2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2ShortFunction composeChar(Char2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2FloatFunction andThenFloat(Short2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2ShortFunction composeFloat(Float2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Char2DoubleFunction andThenDouble(Short2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2ShortFunction composeDouble(Double2CharFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Char2ObjectFunction<T> andThenObject(Short2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ShortFunction<T> composeObject(Object2CharFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getChar(k));
      };
   }

   default <T> Char2ReferenceFunction<T> andThenReference(Short2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ShortFunction<T> composeReference(Reference2CharFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getChar(k));
      };
   }
}
