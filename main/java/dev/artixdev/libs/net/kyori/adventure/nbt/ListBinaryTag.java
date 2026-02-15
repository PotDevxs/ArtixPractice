package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

public interface ListBinaryTag extends Iterable<BinaryTag>, BinaryTag, ListTagSetter<ListBinaryTag, BinaryTag> {
   @NotNull
   static ListBinaryTag empty() {
      return ListBinaryTagImpl.EMPTY;
   }

   @NotNull
   static ListBinaryTag from(@NotNull Iterable<? extends BinaryTag> tags) {
      return ((ListBinaryTag.Builder)builder().add(tags)).build();
   }

   @NotNull
   static ListBinaryTag.Builder<BinaryTag> builder() {
      return new ListTagBuilder();
   }

   @NotNull
   static <T extends BinaryTag> ListBinaryTag.Builder<T> builder(@NotNull BinaryTagType<T> type) {
      if (type == BinaryTagTypes.END) {
         throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
      } else {
         return new ListTagBuilder(type);
      }
   }

   @NotNull
   static ListBinaryTag listBinaryTag(@NotNull BinaryTagType<? extends BinaryTag> type, @NotNull List<BinaryTag> tags) {
      if (tags.isEmpty()) {
         return empty();
      } else if (type == BinaryTagTypes.END) {
         throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
      } else {
         return new ListBinaryTagImpl(type, tags);
      }
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ListBinaryTag of(@NotNull BinaryTagType<? extends BinaryTag> type, @NotNull List<BinaryTag> tags) {
      return listBinaryTag(type, tags);
   }

   @NotNull
   default BinaryTagType<ListBinaryTag> type() {
      return BinaryTagTypes.LIST;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   default BinaryTagType<? extends BinaryTag> listType() {
      return this.elementType();
   }

   @NotNull
   BinaryTagType<? extends BinaryTag> elementType();

   int size();

   @NotNull
   BinaryTag get(@Range(from = 0L,to = 2147483647L) int var1);

   @NotNull
   ListBinaryTag set(int var1, @NotNull BinaryTag var2, @Nullable Consumer<? super BinaryTag> var3);

   @NotNull
   ListBinaryTag remove(int var1, @Nullable Consumer<? super BinaryTag> var2);

   default byte getByte(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getByte(index, (byte)0);
   }

   default byte getByte(@Range(from = 0L,to = 2147483647L) int index, byte defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).byteValue() : defaultValue;
   }

   default short getShort(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getShort(index, (short)0);
   }

   default short getShort(@Range(from = 0L,to = 2147483647L) int index, short defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).shortValue() : defaultValue;
   }

   default int getInt(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getInt(index, 0);
   }

   default int getInt(@Range(from = 0L,to = 2147483647L) int index, int defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).intValue() : defaultValue;
   }

   default long getLong(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getLong(index, 0L);
   }

   default long getLong(@Range(from = 0L,to = 2147483647L) int index, long defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).longValue() : defaultValue;
   }

   default float getFloat(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getFloat(index, 0.0F);
   }

   default float getFloat(@Range(from = 0L,to = 2147483647L) int index, float defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).floatValue() : defaultValue;
   }

   default double getDouble(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getDouble(index, 0.0D);
   }

   default double getDouble(@Range(from = 0L,to = 2147483647L) int index, double defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).doubleValue() : defaultValue;
   }

   @NotNull
   default byte[] getByteArray(@Range(from = 0L,to = 2147483647L) int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.BYTE_ARRAY ? ((ByteArrayBinaryTag)tag).value() : new byte[0];
   }

   @NotNull
   default byte[] getByteArray(@Range(from = 0L,to = 2147483647L) int index, @NotNull byte[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.BYTE_ARRAY ? ((ByteArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default String getString(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getString(index, "");
   }

   @NotNull
   default String getString(@Range(from = 0L,to = 2147483647L) int index, @NotNull String defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.STRING ? ((StringBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getList(index, (BinaryTagType)null, empty());
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) int index, @Nullable BinaryTagType<?> elementType) {
      return this.getList(index, elementType, empty());
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) int index, @NotNull ListBinaryTag defaultValue) {
      return this.getList(index, (BinaryTagType)null, defaultValue);
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) int index, @Nullable BinaryTagType<?> elementType, @NotNull ListBinaryTag defaultValue) {
      BinaryTag tag = this.get(index);
      if (tag.type() == BinaryTagTypes.LIST) {
         ListBinaryTag list = (ListBinaryTag)tag;
         if (elementType == null || list.elementType() == elementType) {
            return list;
         }
      }

      return defaultValue;
   }

   @NotNull
   default CompoundBinaryTag getCompound(@Range(from = 0L,to = 2147483647L) int index) {
      return this.getCompound(index, CompoundBinaryTag.empty());
   }

   @NotNull
   default CompoundBinaryTag getCompound(@Range(from = 0L,to = 2147483647L) int index, @NotNull CompoundBinaryTag defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.COMPOUND ? (CompoundBinaryTag)tag : defaultValue;
   }

   @NotNull
   default int[] getIntArray(@Range(from = 0L,to = 2147483647L) int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.INT_ARRAY ? ((IntArrayBinaryTag)tag).value() : new int[0];
   }

   @NotNull
   default int[] getIntArray(@Range(from = 0L,to = 2147483647L) int index, @NotNull int[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.INT_ARRAY ? ((IntArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default long[] getLongArray(@Range(from = 0L,to = 2147483647L) int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.LONG_ARRAY ? ((LongArrayBinaryTag)tag).value() : new long[0];
   }

   @NotNull
   default long[] getLongArray(@Range(from = 0L,to = 2147483647L) int index, @NotNull long[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.LONG_ARRAY ? ((LongArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   Stream<BinaryTag> stream();

   public interface Builder<T extends BinaryTag> extends ListTagSetter<ListBinaryTag.Builder<T>, T> {
      @NotNull
      ListBinaryTag build();
   }
}
