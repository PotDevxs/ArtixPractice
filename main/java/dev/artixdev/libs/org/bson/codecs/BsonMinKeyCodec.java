package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonMinKey;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonMinKeyCodec implements Codec<BsonMinKey> {
   public void encode(BsonWriter writer, BsonMinKey value, EncoderContext encoderContext) {
      writer.writeMinKey();
   }

   public BsonMinKey decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readMinKey();
      return new BsonMinKey();
   }

   public Class<BsonMinKey> getEncoderClass() {
      return BsonMinKey.class;
   }
}
