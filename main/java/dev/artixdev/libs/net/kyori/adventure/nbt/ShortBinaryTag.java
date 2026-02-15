package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ShortBinaryTag extends NumberBinaryTag {
   @NotNull
   static ShortBinaryTag shortBinaryTag(short value) {
      return new ShortBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ShortBinaryTag of(short value) {
      return new ShortBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<ShortBinaryTag> type() {
      return BinaryTagTypes.SHORT;
   }

   short value();
}
