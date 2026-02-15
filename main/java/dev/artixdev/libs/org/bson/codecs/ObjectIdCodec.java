package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class ObjectIdCodec implements Codec<ObjectId> {
   public void encode(BsonWriter writer, ObjectId value, EncoderContext encoderContext) {
      writer.writeObjectId(value);
   }

   public ObjectId decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readObjectId();
   }

   public Class<ObjectId> getEncoderClass() {
      return ObjectId.class;
   }
}
