package dev.artixdev.libs.org.bson.codecs.jsr310;

import java.time.Instant;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

public class InstantCodec extends DateTimeBasedCodec<Instant> {
   public Instant decode(BsonReader reader, DecoderContext decoderContext) {
      return Instant.ofEpochMilli(this.validateAndReadDateTime(reader));
   }

   public void encode(BsonWriter writer, Instant value, EncoderContext encoderContext) {
      try {
         writer.writeDateTime(value.toEpochMilli());
      } catch (ArithmeticException e) {
         throw new CodecConfigurationException(String.format("Unsupported Instant value '%s' could not be converted to milliseconds: %s", value, e.getMessage()), e);
      }
   }

   public Class<Instant> getEncoderClass() {
      return Instant.class;
   }
}
