package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class MapPropertyCodecProvider implements PropertyCodecProvider {
   public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry registry) {
      if (Map.class.isAssignableFrom(type.getType()) && type.getTypeParameters().size() == 2) {
         Class<?> keyType = ((TypeWithTypeParameters)type.getTypeParameters().get(0)).getType();
         if (!keyType.equals(String.class)) {
            throw new CodecConfigurationException(String.format("Invalid Map type. Maps MUST have string keys, found %s instead.", keyType));
         } else {
            try {
               return new MapPropertyCodecProvider.MapCodec(type.getType(), registry.get((TypeWithTypeParameters)type.getTypeParameters().get(1)));
            } catch (CodecConfigurationException e) {
               if (((TypeWithTypeParameters)type.getTypeParameters().get(1)).getType() == Object.class) {
                  try {
                     @SuppressWarnings("unchecked")
                     Codec<T> fallback = (Codec<T>) registry.get(TypeData.builder(Map.class).build());
                     return fallback;
                  } catch (CodecConfigurationException ignored) {
                  }
               }
               throw e;
            }
         }
      } else {
         return null;
      }
   }

   private static class MapCodec<T> implements Codec<Map<String, T>> {
      private final Class<Map<String, T>> encoderClass;
      private final Codec<T> codec;

      MapCodec(Class<Map<String, T>> encoderClass, Codec<T> codec) {
         this.encoderClass = encoderClass;
         this.codec = codec;
      }

      public void encode(BsonWriter writer, Map<String, T> map, EncoderContext encoderContext) {
         writer.writeStartDocument();
         for (Entry<String, T> entry : map.entrySet()) {
            writer.writeName(entry.getKey());
            if (entry.getValue() == null) {
               writer.writeNull();
            } else {
               this.codec.encode(writer, entry.getValue(), encoderContext);
            }
         }

         writer.writeEndDocument();
      }

      public Map<String, T> decode(BsonReader reader, DecoderContext context) {
         reader.readStartDocument();
         Map<String, T> map = this.getInstance();
         while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (reader.getCurrentBsonType() == BsonType.NULL) {
               reader.readNull();
               map.put(name, null);
            } else {
               map.put(name, this.codec.decode(reader, context));
            }
         }
         reader.readEndDocument();
         return map;
      }

      public Class<Map<String, T>> getEncoderClass() {
         return this.encoderClass;
      }

      @SuppressWarnings("unchecked")
      private Map<String, T> getInstance() {
         if (this.encoderClass.isInterface()) {
            return (Map<String, T>) (Map<?, ?>) new HashMap<String, T>();
         }
         try {
            return (Map<String, T>) this.encoderClass.getDeclaredConstructor().newInstance();
         } catch (Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
         }
      }
   }
}
