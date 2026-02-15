package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ByteBinaryTag extends NumberBinaryTag {
   ByteBinaryTag ZERO = new ByteBinaryTagImpl((byte)0);
   ByteBinaryTag ONE = new ByteBinaryTagImpl((byte)1);

   @NotNull
   static ByteBinaryTag byteBinaryTag(byte value) {
      if (value == 0) {
         return ZERO;
      } else {
         return (ByteBinaryTag)(value == 1 ? ONE : new ByteBinaryTagImpl(value));
      }
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ByteBinaryTag of(byte value) {
      return byteBinaryTag(value);
   }

   @NotNull
   default BinaryTagType<ByteBinaryTag> type() {
      return BinaryTagTypes.BYTE;
   }

   byte value();
}
