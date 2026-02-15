package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BooleanCodec implements Codec<Boolean> {
   public void encode(BsonWriter writer, Boolean value, EncoderContext encoderContext) {
      writer.writeBoolean(value);
   }

   public Boolean decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readBoolean();
   }

   public Class<Boolean> getEncoderClass() {
      return Boolean.class;
   }
}
