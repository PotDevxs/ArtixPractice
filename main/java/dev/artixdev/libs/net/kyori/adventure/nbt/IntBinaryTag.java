package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface IntBinaryTag extends NumberBinaryTag {
   @NotNull
   static IntBinaryTag intBinaryTag(int value) {
      return new IntBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static IntBinaryTag of(int value) {
      return new IntBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<IntBinaryTag> type() {
      return BinaryTagTypes.INT;
   }

   int value();
}
