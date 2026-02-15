package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonTimestamp;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.types.BSONTimestamp;

public class BSONTimestampCodec implements Codec<BSONTimestamp> {
   public void encode(BsonWriter writer, BSONTimestamp value, EncoderContext encoderContext) {
      writer.writeTimestamp(new BsonTimestamp(value.getTime(), value.getInc()));
   }

   public BSONTimestamp decode(BsonReader reader, DecoderContext decoderContext) {
      BsonTimestamp timestamp = reader.readTimestamp();
      return new BSONTimestamp(timestamp.getTime(), timestamp.getInc());
   }

   public Class<BSONTimestamp> getEncoderClass() {
      return BSONTimestamp.class;
   }
}
