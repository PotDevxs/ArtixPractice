package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.MinKey;

public class MinKeyCodec implements Codec<MinKey> {
   public void encode(BsonWriter writer, MinKey value, EncoderContext encoderContext) {
      writer.writeMinKey();
   }

   public MinKey decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readMinKey();
      return new MinKey();
   }

   public Class<MinKey> getEncoderClass() {
      return MinKey.class;
   }
}
