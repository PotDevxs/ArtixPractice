package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class ByteArrayCodec implements Codec<byte[]> {
   public void encode(BsonWriter writer, byte[] value, EncoderContext encoderContext) {
      writer.writeBinaryData(new BsonBinary(value));
   }

   public byte[] decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readBinaryData().getData();
   }

   public Class<byte[]> getEncoderClass() {
      return byte[].class;
   }
}
