package dev.artixdev.libs.org.bson.codecs;

import java.util.Objects;
import dev.artixdev.libs.org.bson.Document;
import dev.artixdev.libs.org.bson.Transformer;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.types.CodeWithScope;

public class DocumentCodecProvider implements CodecProvider {
   private final BsonTypeClassMap bsonTypeClassMap;
   private final Transformer valueTransformer;

   public DocumentCodecProvider() {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP);
   }

   public DocumentCodecProvider(Transformer valueTransformer) {
      this(BsonTypeClassMap.DEFAULT_BSON_TYPE_CLASS_MAP, valueTransformer);
   }

   public DocumentCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
      this(bsonTypeClassMap, (Transformer)null);
   }

   public DocumentCodecProvider(BsonTypeClassMap bsonTypeClassMap, Transformer valueTransformer) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
      this.valueTransformer = valueTransformer;
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (clazz == CodeWithScope.class) {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) new CodeWithScopeCodec(registry.get(Document.class));
         return codec;
      } else {
         if (clazz == Document.class) {
            @SuppressWarnings("unchecked")
            Codec<T> codec = (Codec<T>) new DocumentCodec(registry, this.bsonTypeClassMap, this.valueTransformer);
            return codec;
         }
         return null;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         DocumentCodecProvider that = (DocumentCodecProvider)o;
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
      return "DocumentCodecProvider{}";
   }
}
