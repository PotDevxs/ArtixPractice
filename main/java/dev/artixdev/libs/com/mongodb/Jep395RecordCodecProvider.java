package dev.artixdev.libs.com.mongodb;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.internal.ProvidersCodecRegistry;

public class Jep395RecordCodecProvider implements CodecProvider {
   @Nullable
   private static final CodecProvider RECORD_CODEC_PROVIDER;

   @Nullable
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   @Nullable
   public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
      return RECORD_CODEC_PROVIDER != null ? ProvidersCodecRegistry.getFromCodecProvider(RECORD_CODEC_PROVIDER, clazz, typeArguments, registry) : null;
   }

   public boolean hasRecordSupport() {
      return RECORD_CODEC_PROVIDER != null;
   }

   static {
      CodecProvider possibleCodecProvider;
      try {
         Class.forName("java.lang.Record");
         Class<?> recordCodecProviderClass = Class.forName("dev.artixdev.libs.org.bson.codecs.record.RecordCodecProvider");
         possibleCodecProvider = (CodecProvider) recordCodecProviderClass.getConstructor().newInstance();
      } catch (UnsupportedClassVersionError | ClassNotFoundException e) {
         possibleCodecProvider = null;
      } catch (ReflectiveOperationException e) {
         possibleCodecProvider = null;
      }

      RECORD_CODEC_PROVIDER = possibleCodecProvider;
   }
}
