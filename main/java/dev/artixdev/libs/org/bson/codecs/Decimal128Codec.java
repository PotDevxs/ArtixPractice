package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.Decimal128;

public final class Decimal128Codec implements Codec<Decimal128> {
   public Decimal128 decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readDecimal128();
   }

   public void encode(BsonWriter writer, Decimal128 value, EncoderContext encoderContext) {
      writer.writeDecimal128(value);
   }

   public Class<Decimal128> getEncoderClass() {
      return Decimal128.class;
   }
}
