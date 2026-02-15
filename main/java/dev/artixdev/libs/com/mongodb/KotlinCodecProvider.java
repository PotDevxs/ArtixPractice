package dev.artixdev.libs.com.mongodb;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.internal.ProvidersCodecRegistry;

public class KotlinCodecProvider implements CodecProvider {
   @Nullable
   private static final CodecProvider KOTLIN_SERIALIZABLE_CODEC_PROVIDER;
   @Nullable
   private static final CodecProvider DATA_CLASS_CODEC_PROVIDER;

   @Nullable
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   @Nullable
   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      Codec<T> codec = null;
      if (KOTLIN_SERIALIZABLE_CODEC_PROVIDER != null) {
         codec = ProvidersCodecRegistry.getFromCodecProvider(KOTLIN_SERIALIZABLE_CODEC_PROVIDER, clazz, typeArguments, registry);
      }

      if (codec == null && DATA_CLASS_CODEC_PROVIDER != null) {
         codec = ProvidersCodecRegistry.getFromCodecProvider(DATA_CLASS_CODEC_PROVIDER, clazz, typeArguments, registry);
      }

      return codec;
   }

   static {
      CodecProvider kotlinSerializerProvider = null;
      try {
         Class<?> clazz = Class.forName("dev.artixdev.libs.org.bson.codecs.kotlinx.KotlinSerializerCodecProvider");
         kotlinSerializerProvider = (CodecProvider) clazz.getConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
      }

      KOTLIN_SERIALIZABLE_CODEC_PROVIDER = kotlinSerializerProvider;

      CodecProvider dataClassProvider = null;
      try {
         Class<?> clazz = Class.forName("dev.artixdev.libs.org.bson.codecs.kotlin.DataClassCodecProvider");
         dataClassProvider = (CodecProvider) clazz.getConstructor().newInstance();
      } catch (ReflectiveOperationException e) {
      }

      DATA_CLASS_CODEC_PROVIDER = dataClassProvider;
   }
}
