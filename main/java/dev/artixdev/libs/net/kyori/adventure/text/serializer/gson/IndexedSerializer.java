package dev.artixdev.libs.net.kyori.adventure.text.serializer.gson;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;
import dev.artixdev.libs.net.kyori.adventure.util.Index;

final class IndexedSerializer<E> extends TypeAdapter<E> {
   private final String name;
   private final Index<String, E> map;
   private final boolean throwOnUnknownKey;

   public static <E> TypeAdapter<E> strict(String name, Index<String, E> map) {
      return (new IndexedSerializer(name, map, true)).nullSafe();
   }

   public static <E> TypeAdapter<E> lenient(String name, Index<String, E> map) {
      return (new IndexedSerializer(name, map, false)).nullSafe();
   }

   private IndexedSerializer(String name, Index<String, E> map, boolean throwOnUnknownKey) {
      this.name = name;
      this.map = map;
      this.throwOnUnknownKey = throwOnUnknownKey;
   }

   public void write(JsonWriter out, E value) throws IOException {
      out.value((String)this.map.key(value));
   }

   public E read(JsonReader in) throws IOException {
      String string = in.nextString();
      E value = this.map.value(string);
      if (value != null) {
         return value;
      } else if (this.throwOnUnknownKey) {
         throw new JsonParseException("invalid " + this.name + ":  " + string);
      } else {
         return null;
      }
   }
}
