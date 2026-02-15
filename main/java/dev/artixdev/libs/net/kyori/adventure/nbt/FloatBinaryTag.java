package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface FloatBinaryTag extends NumberBinaryTag {
   @NotNull
   static FloatBinaryTag floatBinaryTag(float value) {
      return new FloatBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static FloatBinaryTag of(float value) {
      return new FloatBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<FloatBinaryTag> type() {
      return BinaryTagTypes.FLOAT;
   }

   float value();
}
