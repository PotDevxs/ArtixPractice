package dev.artixdev.libs.org.bson.codecs;

import java.util.Iterator;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class BsonTypeCodecMap {
   private final BsonTypeClassMap bsonTypeClassMap;
   private final Codec<?>[] codecs = new Codec[256];

   public BsonTypeCodecMap(BsonTypeClassMap bsonTypeClassMap, CodecRegistry codecRegistry) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
      Assertions.notNull("codecRegistry", codecRegistry);
      Iterator<BsonType> iterator = bsonTypeClassMap.keys().iterator();

      while(iterator.hasNext()) {
         BsonType cur = iterator.next();
         Class<?> clazz = bsonTypeClassMap.get(cur);
         if (clazz != null) {
            try {
               this.codecs[cur.getValue()] = codecRegistry.get(clazz);
            } catch (CodecConfigurationException e) {
            }
         }
      }

   }

   public Codec<?> get(BsonType bsonType) {
      Codec<?> codec = this.codecs[bsonType.getValue()];
      if (codec == null) {
         Class<?> clazz = this.bsonTypeClassMap.get(bsonType);
         if (clazz == null) {
            throw new CodecConfigurationException(String.format("No class mapped for BSON type %s.", bsonType));
         } else {
            throw new CodecConfigurationException(String.format("Can't find a codec for %s.", clazz));
         }
      } else {
         return codec;
      }
   }
}
