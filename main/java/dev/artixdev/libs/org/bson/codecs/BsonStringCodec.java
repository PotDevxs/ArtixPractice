package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonStringCodec implements Codec<BsonString> {
   public BsonString decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonString(reader.readString());
   }

   public void encode(BsonWriter writer, BsonString value, EncoderContext encoderContext) {
      writer.writeString(value.getValue());
   }

   public Class<BsonString> getEncoderClass() {
      return BsonString.class;
   }
}
