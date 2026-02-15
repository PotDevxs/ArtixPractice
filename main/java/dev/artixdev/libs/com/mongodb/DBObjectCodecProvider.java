package dev.artixdev.libs.com.mongodb;

import java.util.Date;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.BsonTypeClassMap;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DateCodec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.types.BSONTimestamp;

public class DBObjectCodecProvider implements CodecProvider {
   private final BsonTypeClassMap bsonTypeClassMap;

   public DBObjectCodecProvider() {
      this(DBObjectCodec.getDefaultBsonTypeClassMap());
   }

   public DBObjectCodecProvider(BsonTypeClassMap bsonTypeClassMap) {
      this.bsonTypeClassMap = (BsonTypeClassMap)Assertions.notNull("bsonTypeClassMap", bsonTypeClassMap);
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      if (clazz == BSONTimestamp.class) {
         return (Codec<T>) new BSONTimestampCodec();
      } else if (DBObject.class.isAssignableFrom(clazz) && !List.class.isAssignableFrom(clazz)) {
         return (Codec<T>) new DBObjectCodec(registry, this.bsonTypeClassMap);
      } else {
         return Date.class.isAssignableFrom(clazz) ? (Codec<T>) new DateCodec() : null;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && this.getClass() == o.getClass();
      }
   }

   public int hashCode() {
      return this.getClass().hashCode();
   }

   public String toString() {
      return "DBObjectCodecProvider{}";
   }
}
