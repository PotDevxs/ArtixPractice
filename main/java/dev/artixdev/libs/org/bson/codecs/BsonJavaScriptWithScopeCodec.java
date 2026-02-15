package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonJavaScriptWithScope;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

public class BsonJavaScriptWithScopeCodec implements Codec<BsonJavaScriptWithScope> {
   private final Codec<BsonDocument> documentCodec;

   public BsonJavaScriptWithScopeCodec(Codec<BsonDocument> documentCodec) {
      this.documentCodec = documentCodec;
   }

   public BsonJavaScriptWithScope decode(BsonReader bsonReader, DecoderContext decoderContext) {
      String code = bsonReader.readJavaScriptWithScope();
      BsonDocument scope = (BsonDocument)this.documentCodec.decode(bsonReader, decoderContext);
      return new BsonJavaScriptWithScope(code, scope);
   }

   public void encode(BsonWriter writer, BsonJavaScriptWithScope codeWithScope, EncoderContext encoderContext) {
      writer.writeJavaScriptWithScope(codeWithScope.getCode());
      this.documentCodec.encode(writer, codeWithScope.getScope(), encoderContext);
   }

   public Class<BsonJavaScriptWithScope> getEncoderClass() {
      return BsonJavaScriptWithScope.class;
   }
}
