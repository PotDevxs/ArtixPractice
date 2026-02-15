package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonBooleanCodec implements Codec<BsonBoolean> {
   public BsonBoolean decode(BsonReader reader, DecoderContext decoderContext) {
      boolean value = reader.readBoolean();
      return BsonBoolean.valueOf(value);
   }

   public void encode(BsonWriter writer, BsonBoolean value, EncoderContext encoderContext) {
      writer.writeBoolean(value.getValue());
   }

   public Class<BsonBoolean> getEncoderClass() {
      return BsonBoolean.class;
   }
}
