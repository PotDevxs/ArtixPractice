package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDateTime;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonDateTimeCodec implements Codec<BsonDateTime> {
   public BsonDateTime decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonDateTime(reader.readDateTime());
   }

   public void encode(BsonWriter writer, BsonDateTime value, EncoderContext encoderContext) {
      writer.writeDateTime(value.getValue());
   }

   public Class<BsonDateTime> getEncoderClass() {
      return BsonDateTime.class;
   }
}
