package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class FloatCodec implements Codec<Float> {
   public void encode(BsonWriter writer, Float value, EncoderContext encoderContext) {
      writer.writeDouble((double)value);
   }

   public Float decode(BsonReader reader, DecoderContext decoderContext) {
      double value = NumberCodecHelper.decodeDouble(reader);
      if (!(value < -3.4028234663852886E38D) && !(value > 3.4028234663852886E38D)) {
         return (float)value;
      } else {
         throw new BsonInvalidOperationException(String.format("%s can not be converted into a Float.", value));
      }
   }

   public Class<Float> getEncoderClass() {
      return Float.class;
   }
}
