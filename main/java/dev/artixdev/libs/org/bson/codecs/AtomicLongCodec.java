package dev.artixdev.libs.org.bson.codecs;

import java.util.concurrent.atomic.AtomicLong;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class AtomicLongCodec implements Codec<AtomicLong> {
   public void encode(BsonWriter writer, AtomicLong value, EncoderContext encoderContext) {
      writer.writeInt64(value.longValue());
   }

   public AtomicLong decode(BsonReader reader, DecoderContext decoderContext) {
      return new AtomicLong(NumberCodecHelper.decodeLong(reader));
   }

   public Class<AtomicLong> getEncoderClass() {
      return AtomicLong.class;
   }
}
