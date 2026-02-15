package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class AutomaticPojoCodec<T> extends PojoCodec<T> {
   private final PojoCodec<T> pojoCodec;

   AutomaticPojoCodec(PojoCodec<T> pojoCodec) {
      this.pojoCodec = pojoCodec;
   }

   public T decode(BsonReader reader, DecoderContext decoderContext) {
      try {
         return this.pojoCodec.decode(reader, decoderContext);
      } catch (CodecConfigurationException e) {
         throw new CodecConfigurationException(String.format("An exception occurred when decoding using the AutomaticPojoCodec.%nDecoding into a '%s' failed with the following exception:%n%n%s%n%nA custom Codec or PojoCodec may need to be explicitly configured and registered to handle this type.", this.pojoCodec.getEncoderClass().getSimpleName(), e.getMessage()), e);
      }
   }

   public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
      try {
         this.pojoCodec.encode(writer, value, encoderContext);
      } catch (CodecConfigurationException e) {
         throw new CodecConfigurationException(String.format("An exception occurred when encoding using the AutomaticPojoCodec.%nEncoding a %s: '%s' failed with the following exception:%n%n%s%n%nA custom Codec or PojoCodec may need to be explicitly configured and registered to handle this type.", this.getEncoderClass().getSimpleName(), value, e.getMessage()), e);
      }
   }

   public Class<T> getEncoderClass() {
      return this.pojoCodec.getEncoderClass();
   }

   ClassModel<T> getClassModel() {
      return this.pojoCodec.getClassModel();
   }

   DiscriminatorLookup getDiscriminatorLookup() {
      return this.pojoCodec.getDiscriminatorLookup();
   }
}
