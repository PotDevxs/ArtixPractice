package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.types.CodeWithScope;

public class CodeWithScopeCodec implements Codec<CodeWithScope> {
   private final Codec<Document> documentCodec;

   public CodeWithScopeCodec(Codec<Document> documentCodec) {
      this.documentCodec = documentCodec;
   }

   public CodeWithScope decode(BsonReader bsonReader, DecoderContext decoderContext) {
      String code = bsonReader.readJavaScriptWithScope();
      Document scope = (Document)this.documentCodec.decode(bsonReader, decoderContext);
      return new CodeWithScope(code, scope);
   }

   public void encode(BsonWriter writer, CodeWithScope codeWithScope, EncoderContext encoderContext) {
      writer.writeJavaScriptWithScope(codeWithScope.getCode());
      this.documentCodec.encode(writer, codeWithScope.getScope(), encoderContext);
   }

   public Class<CodeWithScope> getEncoderClass() {
      return CodeWithScope.class;
   }
}
