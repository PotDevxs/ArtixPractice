package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.PrimitiveIterator.OfLong;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface LongArrayBinaryTag extends Iterable<Long>, ArrayBinaryTag {
   @NotNull
   static LongArrayBinaryTag longArrayBinaryTag(@NotNull long... value) {
      return new LongArrayBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static LongArrayBinaryTag of(@NotNull long... value) {
      return new LongArrayBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<LongArrayBinaryTag> type() {
      return BinaryTagTypes.LONG_ARRAY;
   }

   @NotNull
   long[] value();

   int size();

   long get(int var1);

   @NotNull
   OfLong iterator();

   @NotNull
   java.util.Spliterator.OfLong spliterator();

   @NotNull
   LongStream stream();

   void forEachLong(@NotNull LongConsumer var1);
}
