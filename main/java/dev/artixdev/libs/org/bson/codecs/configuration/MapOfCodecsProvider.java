package dev.artixdev.libs.org.bson.codecs.configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import dev.artixdev.libs.org.bson.codecs.Codec;

final class MapOfCodecsProvider implements CodecProvider {
   private final Map<Class<?>, Codec<?>> codecsMap = new HashMap();

   MapOfCodecsProvider(List<? extends Codec<?>> codecsList) {
      Iterator<? extends Codec<?>> iterator = codecsList.iterator();

      while(iterator.hasNext()) {
         Codec<?> codec = iterator.next();
         this.codecsMap.put(codec.getEncoderClass(), codec);
      }

   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return (Codec)this.codecsMap.get(clazz);
   }

   public String toString() {
      return "MapOfCodecsProvider{codecsMap=" + this.codecsMap + '}';
   }
}
