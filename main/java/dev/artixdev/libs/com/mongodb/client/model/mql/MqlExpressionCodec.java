package dev.artixdev.libs.com.mongodb.client.model.mql;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class MqlExpressionCodec implements Codec<MqlExpression> {
   private final CodecRegistry codecRegistry;

   MqlExpressionCodec(CodecRegistry codecRegistry) {
      this.codecRegistry = codecRegistry;
   }

   public MqlExpression decode(BsonReader reader, DecoderContext decoderContext) {
      throw new UnsupportedOperationException("Decoding to an MqlExpression is not supported");
   }

   public void encode(BsonWriter writer, MqlExpression value, EncoderContext encoderContext) {
      BsonValue bsonValue = value.toBsonValue(this.codecRegistry);
      Codec codec = this.codecRegistry.get(bsonValue.getClass());
      codec.encode(writer, bsonValue, encoderContext);
   }

   public Class<MqlExpression> getEncoderClass() {
      return MqlExpression.class;
   }
}
