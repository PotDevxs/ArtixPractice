package dev.artixdev.libs.net.kyori.adventure.nbt;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface NumberBinaryTag extends BinaryTag {
   @NotNull
   BinaryTagType<? extends NumberBinaryTag> type();

   byte byteValue();

   double doubleValue();

   float floatValue();

   int intValue();

   long longValue();

   short shortValue();
}
