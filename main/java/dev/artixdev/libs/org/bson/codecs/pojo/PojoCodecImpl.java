package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentReader;
import dev.artixdev.libs.org.bson.BsonDocumentWrapper;
import dev.artixdev.libs.org.bson.BsonInvalidOperationException;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonReaderMark;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodec;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;
import dev.artixdev.libs.org.bson.diagnostics.Logger;
import dev.artixdev.libs.org.bson.diagnostics.Loggers;

final class PojoCodecImpl<T> extends PojoCodec<T> {
   private static final Logger LOGGER = Loggers.getLogger("PojoCodec");
   private static final Codec<BsonValue> BSON_VALUE_CODEC = new BsonValueCodec();
   private final ClassModel<T> classModel;
   private final CodecRegistry registry;
   private final PropertyCodecRegistry propertyCodecRegistry;
   private final DiscriminatorLookup discriminatorLookup;

   PojoCodecImpl(ClassModel<T> classModel, CodecRegistry codecRegistry, List<PropertyCodecProvider> propertyCodecProviders, DiscriminatorLookup discriminatorLookup) {
      this.classModel = classModel;
      this.registry = codecRegistry;
      this.discriminatorLookup = discriminatorLookup;
      this.propertyCodecRegistry = new PropertyCodecRegistryImpl(this, this.registry, propertyCodecProviders);
      this.specialize();
   }

   PojoCodecImpl(ClassModel<T> classModel, CodecRegistry codecRegistry, PropertyCodecRegistry propertyCodecRegistry, DiscriminatorLookup discriminatorLookup) {
      this.classModel = classModel;
      this.registry = codecRegistry;
      this.discriminatorLookup = discriminatorLookup;
      this.propertyCodecRegistry = propertyCodecRegistry;
      this.specialize();
   }

