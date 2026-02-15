package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public class DBRefCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return clazz == DBRef.class ? (Codec<T>) new DBRefCodec(registry) : null;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o != null && this.getClass() == o.getClass();
      }
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "DBRefCodecProvider{}";
   }
}
