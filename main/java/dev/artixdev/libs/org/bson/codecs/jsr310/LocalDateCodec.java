package dev.artixdev.libs.org.bson.codecs.jsr310;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

public class LocalDateCodec extends DateTimeBasedCodec<LocalDate> {
   public LocalDate decode(BsonReader reader, DecoderContext decoderContext) {
      return Instant.ofEpochMilli(this.validateAndReadDateTime(reader)).atZone(ZoneOffset.UTC).toLocalDate();
   }

   public void encode(BsonWriter writer, LocalDate value, EncoderContext encoderContext) {
      try {
         writer.writeDateTime(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
      } catch (ArithmeticException e) {
         throw new CodecConfigurationException(String.format("Unsupported LocalDate '%s' could not be converted to milliseconds: %s", value, e.getMessage()), e);
      }
   }

   public Class<LocalDate> getEncoderClass() {
      return LocalDate.class;
   }
}
