package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayList;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonArrayCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class CommandResultArrayCodec<T> extends BsonArrayCodec {
   private final Decoder<T> decoder;

   CommandResultArrayCodec(CodecRegistry registry, Decoder<T> decoder) {
      super(registry);
      this.decoder = decoder;
   }

   public BsonArray decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readStartArray();
      ArrayList list = new ArrayList();

      while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         if (reader.getCurrentBsonType() == BsonType.NULL) {
            reader.readNull();
            list.add((Object)null);
         } else {
            list.add(this.decoder.decode(reader, decoderContext));
         }
      }

      reader.readEndArray();
      return new BsonArrayWrapper(list);
   }

   protected BsonValue readValue(BsonReader reader, DecoderContext decoderContext) {
      return (BsonValue)(reader.getCurrentBsonType() == BsonType.DOCUMENT ? new BsonDocumentWrapper(this.decoder.decode(reader, decoderContext), (Encoder)null) : super.readValue(reader, decoderContext));
   }
}
