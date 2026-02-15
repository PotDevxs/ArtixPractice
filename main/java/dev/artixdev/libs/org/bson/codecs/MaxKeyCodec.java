package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.MaxKey;

public class MaxKeyCodec implements Codec<MaxKey> {
   public void encode(BsonWriter writer, MaxKey value, EncoderContext encoderContext) {
      writer.writeMaxKey();
   }

   public MaxKey decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readMaxKey();
      return new MaxKey();
   }

   public Class<MaxKey> getEncoderClass() {
      return MaxKey.class;
   }
}
