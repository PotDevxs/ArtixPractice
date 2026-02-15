package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;

final class TextDecorationSerializer {
   static final TypeAdapter<TextDecoration> INSTANCE;

   private TextDecorationSerializer() {
   }

   static {
      INSTANCE = IndexedSerializer.strict("text decoration", TextDecoration.NAMES);
   }
}
