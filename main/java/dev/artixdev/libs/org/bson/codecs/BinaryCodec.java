package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.Binary;

public class BinaryCodec implements Codec<Binary> {
   public void encode(BsonWriter writer, Binary value, EncoderContext encoderContext) {
      writer.writeBinaryData(new BsonBinary(value.getType(), value.getData()));
   }

   public Binary decode(BsonReader reader, DecoderContext decoderContext) {
      BsonBinary bsonBinary = reader.readBinaryData();
      return new Binary(bsonBinary.getType(), bsonBinary.getData());
   }

   public Class<Binary> getEncoderClass() {
      return Binary.class;
   }
}
