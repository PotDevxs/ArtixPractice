package dev.artixdev.libs.it.unimi.dsi.fastutil.ints;

import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.bytes.Byte2IntFunction;
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
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2IntFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2CharFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
public interface Int2CharFunction extends java.util.function.IntUnaryOperator, Function<Integer, Character> {
   default int applyAsInt(int operand) {
      return this.get(operand);
   }

   default char put(int key, char value) {
      throw new UnsupportedOperationException();
   }

   char get(int var1);

   default char getOrDefault(int key, char defaultValue) {
      char v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default char remove(int key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Character put(Integer key, Character value) {
      int k = key;
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
         int k = (Integer)key;
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
         int k = (Integer)key;
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
         int k = (Integer)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(int key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Integer)key);
   }

   default void defaultReturnValue(char rv) {
      throw new UnsupportedOperationException();
   }

   default char defaultReturnValue() {
      return '\u0000';
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Integer> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Int2ByteFunction andThenByte(Char2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2CharFunction composeByte(Byte2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2ShortFunction andThenShort(Char2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2CharFunction composeShort(Short2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2IntFunction andThenInt(Char2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2CharFunction composeInt(Int2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2LongFunction andThenLong(Char2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2CharFunction composeLong(Long2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2CharFunction andThenChar(Char2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2CharFunction composeChar(Char2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2FloatFunction andThenFloat(Char2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2CharFunction composeFloat(Float2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2DoubleFunction andThenDouble(Char2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2CharFunction composeDouble(Double2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Int2ObjectFunction<T> andThenObject(Char2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2CharFunction<T> composeObject(Object2IntFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getInt(k));
      };
   }

   default <T> Int2ReferenceFunction<T> andThenReference(Char2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2CharFunction<T> composeReference(Reference2IntFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getInt(k));
      };
   }
}
