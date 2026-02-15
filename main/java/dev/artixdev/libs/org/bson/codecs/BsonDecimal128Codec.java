package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDecimal128;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonDecimal128Codec implements Codec<BsonDecimal128> {
   public BsonDecimal128 decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonDecimal128(reader.readDecimal128());
   }

   public void encode(BsonWriter writer, BsonDecimal128 value, EncoderContext encoderContext) {
      writer.writeDecimal128(value.getValue());
   }

   public Class<BsonDecimal128> getEncoderClass() {
      return BsonDecimal128.class;
   }
}
