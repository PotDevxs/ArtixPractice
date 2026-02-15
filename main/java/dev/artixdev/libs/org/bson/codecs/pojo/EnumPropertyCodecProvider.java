package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.EnumCodec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class EnumPropertyCodecProvider implements PropertyCodecProvider {
   private final CodecRegistry codecRegistry;

   EnumPropertyCodecProvider(CodecRegistry codecRegistry) {
      this.codecRegistry = codecRegistry;
   }

   public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry propertyCodecRegistry) {
      Class<T> clazz = type.getType();
      if (Enum.class.isAssignableFrom(clazz)) {
         try {
            return this.codecRegistry.get(clazz);
         } catch (CodecConfigurationException ignored) {
            return new EnumCodec(clazz);
         }
      } else {
         return null;
      }
   }
}
