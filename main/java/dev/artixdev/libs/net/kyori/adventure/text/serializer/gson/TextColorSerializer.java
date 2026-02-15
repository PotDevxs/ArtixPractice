package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import java.util.Locale;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class TextColorSerializer extends TypeAdapter<TextColor> {
   static final TypeAdapter<TextColor> INSTANCE = (new TextColorSerializer(false)).nullSafe();
   static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR = (new TextColorSerializer(true)).nullSafe();
   private final boolean downsampleColor;

   private TextColorSerializer(boolean downsampleColor) {
      this.downsampleColor = downsampleColor;
   }

   public void write(JsonWriter out, TextColor value) throws IOException {
      if (value instanceof NamedTextColor) {
         out.value((String)NamedTextColor.NAMES.key((NamedTextColor)value));
      } else if (this.downsampleColor) {
         out.value((String)NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
      } else {
         out.value(asUpperCaseHexString(value));
      }

   }

   private static String asUpperCaseHexString(TextColor color) {
      return String.format(Locale.ROOT, "%c%06X", '#', color.value());
   }

   @Nullable
   public TextColor read(JsonReader in) throws IOException {
      TextColor color = fromString(in.nextString());
      if (color == null) {
         return null;
      } else {
         return (TextColor)(this.downsampleColor ? NamedTextColor.nearestTo(color) : color);
      }
   }

   @Nullable
   static TextColor fromString(@NotNull String value) {
      return value.startsWith("#") ? TextColor.fromHexString(value) : (TextColor)NamedTextColor.NAMES.value(value);
   }
}
