package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ArrayBinaryTag extends BinaryTag {
   @NotNull
   BinaryTagType<? extends ArrayBinaryTag> type();
}
