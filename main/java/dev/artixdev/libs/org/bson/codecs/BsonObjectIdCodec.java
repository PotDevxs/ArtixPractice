package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonObjectId;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonObjectIdCodec implements Codec<BsonObjectId> {
   public void encode(BsonWriter writer, BsonObjectId value, EncoderContext encoderContext) {
      writer.writeObjectId(value.getValue());
   }

   public BsonObjectId decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonObjectId(reader.readObjectId());
   }

   public Class<BsonObjectId> getEncoderClass() {
      return BsonObjectId.class;
   }
}
