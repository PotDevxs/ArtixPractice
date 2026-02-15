package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.Map;
import java.util.function.Consumer;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface CompoundTagSetter<R> {
   @NotNull
   R put(@NotNull String var1, @NotNull BinaryTag var2);

   @NotNull
   R put(@NotNull CompoundBinaryTag var1);

   @NotNull
   R put(@NotNull Map<String, ? extends BinaryTag> var1);

   @NotNull
   default R remove(@NotNull String key) {
      return this.remove(key, (Consumer<? super BinaryTag>) null);
   }

   @NotNull
   R remove(@NotNull String var1, @Nullable Consumer<? super BinaryTag> var2);

   @NotNull
   default R putBoolean(@NotNull String key, boolean value) {
      return this.put(key, value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO);
   }

   @NotNull
   default R putByte(@NotNull String key, byte value) {
      return this.put(key, ByteBinaryTag.byteBinaryTag(value));
   }

   @NotNull
   default R putShort(@NotNull String key, short value) {
      return this.put(key, ShortBinaryTag.shortBinaryTag(value));
   }

   @NotNull
   default R putInt(@NotNull String key, int value) {
      return this.put(key, IntBinaryTag.intBinaryTag(value));
   }

   @NotNull
   default R putLong(@NotNull String key, long value) {
      return this.put(key, LongBinaryTag.longBinaryTag(value));
   }

   @NotNull
   default R putFloat(@NotNull String key, float value) {
      return this.put(key, FloatBinaryTag.floatBinaryTag(value));
   }

   @NotNull
   default R putDouble(@NotNull String key, double value) {
      return this.put(key, DoubleBinaryTag.doubleBinaryTag(value));
   }

   @NotNull
   default R putByteArray(@NotNull String key, @NotNull byte[] value) {
      return this.put(key, ByteArrayBinaryTag.byteArrayBinaryTag(value));
   }

   @NotNull
   default R putString(@NotNull String key, @NotNull String value) {
      return this.put(key, StringBinaryTag.stringBinaryTag(value));
   }

   @NotNull
   default R putIntArray(@NotNull String key, @NotNull int[] value) {
      return this.put(key, IntArrayBinaryTag.intArrayBinaryTag(value));
   }

   @NotNull
   default R putLongArray(@NotNull String key, @NotNull long[] value) {
      return this.put(key, LongArrayBinaryTag.longArrayBinaryTag(value));
   }
}
