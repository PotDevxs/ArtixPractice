package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonUndefined;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonUndefinedCodec implements Codec<BsonUndefined> {
   public BsonUndefined decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readUndefined();
      return new BsonUndefined();
   }

   public void encode(BsonWriter writer, BsonUndefined value, EncoderContext encoderContext) {
      writer.writeUndefined();
   }

   public Class<BsonUndefined> getEncoderClass() {
      return BsonUndefined.class;
   }
}
