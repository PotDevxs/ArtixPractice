package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.key.Key;

final class KeySerializer extends TypeAdapter<Key> {
   static final TypeAdapter<Key> INSTANCE = (new KeySerializer()).nullSafe();

   private KeySerializer() {
   }

   public void write(JsonWriter out, Key value) throws IOException {
      out.value(value.asString());
   }

   public Key read(JsonReader in) throws IOException {
      return Key.key(in.nextString());
   }
}
