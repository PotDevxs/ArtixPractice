package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.types.Code;

public class CodeCodec implements Codec<Code> {
   public void encode(BsonWriter writer, Code value, EncoderContext encoderContext) {
      writer.writeJavaScript(value.getCode());
   }

   public Code decode(BsonReader bsonReader, DecoderContext decoderContext) {
      return new Code(bsonReader.readJavaScript());
   }

   public Class<Code> getEncoderClass() {
      return Code.class;
   }
}
