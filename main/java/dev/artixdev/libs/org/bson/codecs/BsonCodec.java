package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

public class BsonCodec implements Codec<Bson> {
   private static final Codec<BsonDocument> BSON_DOCUMENT_CODEC = new BsonDocumentCodec();
   private final CodecRegistry registry;

   public BsonCodec(CodecRegistry registry) {
      this.registry = registry;
   }

   public Bson decode(BsonReader reader, DecoderContext decoderContext) {
      throw new UnsupportedOperationException("The BsonCodec can only encode to Bson");
   }

   public void encode(BsonWriter writer, Bson value, EncoderContext encoderContext) {
      try {
         BsonDocument bsonDocument = value.toBsonDocument(BsonDocument.class, this.registry);
         BSON_DOCUMENT_CODEC.encode(writer, bsonDocument, encoderContext);
      } catch (Exception exception) {
         throw new CodecConfigurationException(String.format("Unable to encode a Bson implementation: %s", value), exception);
      }
   }

   public Class<Bson> getEncoderClass() {
      return Bson.class;
   }
}
