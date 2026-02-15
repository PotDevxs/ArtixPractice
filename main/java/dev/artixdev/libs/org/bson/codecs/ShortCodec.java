package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class ShortCodec implements Codec<Short> {
   public void encode(BsonWriter writer, Short value, EncoderContext encoderContext) {
      writer.writeInt32(value);
   }

   public Short decode(BsonReader reader, DecoderContext decoderContext) {
      int value = NumberCodecHelper.decodeInt(reader);
      if (value >= -32768 && value <= 32767) {
         return (short)value;
      } else {
         throw new BsonInvalidOperationException(String.format("%s can not be converted into a Short.", value));
      }
   }

   public Class<Short> getEncoderClass() {
      return Short.class;
   }
}
