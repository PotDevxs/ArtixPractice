package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.Symbol;

public class SymbolCodec implements Codec<Symbol> {
   public Symbol decode(BsonReader reader, DecoderContext decoderContext) {
      return new Symbol(reader.readSymbol());
   }

   public void encode(BsonWriter writer, Symbol value, EncoderContext encoderContext) {
      writer.writeSymbol(value.getSymbol());
   }

   public Class<Symbol> getEncoderClass() {
      return Symbol.class;
   }
}