   public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
      if (this.areEquivalentTypes(value.getClass(), this.classModel.getType())) {
         writer.writeStartDocument();
         this.encodeIdProperty(writer, value, encoderContext, this.classModel.getIdPropertyModelHolder());
         if (this.classModel.useDiscriminator()) {
            writer.writeString(this.classModel.getDiscriminatorKey(), this.classModel.getDiscriminator());
         }

         for (PropertyModel<?> propertyModel : this.classModel.getPropertyModels()) {
            if (!propertyModel.equals(this.classModel.getIdPropertyModel())) {
               this.encodeProperty(writer, value, encoderContext, propertyModel);
            }
         }

         writer.writeEndDocument();
      } else {
         @SuppressWarnings("unchecked")
         Codec<T> codec = (Codec<T>) this.registry.get(value.getClass());
         codec.encode(writer, value, encoderContext);
      }

   }

   public T decode(BsonReader reader, DecoderContext decoderContext) {
      if (decoderContext.hasCheckedDiscriminator()) {
         InstanceCreator<T> instanceCreator = this.classModel.getInstanceCreator();
         this.decodeProperties(reader, decoderContext, instanceCreator);
         return instanceCreator.getInstance();
      } else {
         return this.getCodecFromDocument(reader, this.classModel.useDiscriminator(), this.classModel.getDiscriminatorKey(), this.registry, this.discriminatorLookup, this).decode(reader, DecoderContext.builder().checkedDiscriminator(true).build());
      }
   }

   public Class<T> getEncoderClass() {
      return this.classModel.getType();
   }

   public String toString() {
      return String.format("PojoCodec<%s>", this.classModel);
   }

   ClassModel<T> getClassModel() {
      return this.classModel;
   }

   private <S> void encodeIdProperty(BsonWriter writer, T instance, EncoderContext encoderContext, IdPropertyModelHolder<S> propertyModelHolder) {
      if (propertyModelHolder.getPropertyModel() != null) {
         if (propertyModelHolder.getIdGenerator() == null) {
            this.encodeProperty(writer, instance, encoderContext, propertyModelHolder.getPropertyModel());
         } else {
            S id = propertyModelHolder.getPropertyModel().getPropertyAccessor().get(instance);
            if (id == null && encoderContext.isEncodingCollectibleDocument()) {
               id = propertyModelHolder.getIdGenerator().generate();

               try {
                  propertyModelHolder.getPropertyModel().getPropertyAccessor().set(instance, id);
               } catch (Exception ignored) {
               }
            }

            this.encodeValue(writer, encoderContext, propertyModelHolder.getPropertyModel(), id);
         }
      }

   }

   private <S> void encodeProperty(BsonWriter writer, T instance, EncoderContext encoderContext, PropertyModel<S> propertyModel) {
      if (propertyModel != null && propertyModel.isReadable()) {
         S propertyValue = propertyModel.getPropertyAccessor().get(instance);
         this.encodeValue(writer, encoderContext, propertyModel, propertyValue);
      }

   }

   private <S> void encodeValue(BsonWriter writer, EncoderContext encoderContext, PropertyModel<S> propertyModel, S propertyValue) {
      if (propertyModel.shouldSerialize(propertyValue)) {
         try {
            if (propertyModel.getPropertySerialization().inline()) {
               if (propertyValue != null) {
                  (new BsonDocumentWrapper(propertyValue, propertyModel.getCachedCodec())).forEach((k, v) -> {
                     writer.writeName(k);
                     @SuppressWarnings("unchecked")
                     Codec<BsonValue> valueCodec = (Codec<BsonValue>) this.registry.get(v.getClass());
                     encoderContext.encodeWithChildContext(valueCodec, writer, v);
                  });
               }
            } else {
               writer.writeName(propertyModel.getReadName());
               if (propertyValue == null) {
                  writer.writeNull();
               } else {
                  encoderContext.encodeWithChildContext(propertyModel.getCachedCodec(), writer, propertyValue);
               }
            }
         } catch (CodecConfigurationException e) {
            throw new CodecConfigurationException(String.format("Failed to encode '%s'. Encoding '%s' errored with: %s", this.classModel.getName(), propertyModel.getReadName(), e.getMessage()), e);
         }
      }

   }

   private void decodeProperties(BsonReader reader, DecoderContext decoderContext, InstanceCreator<T> instanceCreator) {
      PropertyModel<?> inlineElementsPropertyModel = this.classModel.getPropertyModels().stream()
         .filter((p) -> p.getPropertySerialization().inline())
         .findFirst()
         .orElse(null);
      BsonDocument extraElements = inlineElementsPropertyModel == null ? null : new BsonDocument();
      reader.readStartDocument();

      while(true) {
         while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (this.classModel.useDiscriminator() && this.classModel.getDiscriminatorKey().equals(name)) {
               reader.readString();
            } else {
               this.decodePropertyModel(reader, decoderContext, instanceCreator, name, this.getPropertyModelByWriteName(this.classModel, name), extraElements);
            }
         }

         reader.readEndDocument();
         this.setPropertyValueBsonExtraElements(instanceCreator, extraElements, inlineElementsPropertyModel);
         return;
      }
   }

   private <S> void decodePropertyModel(BsonReader reader, DecoderContext decoderContext, InstanceCreator<T> instanceCreator, String name, PropertyModel<S> propertyModel, @Nullable BsonDocument extraElements) {
      if (propertyModel != null) {
         this.setPropertyValue(instanceCreator, () -> {
            S value = null;
            if (reader.getCurrentBsonType() == BsonType.NULL) {
               reader.readNull();
            } else {
               Codec<S> codec = propertyModel.getCachedCodec();
               if (codec == null) {
                  throw new CodecConfigurationException(String.format("Missing codec in '%s' for '%s'", this.classModel.getName(), propertyModel.getName()));
               }

               value = decoderContext.decodeWithChildContext(codec, reader);
            }

            return value;
         }, propertyModel);
      } else if (extraElements == null) {
         if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Found property not present in the ClassModel: %s", name));
         }

         reader.skipValue();
      } else {
         try {
            extraElements.append(name, (BsonValue)decoderContext.decodeWithChildContext(BSON_VALUE_CODEC, reader));
         } catch (CodecConfigurationException e) {
            throw new CodecConfigurationException(String.format("Failed to decode '%s'. Decoding '%s' errored with: %s", this.classModel.getName(), name, e.getMessage()), e);
         }
      }

   }

   private <S> void setPropertyValue(InstanceCreator<T> instanceCreator, Supplier<S> valueSupplier, PropertyModel<S> propertyModel) {
      try {
         instanceCreator.set(valueSupplier.get(), propertyModel);
      } catch (CodecConfigurationException | BsonInvalidOperationException e) {
         throw new CodecConfigurationException(String.format("Failed to decode '%s'. Decoding '%s' errored with: %s", this.classModel.getName(), propertyModel.getName(), e.getMessage()), e);
      }
   }

   private <S> void setPropertyValueBsonExtraElements(InstanceCreator<T> instanceCreator, @Nullable BsonDocument extraElements, PropertyModel<S> inlineElementsPropertyModel) {
      if (extraElements != null && !extraElements.isEmpty() && inlineElementsPropertyModel != null && inlineElementsPropertyModel.isWritable()) {
         this.setPropertyValue(instanceCreator, () -> {
            return inlineElementsPropertyModel.getCachedCodec().decode(new BsonDocumentReader(extraElements), DecoderContext.builder().build());
         }, inlineElementsPropertyModel);
      }

   }

   private void specialize() {
      this.classModel.getPropertyModels().forEach(this::cachePropertyModelCodec);
   }

   private <S> void cachePropertyModelCodec(PropertyModel<S> propertyModel) {
      if (propertyModel.getCachedCodec() == null) {
         Codec<S> codec = propertyModel.getCodec() != null ? propertyModel.getCodec() : new LazyPropertyModelCodec(propertyModel, this.registry, this.propertyCodecRegistry);
         propertyModel.cachedCodec((Codec)codec);
      }

   }

   private <S, V> boolean areEquivalentTypes(Class<S> t1, Class<V> t2) {
      if (t1.equals(t2)) {
         return true;
      } else if (Collection.class.isAssignableFrom(t1) && Collection.class.isAssignableFrom(t2)) {
         return true;
      } else {
         return Map.class.isAssignableFrom(t1) && Map.class.isAssignableFrom(t2);
      }
   }

   private Codec<T> getCodecFromDocument(BsonReader reader, boolean useDiscriminator, String discriminatorKey, CodecRegistry registry, DiscriminatorLookup discriminatorLookup, Codec<T> defaultCodec) {
      Codec<T> codec = defaultCodec;
      if (useDiscriminator) {
         BsonReaderMark mark = reader.getMark();
         reader.readStartDocument();
         boolean discriminatorKeyFound = false;

         while(!discriminatorKeyFound && reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (discriminatorKey.equals(name)) {
               discriminatorKeyFound = true;

               try {
                  Class<?> discriminatorClass = discriminatorLookup.lookup(reader.readString());
                  if (!codec.getEncoderClass().equals(discriminatorClass)) {
                     @SuppressWarnings("unchecked")
                     Codec<T> resolved = (Codec<T>) registry.get(discriminatorClass);
                     codec = resolved;
                  }
               } catch (Exception e) {
                  throw new CodecConfigurationException(String.format("Failed to decode '%s'. Decoding errored with: %s", this.classModel.getName(), e.getMessage()), e);
               }
            } else {
               reader.skipValue();
            }
         }

         mark.reset();
      }

      return codec;
   }

   private PropertyModel<?> getPropertyModelByWriteName(ClassModel<T> classModel, String readName) {
      for (PropertyModel<?> propertyModel : classModel.getPropertyModels()) {
         if (propertyModel.isWritable() && propertyModel.getWriteName().equals(readName)) {
            return propertyModel;
         }
      }
      return null;
   }

   DiscriminatorLookup getDiscriminatorLookup() {
      return this.discriminatorLookup;
   }
}
