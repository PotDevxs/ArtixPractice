package dev.artixdev.libs.com.github.retrooper.packetevents.util;

public final class LegacyFormat {
   private LegacyFormat() {
   }

   public static String trimLegacyFormat(String text, int length) {
      if (text.length() <= length) {
         return text;
      } else {
         return text.charAt(length - 1) == 167 ? text.substring(0, length - 1) : text.substring(0, length);
      }
   }
}
