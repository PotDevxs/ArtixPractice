package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface LongBinaryTag extends NumberBinaryTag {
   @NotNull
   static LongBinaryTag longBinaryTag(long value) {
      return new LongBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static LongBinaryTag of(long value) {
      return new LongBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<LongBinaryTag> type() {
      return BinaryTagTypes.LONG;
   }

   long value();
}
