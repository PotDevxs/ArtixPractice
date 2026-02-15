package dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.legacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextFormat;

final class CharacterAndFormatSet {
   static final CharacterAndFormatSet DEFAULT = of(CharacterAndFormat.defaults());
   final List<TextFormat> formats;
   final List<TextColor> colors;
   final String characters;

   static CharacterAndFormatSet of(List<CharacterAndFormat> pairs) {
      int size = pairs.size();
      List<TextColor> colors = new ArrayList();
      List<TextFormat> formats = new ArrayList(size);
      StringBuilder characters = new StringBuilder(size);

      for(int i = 0; i < size; ++i) {
         CharacterAndFormat pair = (CharacterAndFormat)pairs.get(i);
         characters.append(pair.character());
         TextFormat format = pair.format();
         formats.add(format);
         if (format instanceof TextColor) {
            colors.add((TextColor)format);
         }
      }

      if (formats.size() != characters.length()) {
         throw new IllegalStateException("formats length differs from characters length");
      } else {
         return new CharacterAndFormatSet(Collections.unmodifiableList(formats), Collections.unmodifiableList(colors), characters.toString());
      }
   }

   CharacterAndFormatSet(List<TextFormat> formats, List<TextColor> colors, String characters) {
      this.formats = formats;
      this.colors = colors;
      this.characters = characters;
   }
}
