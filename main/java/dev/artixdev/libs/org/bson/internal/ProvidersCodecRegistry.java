package dev.artixdev.libs.org.bson.internal;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.Parameterizable;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class ProvidersCodecRegistry implements CycleDetectingCodecRegistry {
   private final List<CodecProvider> codecProviders;
   private final CodecCache codecCache = new CodecCache();

   public ProvidersCodecRegistry(List<? extends CodecProvider> codecProviders) {
      Assertions.isTrueArgument("codecProviders must not be null or empty", codecProviders != null && codecProviders.size() > 0);
      this.codecProviders = new ArrayList(codecProviders);
   }

   public <T> Codec<T> get(Class<T> clazz) {
      return this.get(new ChildCodecRegistry(this, clazz, (List)null));
   }

   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments) {
      Assertions.notNull("typeArguments", typeArguments);
      Assertions.isTrueArgument(String.format("typeArguments size should equal the number of type parameters in class %s, but is %d", clazz, typeArguments.size()), clazz.getTypeParameters().length == typeArguments.size());
      return this.get(new ChildCodecRegistry(this, clazz, typeArguments));
   }

   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      Iterator<? extends CodecProvider> iterator = this.codecProviders.iterator();

      Codec codec;
      do {
         if (!iterator.hasNext()) {
            return null;
         }

         CodecProvider provider = iterator.next();
         codec = getFromCodecProvider(provider, clazz, typeArguments, registry);
      } while(codec == null);

      return codec;
   }

   public <T> Codec<T> get(ChildCodecRegistry<T> context) {
      CodecCache.CodecCacheKey codecCacheKey = new CodecCache.CodecCacheKey(context.getCodecClass(), (List)context.getTypes().orElse(null));
      return (Codec)this.codecCache.get(codecCacheKey).orElseGet(() -> {
         Iterator<? extends CodecProvider> iterator = this.codecProviders.iterator();

         Codec codec;
         do {
            if (!iterator.hasNext()) {
               throw new CodecConfigurationException(String.format("Can't find a codec for %s.", codecCacheKey));
            }

            CodecProvider provider = iterator.next();
            codec = getFromCodecProvider(provider, context.getCodecClass(), (List)context.getTypes().orElse(Collections.emptyList()), context);
         } while(codec == null);

         return this.codecCache.putIfAbsent(codecCacheKey, codec);
      });
   }

   @Nullable
   public static <T> Codec<T> getFromCodecProvider(CodecProvider provider, Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      Codec<T> codec = provider.get(clazz, typeArguments, registry);
      if (codec instanceof Parameterizable && !typeArguments.isEmpty()) {
         @SuppressWarnings({"unchecked", "deprecation"})
         Codec<T> parameterizedCodec = (Codec<T>) ((Parameterizable)codec).parameterize(registry, typeArguments);
         codec = parameterizedCodec;
      }

      return codec;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ProvidersCodecRegistry that = (ProvidersCodecRegistry)o;
         if (this.codecProviders.size() != that.codecProviders.size()) {
            return false;
         } else {
            for(int i = 0; i < this.codecProviders.size(); ++i) {
               if (((CodecProvider)this.codecProviders.get(i)).getClass() != ((CodecProvider)that.codecProviders.get(i)).getClass()) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.codecProviders.hashCode();
   }

   public String toString() {
      return "ProvidersCodecRegistry{codecProviders=" + this.codecProviders + '}';
   }
}
