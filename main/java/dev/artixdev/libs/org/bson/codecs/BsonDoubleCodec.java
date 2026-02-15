package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDouble;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonDoubleCodec implements Codec<BsonDouble> {
   public BsonDouble decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonDouble(reader.readDouble());
   }

   public void encode(BsonWriter writer, BsonDouble value, EncoderContext encoderContext) {
      writer.writeDouble(value.getValue());
   }

   public Class<BsonDouble> getEncoderClass() {
      return BsonDouble.class;
   }
}
