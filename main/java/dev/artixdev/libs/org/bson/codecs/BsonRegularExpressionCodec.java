package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonRegularExpression;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonRegularExpressionCodec implements Codec<BsonRegularExpression> {
   public BsonRegularExpression decode(BsonReader reader, DecoderContext decoderContext) {
      return reader.readRegularExpression();
   }

   public void encode(BsonWriter writer, BsonRegularExpression value, EncoderContext encoderContext) {
      writer.writeRegularExpression(value);
   }

   public Class<BsonRegularExpression> getEncoderClass() {
      return BsonRegularExpression.class;
   }
}
