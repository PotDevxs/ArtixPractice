package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface BinaryTag extends BinaryTagLike, Examinable {
   @NotNull
   BinaryTagType<? extends BinaryTag> type();

   @NotNull
   default BinaryTag asBinaryTag() {
      return this;
   }
}
