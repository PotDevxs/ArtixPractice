package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface StringBinaryTag extends BinaryTag {
   @NotNull
   static StringBinaryTag stringBinaryTag(@NotNull String value) {
      return new StringBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static StringBinaryTag of(@NotNull String value) {
      return new StringBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<StringBinaryTag> type() {
      return BinaryTagTypes.STRING;
   }

   @NotNull
   String value();
}
