package dev.artixdev.libs.org.bson.codecs;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class CollectionCodecProvider implements CodecProvider {
   private final BsonTypeClassMap bsonTypeClassMap;
   private final Transformer valueTransformer;

   public CollectionCodecProvider() {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public CollectionCodecProvider(Transformer valueTransformer) {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP, valueTransformer);
   }

   public CollectionCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
      this(bsonTypeClassMap, (Transformer)null);
   }

   public CollectionCodecProvider(BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
      this.valueTransformer = valueTransformer;
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      if (Collection.class.isAssignableFrom(clazz)) {
         int typeArgumentsSize = typeArguments.size();
         switch(typeArgumentsSize) {
         case 0:
            return new CollectionCodec(registry, this.bsonTypeClassMap, this.valueTransformer, clazz);
         case 1:
            return new ParameterizedCollectionCodec(ContainerCodecHelper.getCodec(registry, (Type)typeArguments.get(0)), clazz);
         default:
            throw new CodecConfigurationException("Expected only one type argument for a Collection, but found " + typeArgumentsSize);
         }
      } else {
         return null;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CollectionCodecProvider that = (CollectionCodecProvider)o;
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
      return Objects.hash(new Object[]{this.bsonTypeClassMap, this.valueTransformer});
   }

   public String toString() {
      return "CollectionCodecProvider{}";
   }
}
