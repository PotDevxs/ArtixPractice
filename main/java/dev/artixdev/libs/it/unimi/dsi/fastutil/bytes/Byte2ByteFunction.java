package dev.artixdev.libs.it.unimi.dsi.fastutil.bytes;

import java.util.function.IntUnaryOperator;
import dev.artixdev.libs.it.unimi.dsi.fastutil.Function;
import dev.artixdev.libs.it.unimi.dsi.fastutil.SafeMath;
import dev.artixdev.libs.it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.doubles.Double2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.floats.Float2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.ints.Int2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import dev.artixdev.libs.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;

@FunctionalInterface
public interface Byte2ByteFunction extends IntUnaryOperator, Function<Byte, Byte> {
   /** @deprecated */
   @Deprecated
   default int applyAsInt(int operand) {
      return this.get(SafeMath.safeIntToByte(operand));
   }

   default byte put(byte key, byte value) {
      throw new UnsupportedOperationException();
   }

   byte get(byte var1);

   default byte getOrDefault(byte key, byte defaultValue) {
      byte v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default byte remove(byte key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Byte put(Byte key, Byte value) {
      byte k = key;
      boolean containsKey = this.containsKey(k);
      byte v = this.put(k, value.byteValue());
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Byte get(Object key) {
      if (key == null) {
         return null;
      } else {
         byte k = (Byte)key;
         byte v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Byte getOrDefault(Object key, Byte defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         byte k = (Byte)key;
         byte v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Byte remove(Object key) {
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

   default void defaultReturnValue(byte rv) {
      throw new UnsupportedOperationException();
   }

   default byte defaultReturnValue() {
      return 0;
   }

   static Byte2ByteFunction identity() {
      return (k) -> {
         return k;
      };
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Byte> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Byte2ByteFunction andThenByte(Byte2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2ByteFunction composeByte(Byte2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2ShortFunction andThenShort(Byte2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2ByteFunction composeShort(Short2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2IntFunction andThenInt(Byte2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2ByteFunction composeInt(Int2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2LongFunction andThenLong(Byte2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2ByteFunction composeLong(Long2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2CharFunction andThenChar(Byte2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2ByteFunction composeChar(Char2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2FloatFunction andThenFloat(Byte2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2ByteFunction composeFloat(Float2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2DoubleFunction andThenDouble(Byte2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2ByteFunction composeDouble(Double2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Byte2ObjectFunction<T> andThenObject(Byte2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ByteFunction<T> composeObject(Object2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }

   default <T> Byte2ReferenceFunction<T> andThenReference(Byte2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ByteFunction<T> composeReference(Reference2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }
}
