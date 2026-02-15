package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonMaxKey;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonMaxKeyCodec implements Codec<BsonMaxKey> {
   public void encode(BsonWriter writer, BsonMaxKey value, EncoderContext encoderContext) {
      writer.writeMaxKey();
   }

   public BsonMaxKey decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readMaxKey();
      return new BsonMaxKey();
   }

   public Class<BsonMaxKey> getEncoderClass() {
      return BsonMaxKey.class;
   }
}
