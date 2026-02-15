package dev.artixdev.libs.org.bson.codecs;

import java.util.Date;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class DateCodec implements Codec<Date> {
   public void encode(BsonWriter writer, Date value, EncoderContext encoderContext) {
      writer.writeDateTime(value.getTime());
   }

   public Date decode(BsonReader reader, DecoderContext decoderContext) {
      return new Date(reader.readDateTime());
   }

   public Class<Date> getEncoderClass() {
      return Date.class;
   }
}
