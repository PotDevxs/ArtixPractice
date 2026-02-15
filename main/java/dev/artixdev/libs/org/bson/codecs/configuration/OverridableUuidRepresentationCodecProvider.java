package dev.artixdev.libs.org.bson.codecs.configuration;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.OverridableUuidRepresentationCodec;
import dev.artixdev.libs.org.bson.internal.ProvidersCodecRegistry;

final class OverridableUuidRepresentationCodecProvider implements CodecProvider {
   private final CodecProvider wrapped;
   private final UuidRepresentation uuidRepresentation;

   OverridableUuidRepresentationCodecProvider(CodecProvider wrapped, UuidRepresentation uuidRepresentation) {
      this.uuidRepresentation = (UuidRepresentation)Assertions.notNull("uuidRepresentation", uuidRepresentation);
      this.wrapped = (CodecProvider)Assertions.notNull("wrapped", wrapped);
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      Codec<T> codec = ProvidersCodecRegistry.getFromCodecProvider(this.wrapped, clazz, typeArguments, registry);
      if (codec instanceof OverridableUuidRepresentationCodec) {
         Codec<T> codecWithUuidRepresentation = ((OverridableUuidRepresentationCodec)codec).withUuidRepresentation(this.uuidRepresentation);
         codec = codecWithUuidRepresentation;
      }

      return codec;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         OverridableUuidRepresentationCodecProvider that = (OverridableUuidRepresentationCodecProvider)o;
         if (!this.wrapped.equals(that.wrapped)) {
            return false;
         } else {
            return this.uuidRepresentation == that.uuidRepresentation;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.wrapped.hashCode();
      result = 31 * result + this.uuidRepresentation.hashCode();
      return result;
   }

   public String toString() {
      return "OverridableUuidRepresentationCodecRegistry{wrapped=" + this.wrapped + ", uuidRepresentation=" + this.uuidRepresentation + '}';
   }
}
