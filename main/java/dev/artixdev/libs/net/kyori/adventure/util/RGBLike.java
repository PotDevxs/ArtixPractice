package dev.artixdev.libs.net.kyori.adventure.util;

import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

public interface RGBLike {
   @Range(
      from = 0L,
      to = 255L
   )
   int red();

   @Range(
      from = 0L,
      to = 255L
   )
   int green();

   @Range(
      from = 0L,
      to = 255L
   )
   int blue();

   @NotNull
   default HSVLike asHSV() {
      return HSVLike.fromRGB(this.red(), this.green(), this.blue());
   }
}
