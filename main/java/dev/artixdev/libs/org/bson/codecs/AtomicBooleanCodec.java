package dev.artixdev.libs.org.bson.codecs;

import java.util.concurrent.atomic.AtomicBoolean;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class AtomicBooleanCodec implements Codec<AtomicBoolean> {
   public void encode(BsonWriter writer, AtomicBoolean value, EncoderContext encoderContext) {
      writer.writeBoolean(value.get());
   }

   public AtomicBoolean decode(BsonReader reader, DecoderContext decoderContext) {
      return new AtomicBoolean(reader.readBoolean());
   }

   public Class<AtomicBoolean> getEncoderClass() {
      return AtomicBoolean.class;
   }
}
