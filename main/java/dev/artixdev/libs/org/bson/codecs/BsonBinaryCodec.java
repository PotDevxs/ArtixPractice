package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonBinaryCodec implements Codec<BsonBinary> {
   public void encode(BsonWriter writer, BsonBinary value, EncoderContext encoderContext) {
      writer.writeBinaryData(value);
   }

   public BsonBinary decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readBinaryData();
   }

   public Class<BsonBinary> getEncoderClass() {
      return BsonBinary.class;
   }
}
