package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.util.HSVLike;
import dev.artixdev.libs.net.kyori.adventure.util.RGBLike;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.Range;

public interface TextColor extends Comparable<TextColor>, StyleBuilderApplicable, TextFormat, RGBLike, Examinable {
   char HEX_CHARACTER = '#';
   String HEX_PREFIX = "#";

   @NotNull
   static TextColor color(int value) {
      int truncatedValue = value & 16777215;
      NamedTextColor named = NamedTextColor.namedColor(truncatedValue);
      return (TextColor)(named != null ? named : new TextColorImpl(truncatedValue));
   }

   @NotNull
   static TextColor color(@NotNull RGBLike rgb) {
      return rgb instanceof TextColor ? (TextColor)rgb : color(rgb.red(), rgb.green(), rgb.blue());
   }

   @NotNull
   static TextColor color(@NotNull HSVLike hsv) {
      float s = hsv.s();
      float v = hsv.v();
      if (s == 0.0F) {
         return color(v, v, v);
      } else {
         float h = hsv.h() * 6.0F;
         int i = (int)Math.floor((double)h);
         float f = h - (float)i;
         float p = v * (1.0F - s);
         float q = v * (1.0F - s * f);
         float t = v * (1.0F - s * (1.0F - f));
         if (i == 0) {
            return color(v, t, p);
         } else if (i == 1) {
            return color(q, v, p);
         } else if (i == 2) {
            return color(p, v, t);
         } else if (i == 3) {
            return color(p, q, v);
         } else {
            return i == 4 ? color(t, p, v) : color(v, p, q);
         }
      }
   }

   @NotNull
   static TextColor color(@Range(from = 0L,to = 255L) int r, @Range(from = 0L,to = 255L) int g, @Range(from = 0L,to = 255L) int b) {
      return color((r & 255) << 16 | (g & 255) << 8 | b & 255);
   }

   @NotNull
   static TextColor color(float r, float g, float b) {
      return color((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F));
   }

   @Nullable
   static TextColor fromHexString(@NotNull String string) {
      if (string.startsWith("#")) {
         try {
            int hex = Integer.parseInt(string.substring(1), 16);
            return color(hex);
         } catch (NumberFormatException ignored) {
            return null;
         }
      } else {
         return null;
      }
   }

   @Nullable
   static TextColor fromCSSHexString(@NotNull String string) {
      if (string.startsWith("#")) {
         String hexString = string.substring(1);
         if (hexString.length() != 3 && hexString.length() != 6) {
            return null;
         } else {
            int hex;
            try {
               hex = Integer.parseInt(hexString, 16);
            } catch (NumberFormatException ignored) {
               return null;
            }

            if (hexString.length() == 6) {
               return color(hex);
            } else {
               int red = (hex & 3840) >> 8 | (hex & 3840) >> 4;
               int green = (hex & 240) >> 4 | hex & 240;
               int blue = (hex & 15) << 4 | hex & 15;
               return color(red, green, blue);
            }
         }
      } else {
         return null;
      }
   }

   int value();

   @NotNull
   default String asHexString() {
      return String.format("%c%06x", '#', this.value());
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int red() {
      return this.value() >> 16 & 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int green() {
      return this.value() >> 8 & 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   default int blue() {
      return this.value() & 255;
   }

   @NotNull
   static TextColor lerp(float t, @NotNull RGBLike a, @NotNull RGBLike b) {
      float clampedT = Math.min(1.0F, Math.max(0.0F, t));
      int ar = a.red();
      int br = b.red();
      int ag = a.green();
      int bg = b.green();
      int ab = a.blue();
      int bb = b.blue();
      return color(Math.round((float)ar + clampedT * (float)(br - ar)), Math.round((float)ag + clampedT * (float)(bg - ag)), Math.round((float)ab + clampedT * (float)(bb - ab)));
   }

   @NotNull
   static <C extends TextColor> C nearestColorTo(@NotNull List<C> values, @NotNull TextColor any) {
      Objects.requireNonNull(any, "color");
      float matchedDistance = Float.MAX_VALUE;
      @SuppressWarnings("unchecked")
      C match = (C) values.get(0);
      int i = 0;

      for(int length = values.size(); i < length; ++i) {
         @SuppressWarnings("unchecked")
         C potential = (C) values.get(i);
         float distance = TextColorImpl.distance(any.asHSV(), potential.asHSV());
         if (distance < matchedDistance) {
            match = potential;
            matchedDistance = distance;
         }

         if (distance == 0.0F) {
            break;
         }
      }

      return match;
   }

   default void styleApply(@NotNull Style.Builder style) {
      style.color(this);
   }

   default int compareTo(TextColor that) {
      return Integer.compare(this.value(), that.value());
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.asHexString()));
   }
}
