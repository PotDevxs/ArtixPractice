package dev.artixdev.libs.org.bson.internal;

import java.lang.reflect.Type;
import java.util.List;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class LazyCodec<T> implements Codec<T> {
   private final CodecRegistry registry;
   private final Class<T> clazz;
   private final List<Type> types;
   private volatile Codec<T> wrapped;

   LazyCodec(CodecRegistry registry, Class<T> clazz, List<Type> types) {
      this.registry = registry;
      this.clazz = clazz;
      this.types = types;
   }

   public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
      this.getWrapped().encode(writer, value, encoderContext);
   }

   public Class<T> getEncoderClass() {
      return this.clazz;
   }

   public T decode(BsonReader reader, DecoderContext decoderContext) {
      return this.getWrapped().decode(reader, decoderContext);
   }

   private Codec<T> getWrapped() {
      if (this.wrapped == null) {
         if (this.types == null) {
            this.wrapped = this.registry.get(this.clazz);
         } else {
            this.wrapped = this.registry.get(this.clazz, this.types);
         }
      }

      return this.wrapped;
   }
}
