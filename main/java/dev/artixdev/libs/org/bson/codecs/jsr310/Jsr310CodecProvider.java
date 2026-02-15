package dev.artixdev.libs.org.bson.codecs.jsr310;

import java.util.HashMap;
import java.util.Map;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class Jsr310CodecProvider implements CodecProvider {
   private static final Map<Class<?>, Codec<?>> JSR310_CODEC_MAP = new HashMap();

   private static void putCodec(Codec<?> codec) {
      JSR310_CODEC_MAP.put(codec.getEncoderClass(), codec);
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return (Codec)JSR310_CODEC_MAP.get(clazz);
   }

   public String toString() {
      return "Jsr310CodecProvider{}";
   }

   static {
      putCodec(new InstantCodec());
      putCodec(new LocalDateCodec());
      putCodec(new LocalDateTimeCodec());
      putCodec(new LocalTimeCodec());
   }
}
