package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistries;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class CommandResultDocumentCodec<T> extends BsonDocumentCodec {
   private final Decoder<T> payloadDecoder;
   private final List<String> fieldsContainingPayload;

   CommandResultDocumentCodec(CodecRegistry registry, Decoder<T> payloadDecoder, List<String> fieldsContainingPayload) {
      super(registry);
      this.payloadDecoder = payloadDecoder;
      this.fieldsContainingPayload = fieldsContainingPayload;
   }

   static <P> Codec<BsonDocument> create(Decoder<P> decoder, String fieldContainingPayload) {
      return create(decoder, Collections.singletonList(fieldContainingPayload));
   }

   static <P> Codec<BsonDocument> create(Decoder<P> decoder, List<String> fieldsContainingPayload) {
      CodecRegistry registry = CodecRegistries.fromProviders(new CommandResultCodecProvider(decoder, fieldsContainingPayload));
      return registry.get(BsonDocument.class);
   }

   protected BsonValue readValue(BsonReader reader, DecoderContext decoderContext) {
      if (this.fieldsContainingPayload.contains(reader.getCurrentName())) {
         if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
            return new BsonDocumentWrapper(this.payloadDecoder.decode(reader, decoderContext), (Encoder)null);
         }

         if (reader.getCurrentBsonType() == BsonType.ARRAY) {
            return (new CommandResultArrayCodec(this.getCodecRegistry(), this.payloadDecoder)).decode(reader, decoderContext);
         }
      }

      return super.readValue(reader, decoderContext);
   }
}
