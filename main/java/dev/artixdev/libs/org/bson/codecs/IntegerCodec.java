package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class IntegerCodec implements Codec<Integer> {
   public void encode(BsonWriter writer, Integer value, EncoderContext encoderContext) {
      writer.writeInt32(value);
   }

   public Integer decode(BsonReader reader, DecoderContext decoderContext) {
      return NumberCodecHelper.decodeInt(reader);
   }

   public Class<Integer> getEncoderClass() {
      return Integer.class;
   }
}
