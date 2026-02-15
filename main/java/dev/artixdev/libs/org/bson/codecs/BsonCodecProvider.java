package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class BsonCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (Bson.class.isAssignableFrom(clazz)) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new BsonCodec(registry);
         return codec;
      }
      return null;
   }

   public String toString() {
      return "BsonCodecProvider{}";
   }
}
