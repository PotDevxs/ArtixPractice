package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class DoubleCodec implements Codec<Double> {
   public void encode(BsonWriter writer, Double value, EncoderContext encoderContext) {
      writer.writeDouble(value);
   }

   public Double decode(BsonReader reader, DecoderContext decoderContext) {
      return NumberCodecHelper.decodeDouble(reader);
   }

   public Class<Double> getEncoderClass() {
      return Double.class;
   }
}
