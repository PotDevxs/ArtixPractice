package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class EnumCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return Enum.class.isAssignableFrom(clazz) ? new EnumCodec(clazz) : null;
   }

   public String toString() {
      return "EnumCodecProvider{}";
   }
}
