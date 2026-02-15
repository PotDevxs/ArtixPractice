package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class BsonValueCodec implements Codec<BsonValue> {
   private final CodecRegistry codecRegistry;

   public BsonValueCodec() {
      this(CodecRegistries.fromProviders(new BsonValueCodecProvider()));
   }

   public BsonValueCodec(CodecRegistry codecRegistry) {
      this.codecRegistry = codecRegistry;
   }

   public BsonValue decode(BsonReader reader, DecoderContext decoderContext) {
      return (BsonValue)this.codecRegistry.get(BsonValueCodecProvider.getClassForBsonType(reader.getCurrentBsonType())).decode(reader, decoderContext);
   }

   public void encode(BsonWriter writer, BsonValue value, EncoderContext encoderContext) {
      Codec codec = this.codecRegistry.get(value.getClass());
      encoderContext.encodeWithChildContext(codec, writer, value);
   }

   public Class<BsonValue> getEncoderClass() {
      return BsonValue.class;
   }
}
