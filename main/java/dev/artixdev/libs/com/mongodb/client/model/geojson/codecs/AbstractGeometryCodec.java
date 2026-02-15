package dev.artixdev.libs.com.mongodb.client.model.geojson.codecs;

import dev.artixdev.libs.com.mongodb.client.model.geojson.Geometry;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

abstract class AbstractGeometryCodec<T extends Geometry> implements Codec<T> {
   private final CodecRegistry registry;
   private final Class<T> encoderClass;

   AbstractGeometryCodec(CodecRegistry registry, Class<T> encoderClass) {
      this.registry = registry;
      this.encoderClass = encoderClass;
   }

   public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
      GeometryEncoderHelper.encodeGeometry(writer, value, encoderContext, this.registry);
   }

   public T decode(BsonReader reader, DecoderContext decoderContext) {
      return GeometryDecoderHelper.decodeGeometry(reader, this.getEncoderClass());
   }

   public Class<T> getEncoderClass() {
      return this.encoderClass;
   }
}
