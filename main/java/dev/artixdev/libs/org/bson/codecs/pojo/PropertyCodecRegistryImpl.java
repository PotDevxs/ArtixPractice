package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class PropertyCodecRegistryImpl implements PropertyCodecRegistry {
   private final List<PropertyCodecProvider> propertyCodecProviders;
   private final ConcurrentHashMap<TypeWithTypeParameters<?>, Codec<?>> propertyCodecCache;

   PropertyCodecRegistryImpl(PojoCodec<?> pojoCodec, CodecRegistry codecRegistry, List<PropertyCodecProvider> propertyCodecProviders) {
      List<PropertyCodecProvider> augmentedProviders = new ArrayList();
      if (propertyCodecProviders != null) {
         augmentedProviders.addAll(propertyCodecProviders);
      }

      augmentedProviders.add(new CollectionPropertyCodecProvider());
      augmentedProviders.add(new MapPropertyCodecProvider());
      augmentedProviders.add(new EnumPropertyCodecProvider(codecRegistry));
      augmentedProviders.add(new FallbackPropertyCodecProvider(pojoCodec, codecRegistry));
      this.propertyCodecProviders = augmentedProviders;
      this.propertyCodecCache = new ConcurrentHashMap();
   }

   public <S> Codec<S> get(TypeWithTypeParameters<S> typeWithTypeParameters) {
      if (this.propertyCodecCache.containsKey(typeWithTypeParameters)) {
         return (Codec)this.propertyCodecCache.get(typeWithTypeParameters);
      } else {
         Iterator var2 = this.propertyCodecProviders.iterator();

         Codec codec;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            PropertyCodecProvider propertyCodecProvider = (PropertyCodecProvider)var2.next();
            codec = propertyCodecProvider.get(typeWithTypeParameters, this);
         } while(codec == null);

         this.propertyCodecCache.put(typeWithTypeParameters, codec);
         return codec;
      }
   }
}
