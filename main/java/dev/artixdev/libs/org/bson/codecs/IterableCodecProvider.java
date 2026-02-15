package dev.artixdev.libs.org.bson.codecs;

import java.util.Objects;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class IterableCodecProvider implements CodecProvider {
   private final BsonTypeClassMap bsonTypeClassMap;
   private final Transformer valueTransformer;

   public IterableCodecProvider() {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public IterableCodecProvider(Transformer valueTransformer) {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP, valueTransformer);
   }

   public IterableCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
      this(bsonTypeClassMap, (Transformer)null);
   }

   public IterableCodecProvider(BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
      this.valueTransformer = valueTransformer;
   }

   @SuppressWarnings({"deprecation", "unchecked"})
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (Iterable.class.isAssignableFrom(clazz)) {
         Codec<T> codec = (Codec<T>) new IterableCodec(registry, this.bsonTypeClassMap, this.valueTransformer);
         return codec;
      }
      return null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         IterableCodecProvider that = (IterableCodecProvider)o;
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
      return "IterableCodecProvider{}";
   }
}
