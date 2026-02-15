package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.PrimitiveIterator.OfInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface IntArrayBinaryTag extends Iterable<Integer>, ArrayBinaryTag {
   @NotNull
   static IntArrayBinaryTag intArrayBinaryTag(@NotNull int... value) {
      return new IntArrayBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static IntArrayBinaryTag of(@NotNull int... value) {
      return new IntArrayBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<IntArrayBinaryTag> type() {
      return BinaryTagTypes.INT_ARRAY;
   }

   @NotNull
   int[] value();

   int size();

   int get(int var1);

   @NotNull
   OfInt iterator();

   @NotNull
   java.util.Spliterator.OfInt spliterator();

   @NotNull
   IntStream stream();

   void forEachInt(@NotNull IntConsumer var1);
}
