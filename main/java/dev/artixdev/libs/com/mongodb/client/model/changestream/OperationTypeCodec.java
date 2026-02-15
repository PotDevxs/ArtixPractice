package dev.artixdev.libs.com.mongodb.client.model.changestream;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;

final class OperationTypeCodec implements Codec<OperationType> {
   public OperationType decode(BsonReader reader, DecoderContext decoderContext) {
      return OperationType.fromString(reader.readString());
   }

   public void encode(BsonWriter writer, OperationType value, EncoderContext encoderContext) {
      writer.writeString(value.getValue());
   }

   public Class<OperationType> getEncoderClass() {
      return OperationType.class;
   }
}
