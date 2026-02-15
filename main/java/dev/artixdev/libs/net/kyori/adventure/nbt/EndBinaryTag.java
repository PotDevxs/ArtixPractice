package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface EndBinaryTag extends BinaryTag {
   @NotNull
   static EndBinaryTag endBinaryTag() {
      return EndBinaryTagImpl.INSTANCE;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static EndBinaryTag get() {
      return EndBinaryTagImpl.INSTANCE;
   }

   @NotNull
   default BinaryTagType<EndBinaryTag> type() {
      return BinaryTagTypes.END;
   }
}
