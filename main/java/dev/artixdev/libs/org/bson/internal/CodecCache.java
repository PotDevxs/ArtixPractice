package dev.artixdev.libs.org.bson.internal;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.Codec;

final class CodecCache {
   private final ConcurrentMap<CodecCache.CodecCacheKey, Codec<?>> codecCache = new ConcurrentHashMap();

   public <T> Codec<T> putIfAbsent(CodecCache.CodecCacheKey codecCacheKey, Codec<T> codec) {
      Assertions.assertNotNull(codec);
      Codec<T> prevCodec = (Codec)this.codecCache.putIfAbsent(codecCacheKey, codec);
      return prevCodec == null ? codec : prevCodec;
   }

   public <T> Optional<Codec<T>> get(CodecCache.CodecCacheKey codecCacheKey) {
      Codec<T> codec = (Codec)this.codecCache.get(codecCacheKey);
      return Optional.ofNullable(codec);
   }

   static final class CodecCacheKey {
      private final Class<?> clazz;
      private final List<Type> types;

      CodecCacheKey(Class<?> clazz, List<Type> types) {
         this.clazz = clazz;
         this.types = types;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            CodecCache.CodecCacheKey that = (CodecCache.CodecCacheKey)o;
            return this.clazz.equals(that.clazz) && Objects.equals(this.types, that.types);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.clazz, this.types});
      }

      public String toString() {
         return "CodecCacheKey{clazz=" + this.clazz + ", types=" + this.types + '}';
      }
   }
}
