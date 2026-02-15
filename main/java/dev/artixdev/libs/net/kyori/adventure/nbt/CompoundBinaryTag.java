package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface CompoundBinaryTag extends Iterable<Entry<String, ? extends BinaryTag>>, BinaryTag, CompoundTagSetter<CompoundBinaryTag> {
   @NotNull
   static CompoundBinaryTag empty() {
      return CompoundBinaryTagImpl.EMPTY;
   }

   @NotNull
   static CompoundBinaryTag from(@NotNull Map<String, ? extends BinaryTag> tags) {
      return (CompoundBinaryTag)(tags.isEmpty() ? empty() : new CompoundBinaryTagImpl(new HashMap(tags)));
   }

   @NotNull
   static CompoundBinaryTag.Builder builder() {
      return new CompoundTagBuilder();
   }

   @NotNull
   default BinaryTagType<CompoundBinaryTag> type() {
      return BinaryTagTypes.COMPOUND;
   }

   @NotNull
   Set<String> keySet();

   @Nullable
   BinaryTag get(String var1);

   default boolean getBoolean(@NotNull String key) {
      return this.getBoolean(key, false);
   }

   default boolean getBoolean(@NotNull String key, boolean defaultValue) {
      return this.getByte(key) != 0 || defaultValue;
   }

   default byte getByte(@NotNull String key) {
      return this.getByte(key, (byte)0);
   }

   byte getByte(@NotNull String var1, byte var2);

   default short getShort(@NotNull String key) {
      return this.getShort(key, (short)0);
   }

   short getShort(@NotNull String var1, short var2);

   default int getInt(@NotNull String key) {
      return this.getInt(key, 0);
   }

   int getInt(@NotNull String var1, int var2);

   default long getLong(@NotNull String key) {
      return this.getLong(key, 0L);
   }

   long getLong(@NotNull String var1, long var2);

   default float getFloat(@NotNull String key) {
      return this.getFloat(key, 0.0F);
   }

   float getFloat(@NotNull String var1, float var2);

   default double getDouble(@NotNull String key) {
      return this.getDouble(key, 0.0D);
   }

   double getDouble(@NotNull String var1, double var2);

   @NotNull
   byte[] getByteArray(@NotNull String var1);

   @NotNull
   byte[] getByteArray(@NotNull String var1, @NotNull byte[] var2);

   @NotNull
   default String getString(@NotNull String key) {
      return this.getString(key, "");
   }

   @NotNull
   String getString(@NotNull String var1, @NotNull String var2);

   @NotNull
   default ListBinaryTag getList(@NotNull String key) {
      return this.getList(key, ListBinaryTag.empty());
   }

   @NotNull
   ListBinaryTag getList(@NotNull String var1, @NotNull ListBinaryTag var2);

   @NotNull
   default ListBinaryTag getList(@NotNull String key, @NotNull BinaryTagType<? extends BinaryTag> expectedType) {
      return this.getList(key, expectedType, ListBinaryTag.empty());
   }

   @NotNull
   ListBinaryTag getList(@NotNull String var1, @NotNull BinaryTagType<? extends BinaryTag> var2, @NotNull ListBinaryTag var3);

   @NotNull
   default CompoundBinaryTag getCompound(@NotNull String key) {
      return this.getCompound(key, empty());
   }

   @NotNull
   CompoundBinaryTag getCompound(@NotNull String var1, @NotNull CompoundBinaryTag var2);

   @NotNull
   int[] getIntArray(@NotNull String var1);

   @NotNull
   int[] getIntArray(@NotNull String var1, @NotNull int[] var2);

   @NotNull
   long[] getLongArray(@NotNull String var1);

   @NotNull
   long[] getLongArray(@NotNull String var1, @NotNull long[] var2);

   public interface Builder extends CompoundTagSetter<CompoundBinaryTag.Builder> {
      @NotNull
      CompoundBinaryTag build();
   }
}
