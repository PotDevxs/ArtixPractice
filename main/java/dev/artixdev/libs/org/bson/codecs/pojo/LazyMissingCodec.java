package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

class LazyMissingCodec<S> implements Codec<S> {
   private final Class<S> clazz;
   private final CodecConfigurationException exception;

   LazyMissingCodec(Class<S> clazz, CodecConfigurationException exception) {
      this.clazz = clazz;
      this.exception = exception;
   }

   public S decode(BsonReader reader, DecoderContext decoderContext) {
      throw this.exception;
   }

   public void encode(BsonWriter writer, S value, EncoderContext encoderContext) {
      throw this.exception;
   }

   public Class<S> getEncoderClass() {
      return this.clazz;
   }
}
