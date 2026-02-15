package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ByteArrayBinaryTag extends Iterable<Byte>, ArrayBinaryTag {
   @NotNull
   static ByteArrayBinaryTag byteArrayBinaryTag(@NotNull byte... value) {
      return new ByteArrayBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ByteArrayBinaryTag of(@NotNull byte... value) {
      return new ByteArrayBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<ByteArrayBinaryTag> type() {
      return BinaryTagTypes.BYTE_ARRAY;
   }

   @NotNull
   byte[] value();

   int size();

   byte get(int var1);
}
