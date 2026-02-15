package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonTimestampCodec implements Codec<BsonTimestamp> {
   public void encode(BsonWriter writer, BsonTimestamp value, EncoderContext encoderContext) {
      writer.writeTimestamp(value);
   }

   public BsonTimestamp decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readTimestamp();
   }

   public Class<BsonTimestamp> getEncoderClass() {
      return BsonTimestamp.class;
   }
}
