package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class FallbackPropertyCodecProvider implements PropertyCodecProvider {
   private final CodecRegistry codecRegistry;
   private final PojoCodec<?> pojoCodec;

   FallbackPropertyCodecProvider(PojoCodec<?> pojoCodec, CodecRegistry codecRegistry) {
      this.pojoCodec = pojoCodec;
      this.codecRegistry = codecRegistry;
   }

   public <S> Codec<S> get(TypeWithTypeParameters<S> type, PropertyCodecRegistry propertyCodecRegistry) {
      Class<S> clazz = type.getType();
      return (Codec)(clazz == this.pojoCodec.getEncoderClass() ? this.pojoCodec : this.codecRegistry.get(type.getType()));
   }
}
