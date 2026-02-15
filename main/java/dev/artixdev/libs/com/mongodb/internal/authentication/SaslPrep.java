package dev.artixdev.libs.com.mongodb.internal.authentication;

import java.nio.CharBuffer;
import java.text.Normalizer;
import java.text.Normalizer.Form;

public final class SaslPrep {
   public static String saslPrepQuery(String str) {
      return saslPrep(str, true);
   }

   public static String saslPrepStored(String str) {
      return saslPrep(str, false);
   }

   private static String saslPrep(String str, boolean allowUnassigned) {
      char[] chars = str.toCharArray();

      int length;
      for(length = 0; length < str.length(); ++length) {
         char ch = str.charAt(length);
         if (nonAsciiSpace(ch)) {
            chars[length] = ' ';
         }
      }

      length = 0;

      for(int i = 0; i < str.length(); ++i) {
         char ch = chars[i];
         if (!mappedToNothing(ch)) {
            chars[length++] = ch;
         }
      }

      String normalized = Normalizer.normalize(CharBuffer.wrap(chars, 0, length), Form.NFKC);
      boolean containsRandALCat = false;
      boolean containsLCat = false;
      boolean initialRandALCat = false;
      int i = 0;

      boolean isRandALcat;
      do {
         if (i >= normalized.length()) {
            if (containsRandALCat && containsLCat) {
               throw new IllegalArgumentException("Contains both RandALCat characters and LCat characters");
            }

            return normalized;
         }

         int codepoint = normalized.codePointAt(i);
         if (prohibited(codepoint)) {
            throw new IllegalArgumentException("Prohibited character at position " + i);
         }

         byte directionality = Character.getDirectionality(codepoint);
         isRandALcat = directionality == 1 || directionality == 2;
         containsRandALCat |= isRandALcat;
         containsLCat |= directionality == 0;
         initialRandALCat |= i == 0 && isRandALcat;
         if (!allowUnassigned && !Character.isDefined(codepoint)) {
            throw new IllegalArgumentException("Character at position " + i + " is unassigned");
         }

         i += Character.charCount(codepoint);
      } while(!initialRandALCat || i < normalized.length() || isRandALcat);

      throw new IllegalArgumentException("First character is RandALCat, but last character is not");
   }

   static boolean prohibited(int codepoint) {
      return nonAsciiSpace((char)codepoint) || asciiControl((char)codepoint) || nonAsciiControl(codepoint) || privateUse(codepoint) || nonCharacterCodePoint(codepoint) || surrogateCodePoint(codepoint) || inappropriateForPlainText(codepoint) || inappropriateForCanonical(codepoint) || changeDisplayProperties(codepoint) || tagging(codepoint);
   }

   private static boolean tagging(int codepoint) {
      return codepoint == 917505 || 917536 <= codepoint && codepoint <= 917631;
   }

   private static boolean changeDisplayProperties(int codepoint) {
      return codepoint == 832 || codepoint == 833 || codepoint == 8206 || codepoint == 8207 || codepoint == 8234 || codepoint == 8235 || codepoint == 8236 || codepoint == 8237 || codepoint == 8238 || codepoint == 8298 || codepoint == 8299 || codepoint == 8300 || codepoint == 8301 || codepoint == 8302 || codepoint == 8303;
   }

   private static boolean inappropriateForCanonical(int codepoint) {
      return 12272 <= codepoint && codepoint <= 12283;
   }

   private static boolean inappropriateForPlainText(int codepoint) {
      return codepoint == 65529 || codepoint == 65530 || codepoint == 65531 || codepoint == 65532 || codepoint == 65533;
   }

   private static boolean surrogateCodePoint(int codepoint) {
      return 55296 <= codepoint && codepoint <= 57343;
   }

   private static boolean nonCharacterCodePoint(int codepoint) {
      return 64976 <= codepoint && codepoint <= 65007 || 65534 <= codepoint && codepoint <= 65535 || 131070 <= codepoint && codepoint <= 131071 || 196606 <= codepoint && codepoint <= 196607 || 262142 <= codepoint && codepoint <= 262143 || 327678 <= codepoint && codepoint <= 327679 || 393214 <= codepoint && codepoint <= 393215 || 458750 <= codepoint && codepoint <= 458751 || 524286 <= codepoint && codepoint <= 524287 || 589822 <= codepoint && codepoint <= 589823 || 655358 <= codepoint && codepoint <= 655359 || 720894 <= codepoint && codepoint <= 720895 || 786430 <= codepoint && codepoint <= 786431 || 851966 <= codepoint && codepoint <= 851967 || 917502 <= codepoint && codepoint <= 917503 || 983038 <= codepoint && codepoint <= 983039 || 1048574 <= codepoint && codepoint <= 1048575 || 1114110 <= codepoint && codepoint <= 1114111;
   }

   private static boolean privateUse(int codepoint) {
      return 57344 <= codepoint && codepoint <= 63743 || 61440 <= codepoint && codepoint <= 1048573 || 1048576 <= codepoint && codepoint <= 1114109;
   }

   private static boolean nonAsciiControl(int codepoint) {
      return 128 <= codepoint && codepoint <= 159 || codepoint == 1757 || codepoint == 1807 || codepoint == 6158 || codepoint == 8204 || codepoint == 8205 || codepoint == 8232 || codepoint == 8233 || codepoint == 8288 || codepoint == 8289 || codepoint == 8290 || codepoint == 8291 || 8298 <= codepoint && codepoint <= 8303 || codepoint == 65279 || 65529 <= codepoint && codepoint <= 65532 || 119155 <= codepoint && codepoint <= 119162;
   }

   private static boolean asciiControl(char ch) {
      return ch <= 31 || ch == 127;
   }

   private static boolean nonAsciiSpace(char ch) {
      return ch == 160 || ch == 5760 || 8192 <= ch && ch <= 8203 || ch == 8239 || ch == 8287 || ch == 12288;
   }

   private static boolean mappedToNothing(char ch) {
      return ch == 173 || ch == 847 || ch == 6150 || ch == 6155 || ch == 6156 || ch == 6157 || ch == 8203 || ch == 8204 || ch == 8205 || ch == 8288 || '︀' <= ch && ch <= '️' || ch == '\ufeff';
   }

   private SaslPrep() {
   }
}
