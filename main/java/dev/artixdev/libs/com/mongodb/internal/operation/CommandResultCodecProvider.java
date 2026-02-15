package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonArrayCodec;
import dev.artixdev.libs.org.bson.codecs.BsonBinaryCodec;
import dev.artixdev.libs.org.bson.codecs.BsonBooleanCodec;
import dev.artixdev.libs.org.bson.codecs.BsonDBPointerCodec;
import dev.artixdev.libs.org.bson.codecs.BsonDateTimeCodec;
import dev.artixdev.libs.org.bson.codecs.BsonDecimal128Codec;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.BsonDoubleCodec;
import dev.artixdev.libs.org.bson.codecs.BsonInt32Codec;
import dev.artixdev.libs.org.bson.codecs.BsonInt64Codec;
import dev.artixdev.libs.org.bson.codecs.BsonJavaScriptCodec;
import dev.artixdev.libs.org.bson.codecs.BsonJavaScriptWithScopeCodec;
import dev.artixdev.libs.org.bson.codecs.BsonMaxKeyCodec;
import dev.artixdev.libs.org.bson.codecs.BsonMinKeyCodec;
import dev.artixdev.libs.org.bson.codecs.BsonNullCodec;
import dev.artixdev.libs.org.bson.codecs.BsonObjectIdCodec;
import dev.artixdev.libs.org.bson.codecs.BsonRegularExpressionCodec;
import dev.artixdev.libs.org.bson.codecs.BsonStringCodec;
import dev.artixdev.libs.org.bson.codecs.BsonSymbolCodec;
import dev.artixdev.libs.org.bson.codecs.BsonTimestampCodec;
import dev.artixdev.libs.org.bson.codecs.BsonUndefinedCodec;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class CommandResultCodecProvider<P> implements CodecProvider {
   private final Map<Class<?>, Codec<?>> codecs = new HashMap();
   private final Decoder<P> payloadDecoder;
   private final List<String> fieldsContainingPayload;

   CommandResultCodecProvider(Decoder<P> payloadDecoder, List<String> fieldContainingPayload) {
      this.payloadDecoder = payloadDecoder;
      this.fieldsContainingPayload = fieldContainingPayload;
      this.addCodecs();
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (this.codecs.containsKey(clazz)) {
         return (Codec)this.codecs.get(clazz);
      } else if (clazz == BsonArray.class) {
         return (Codec<T>) new BsonArrayCodec(registry);
      } else {
         return clazz == BsonDocument.class ? (Codec<T>) new CommandResultDocumentCodec(registry, this.payloadDecoder, this.fieldsContainingPayload) : null;
      }
   }

   private void addCodecs() {
      this.addCodec(new BsonNullCodec());
      this.addCodec(new BsonBinaryCodec());
      this.addCodec(new BsonBooleanCodec());
      this.addCodec(new BsonDateTimeCodec());
      this.addCodec(new BsonDBPointerCodec());
      this.addCodec(new BsonDoubleCodec());
      this.addCodec(new BsonInt32Codec());
      this.addCodec(new BsonInt64Codec());
      this.addCodec(new BsonDecimal128Codec());
      this.addCodec(new BsonMinKeyCodec());
      this.addCodec(new BsonMaxKeyCodec());
      this.addCodec(new BsonJavaScriptCodec());
      this.addCodec(new BsonObjectIdCodec());
      this.addCodec(new BsonRegularExpressionCodec());
      this.addCodec(new BsonStringCodec());
      this.addCodec(new BsonSymbolCodec());
      this.addCodec(new BsonTimestampCodec());
      this.addCodec(new BsonUndefinedCodec());
      this.addCodec(new BsonJavaScriptWithScopeCodec(new BsonDocumentCodec()));
   }

   private <T extends BsonValue> void addCodec(Codec<T> codec) {
      this.codecs.put(codec.getEncoderClass(), codec);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CommandResultCodecProvider<?> that = (CommandResultCodecProvider)o;
         if (!this.fieldsContainingPayload.equals(that.fieldsContainingPayload)) {
            return false;
         } else {
            return this.payloadDecoder.getClass().equals(that.payloadDecoder.getClass());
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.payloadDecoder.getClass().hashCode();
      result = 31 * result + this.fieldsContainingPayload.hashCode();
      return result;
   }
}
