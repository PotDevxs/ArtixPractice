package dev.artixdev.libs.net.kyori.adventure.util;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

public interface HSVLike extends Examinable {
   @NotNull
   static HSVLike hsvLike(float h, float s, float v) {
      return new HSVLikeImpl(h, s, v);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static HSVLike of(float h, float s, float v) {
      return new HSVLikeImpl(h, s, v);
   }

   @NotNull
   static HSVLike fromRGB(@Range(from = 0L,to = 255L) int red, @Range(from = 0L,to = 255L) int green, @Range(from = 0L,to = 255L) int blue) {
      float r = (float)red / 255.0F;
      float g = (float)green / 255.0F;
      float b = (float)blue / 255.0F;
      float min = Math.min(r, Math.min(g, b));
      float max = Math.max(r, Math.max(g, b));
      float delta = max - min;
      float s;
      if (max != 0.0F) {
         s = delta / max;
      } else {
         s = 0.0F;
      }

      if (s == 0.0F) {
         return new HSVLikeImpl(0.0F, s, max);
      } else {
         float h;
         if (r == max) {
            h = (g - b) / delta;
         } else if (g == max) {
            h = 2.0F + (b - r) / delta;
         } else {
            h = 4.0F + (r - g) / delta;
         }

         h *= 60.0F;
         if (h < 0.0F) {
            h += 360.0F;
         }

         return new HSVLikeImpl(h / 360.0F, s, max);
      }
   }

   float h();

   float s();

   float v();

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("h", this.h()), ExaminableProperty.of("s", this.s()), ExaminableProperty.of("v", this.v()));
   }
}
