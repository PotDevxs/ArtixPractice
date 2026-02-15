package dev.artixdev.libs.org.bson.codecs.jsr310;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

public class LocalDateTimeCodec extends DateTimeBasedCodec<LocalDateTime> {
   public LocalDateTime decode(BsonReader reader, DecoderContext decoderContext) {
      return Instant.ofEpochMilli(this.validateAndReadDateTime(reader)).atZone(ZoneOffset.UTC).toLocalDateTime();
   }

   public void encode(BsonWriter writer, LocalDateTime value, EncoderContext encoderContext) {
      try {
         writer.writeDateTime(value.toInstant(ZoneOffset.UTC).toEpochMilli());
      } catch (ArithmeticException e) {
         throw new CodecConfigurationException(String.format("Unsupported LocalDateTime value '%s' could not be converted to milliseconds: %s", value, e.getMessage()), e);
      }
   }

   public Class<LocalDateTime> getEncoderClass() {
      return LocalDateTime.class;
   }
}
