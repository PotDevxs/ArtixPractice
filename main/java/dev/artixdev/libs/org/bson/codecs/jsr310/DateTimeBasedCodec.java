package dev.artixdev.libs.org.bson.codecs.jsr310;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

abstract class DateTimeBasedCodec<T> implements Codec<T> {
   long validateAndReadDateTime(BsonReader reader) {
      BsonType currentType = reader.getCurrentBsonType();
      if (!currentType.equals(BsonType.DATE_TIME)) {
         throw new CodecConfigurationException(String.format("Could not decode into %s, expected '%s' BsonType but got '%s'.", this.getEncoderClass().getSimpleName(), BsonType.DATE_TIME, currentType));
      } else {
         return reader.readDateTime();
      }
   }
}
