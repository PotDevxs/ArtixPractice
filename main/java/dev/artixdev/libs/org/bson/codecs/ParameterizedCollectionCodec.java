package dev.artixdev.libs.org.bson.codecs;

import java.util.Collection;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;

class ParameterizedCollectionCodec<T, C extends Collection<T>> extends AbstractCollectionCodec<T, C> {
   private final Codec<T> codec;

   ParameterizedCollectionCodec(Codec<T> codec, Class<C> clazz) {
      super(clazz);
      this.codec = codec;
   }

   T readValue(BsonReader reader, DecoderContext decoderContext) {
      return decoderContext.decodeWithChildContext(this.codec, reader);
   }

   void writeValue(BsonWriter writer, T cur, EncoderContext encoderContext) {
      encoderContext.encodeWithChildContext(this.codec, writer, cur);
   }
}
