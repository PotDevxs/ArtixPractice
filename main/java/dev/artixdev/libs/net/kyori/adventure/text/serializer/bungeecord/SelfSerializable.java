package dev.artixdev.libs.net.kyori.adventure.text.serializer.bungeecord;

import java.io.IOException;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.TypeAdapter;
import dev.artixdev.libs.com.google.gson.TypeAdapterFactory;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.libs.com.google.gson.stream.JsonReader;
import dev.artixdev.libs.com.google.gson.stream.JsonWriter;

interface SelfSerializable {
   void write(JsonWriter var1) throws IOException;

   public static class AdapterFactory implements TypeAdapterFactory {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
         return !SelfSerializable.class.isAssignableFrom(type.getRawType()) ? null : new SelfSerializable.AdapterFactory.SelfSerializableTypeAdapter(type);
      }

      static {
         SelfSerializable.AdapterFactory.SelfSerializableTypeAdapter.class.getName();
      }

      static class SelfSerializableTypeAdapter<T> extends TypeAdapter<T> {
         private final TypeToken<T> type;

         SelfSerializableTypeAdapter(TypeToken<T> type) {
            this.type = type;
         }

         public void write(JsonWriter out, T value) throws IOException {
            ((SelfSerializable)value).write(out);
         }

         public T read(JsonReader in) {
            throw new UnsupportedOperationException("Cannot load values of type " + this.type.getType().getTypeName());
         }
      }
   }
}
