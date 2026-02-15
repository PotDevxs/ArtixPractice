package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDbPointer;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonDBPointerCodec implements Codec<BsonDbPointer> {
   public BsonDbPointer decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readDBPointer();
   }

   public void encode(BsonWriter writer, BsonDbPointer value, EncoderContext encoderContext) {
      writer.writeDBPointer(value);
   }

   public Class<BsonDbPointer> getEncoderClass() {
      return BsonDbPointer.class;
   }
}
