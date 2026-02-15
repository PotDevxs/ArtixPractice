package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class LongCodec implements Codec<Long> {
   public void encode(BsonWriter writer, Long value, EncoderContext encoderContext) {
      writer.writeInt64(value);
   }

   public Long decode(BsonReader reader, DecoderContext decoderContext) {
      return NumberCodecHelper.decodeLong(reader);
   }

   public Class<Long> getEncoderClass() {
      return Long.class;
   }
}
