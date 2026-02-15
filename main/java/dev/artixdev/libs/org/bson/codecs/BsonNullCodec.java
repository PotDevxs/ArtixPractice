package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonNull;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonNullCodec implements Codec<BsonNull> {
   public BsonNull decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readNull();
      return BsonNull.VALUE;
   }

   public void encode(BsonWriter writer, BsonNull value, EncoderContext encoderContext) {
      writer.writeNull();
   }

   public Class<BsonNull> getEncoderClass() {
      return BsonNull.class;
   }
}
