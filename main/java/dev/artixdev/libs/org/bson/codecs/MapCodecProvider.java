package dev.artixdev.libs.org.bson.codecs;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class MapCodecProvider implements CodecProvider {
   private final BsonTypeClassMap bsonTypeClassMap;
   private final Transformer valueTransformer;

   public MapCodecProvider() {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public MapCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
      this(bsonTypeClassMap, (Transformer)null);
   }

   public MapCodecProvider(Transformer valueTransformer) {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP, valueTransformer);
   }

   public MapCodecProvider(BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
      this.valueTransformer = valueTransformer;
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      if (Map.class.isAssignableFrom(clazz)) {
         int typeArgumentsSize = typeArguments.size();
         switch(typeArgumentsSize) {
         case 0:
            return new MapCodecV2(registry, this.bsonTypeClassMap, this.valueTransformer, clazz);
         case 2:
            Type genericTypeOfMapKey = (Type)typeArguments.get(0);
            if (!genericTypeOfMapKey.getTypeName().equals("java.lang.String")) {
               throw new CodecConfigurationException("Unsupported key type for Map: " + genericTypeOfMapKey.getTypeName());
            }

            return new ParameterizedMapCodec(ContainerCodecHelper.getCodec(registry, (Type)typeArguments.get(1)), clazz);
         default:
            throw new CodecConfigurationException("Expected two parameterized type for an Iterable, but found " + typeArgumentsSize);
         }
      } else {
         return null;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MapCodecProvider that = (MapCodecProvider)o;
         if (!this.bsonTypeClassMap.equals(that.bsonTypeClassMap)) {
            return false;
         } else {
            return Objects.equals(this.valueTransformer, that.valueTransformer);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.bsonTypeClassMap.hashCode();
      result = 31 * result + (this.valueTransformer != null ? this.valueTransformer.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "MapCodecProvider{}";
   }
}
