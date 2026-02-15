package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonSymbol;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonSymbolCodec implements Codec<BsonSymbol> {
   public BsonSymbol decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonSymbol(reader.readSymbol());
   }

   public void encode(BsonWriter writer, BsonSymbol value, EncoderContext encoderContext) {
      writer.writeSymbol(value.getSymbol());
   }

   public Class<BsonSymbol> getEncoderClass() {
      return BsonSymbol.class;
   }
}
