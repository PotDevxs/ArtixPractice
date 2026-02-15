package dev.artixdev.libs.org.bson.codecs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.UUID;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class ContainerCodecHelper {
   static Object readValue(BsonReader reader, DecoderContext decoderContext, BsonTypeCodecMap bsonTypeCodecMap, UuidRepresentation uuidRepresentation, CodecRegistry registry, Transformer valueTransformer) {
      BsonType bsonType = reader.getCurrentBsonType();
      if (bsonType == BsonType.NULL) {
         reader.readNull();
         return null;
      } else {
         Codec<?> codec = bsonTypeCodecMap.get(bsonType);
         if (bsonType == BsonType.BINARY && reader.peekBinarySize() == 16) {
            switch(reader.peekBinarySubType()) {
            case 3:
               if (uuidRepresentation == UuidRepresentation.JAVA_LEGACY || uuidRepresentation == UuidRepresentation.C_SHARP_LEGACY || uuidRepresentation == UuidRepresentation.PYTHON_LEGACY) {
                  codec = registry.get(UUID.class);
               }
               break;
            case 4:
               if (uuidRepresentation == UuidRepresentation.STANDARD) {
                  codec = registry.get(UUID.class);
               }
            }
         }

         return valueTransformer.transform(codec.decode(reader, decoderContext));
      }
   }

   static Codec<?> getCodec(CodecRegistry codecRegistry, Type type) {
      if (type instanceof Class) {
         return codecRegistry.get((Class)type);
      } else if (type instanceof ParameterizedType) {
         ParameterizedType parameterizedType = (ParameterizedType)type;
         return codecRegistry.get((Class)parameterizedType.getRawType(), Arrays.asList(parameterizedType.getActualTypeArguments()));
      } else {
         throw new CodecConfigurationException("Unsupported generic type of container: " + type);
      }
   }

   private ContainerCodecHelper() {
   }
}
