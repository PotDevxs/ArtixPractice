package dev.artixdev.libs.org.bson.codecs.jsr310;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;

public class LocalTimeCodec extends DateTimeBasedCodec<LocalTime> {
   public LocalTime decode(BsonReader reader, DecoderContext decoderContext) {
      return Instant.ofEpochMilli(this.validateAndReadDateTime(reader)).atOffset(ZoneOffset.UTC).toLocalTime();
   }

   public void encode(BsonWriter writer, LocalTime value, EncoderContext encoderContext) {
      writer.writeDateTime(value.atDate(LocalDate.ofEpochDay(0L)).toInstant(ZoneOffset.UTC).toEpochMilli());
   }

   public Class<LocalTime> getEncoderClass() {
      return LocalTime.class;
   }
}
