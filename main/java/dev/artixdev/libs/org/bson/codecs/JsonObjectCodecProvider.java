package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.json.JsonObject;

public final class JsonObjectCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (clazz.equals(JsonObject.class)) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new JsonObjectCodec();
         return codec;
      }
      return null;
   }

   public String toString() {
      return "JsonObjectCodecProvider{}";
   }
}
