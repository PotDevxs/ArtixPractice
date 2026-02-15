package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface DoubleBinaryTag extends NumberBinaryTag {
   @NotNull
   static DoubleBinaryTag doubleBinaryTag(double value) {
      return new DoubleBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static DoubleBinaryTag of(double value) {
      return new DoubleBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<DoubleBinaryTag> type() {
      return BinaryTagTypes.DOUBLE;
   }

   double value();
}
