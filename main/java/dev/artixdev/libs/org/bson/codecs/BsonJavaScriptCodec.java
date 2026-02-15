package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonJavaScript;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonJavaScriptCodec implements Codec<BsonJavaScript> {
   public BsonJavaScript decode(BsonReader reader, DecoderContext decoderContext) {
      return new BsonJavaScript(reader.readJavaScript());
   }

   public void encode(BsonWriter writer, BsonJavaScript value, EncoderContext encoderContext) {
      writer.writeJavaScript(value.getCode());
   }

   public Class<BsonJavaScript> getEncoderClass() {
      return BsonJavaScript.class;
   }
}
