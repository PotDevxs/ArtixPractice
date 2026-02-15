package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSyntaxException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextDecoration;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class TextColorWrapper {
   @Nullable
   final TextColor color;
   @Nullable
   final TextDecoration decoration;
   final boolean reset;

   TextColorWrapper(@Nullable TextColor color, @Nullable TextDecoration decoration, boolean reset) {
      this.color = color;
      this.decoration = decoration;
      this.reset = reset;
   }

   static final class Serializer extends TypeAdapter<TextColorWrapper> {
      static final TextColorWrapper.Serializer INSTANCE = new TextColorWrapper.Serializer();

      private Serializer() {
      }

      public void write(JsonWriter out, TextColorWrapper value) {
         throw new JsonSyntaxException("Cannot write TextColorWrapper instances");
      }

      public TextColorWrapper read(JsonReader in) throws IOException {
         String input = in.nextString();
         TextColor color = TextColorSerializer.fromString(input);
         TextDecoration decoration = (TextDecoration)TextDecoration.NAMES.value(input);
         boolean reset = decoration == null && input.equals("reset");
         if (color == null && decoration == null && !reset) {
            throw new JsonParseException("Don't know how to parse " + input + " at " + in.getPath());
         } else {
            return new TextColorWrapper(color, decoration, reset);
         }
      }
   }
}
