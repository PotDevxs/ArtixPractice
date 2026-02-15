package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class ByteCodec implements Codec<Byte> {
   public void encode(BsonWriter writer, Byte value, EncoderContext encoderContext) {
      writer.writeInt32(value);
   }

   public Byte decode(BsonReader reader, DecoderContext decoderContext) {
      int value = NumberCodecHelper.decodeInt(reader);
      if (value >= -128 && value <= 127) {
         return (byte)value;
      } else {
         throw new BsonInvalidOperationException(String.format("%s can not be converted into a Byte.", value));
      }
   }

   public Class<Byte> getEncoderClass() {
      return Byte.class;
   }
}
