package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWriter;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.conversions.Bson;

final class BuildersHelper {
   static <TItem> void encodeValue(BsonDocumentWriter writer, TItem value, CodecRegistry codecRegistry) {
      if (value == null) {
         writer.writeNull();
      } else if (value instanceof Bson) {
         codecRegistry.get(BsonDocument.class).encode(writer, ((Bson)value).toBsonDocument(BsonDocument.class, codecRegistry), EncoderContext.builder().build());
      } else {
         ((Encoder<TItem>) codecRegistry.get(value.getClass())).encode(writer, value, EncoderContext.builder().build());
      }

   }

   private BuildersHelper() {
   }
}
