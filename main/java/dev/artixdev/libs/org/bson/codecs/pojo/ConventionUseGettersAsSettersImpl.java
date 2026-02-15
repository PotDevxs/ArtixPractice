package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class ConventionUseGettersAsSettersImpl implements Convention {
   public void apply(ClassModelBuilder<?> classModelBuilder) {
      Iterator var2 = classModelBuilder.getPropertyModelBuilders().iterator();

      while(var2.hasNext()) {
         PropertyModelBuilder<?> propertyModelBuilder = (PropertyModelBuilder)var2.next();
         if (!(propertyModelBuilder.getPropertyAccessor() instanceof PropertyAccessorImpl)) {
            throw new CodecConfigurationException(String.format("The USE_GETTER_AS_SETTER_CONVENTION is not compatible with propertyModelBuilder instance that have custom implementations of org.bson.codecs.pojo.PropertyAccessor: %s", propertyModelBuilder.getPropertyAccessor().getClass().getName()));
         }

         PropertyAccessorImpl<?> defaultAccessor = (PropertyAccessorImpl)propertyModelBuilder.getPropertyAccessor();
         PropertyMetadata<?> propertyMetaData = defaultAccessor.getPropertyMetadata();
         if (!propertyMetaData.isDeserializable() && propertyMetaData.isSerializable() && this.isMapOrCollection(propertyMetaData.getTypeData().getType())) {
            this.setPropertyAccessor(propertyModelBuilder);
         }
      }

   }

   private <T> boolean isMapOrCollection(Class<T> clazz) {
      return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
   }

   private <T> void setPropertyAccessor(PropertyModelBuilder<T> propertyModelBuilder) {
      propertyModelBuilder.propertyAccessor(new ConventionUseGettersAsSettersImpl.PrivatePropertyAccessor((PropertyAccessorImpl)propertyModelBuilder.getPropertyAccessor()));
   }

   private static final class PrivatePropertyAccessor<T> implements PropertyAccessor<T> {
      private final PropertyAccessorImpl<T> wrapped;

      private PrivatePropertyAccessor(PropertyAccessorImpl<T> wrapped) {
         this.wrapped = wrapped;
      }

      public <S> T get(S instance) {
         return this.wrapped.get(instance);
      }

      public <S> void set(S instance, T value) {
         if (value instanceof Collection) {
            this.mutateCollection(instance, (Collection)value);
         } else if (value instanceof Map) {
            this.mutateMap(instance, (Map)value);
         } else {
            this.throwCodecConfigurationException(String.format("Unexpected type: '%s'", value.getClass()), (Exception)null);
         }

      }

      private <S> void mutateCollection(S instance, Collection value) {
         T originalCollection = this.get(instance);
         Collection<?> collection = (Collection)originalCollection;
         if (collection == null) {
            this.throwCodecConfigurationException("The getter returned null.", (Exception)null);
         } else if (!collection.isEmpty()) {
            this.throwCodecConfigurationException("The getter returned a non empty collection.", (Exception)null);
         } else {
            try {
               collection.addAll(value);
            } catch (Exception e) {
               this.throwCodecConfigurationException("collection#addAll failed.", e);
            }
         }

      }

      private <S> void mutateMap(S instance, Map value) {
         T originalMap = this.get(instance);
         Map<?, ?> map = (Map)originalMap;
         if (map == null) {
            this.throwCodecConfigurationException("The getter returned null.", (Exception)null);
         } else if (!map.isEmpty()) {
            this.throwCodecConfigurationException("The getter returned a non empty map.", (Exception)null);
         } else {
            try {
               map.putAll(value);
            } catch (Exception e) {
               this.throwCodecConfigurationException("map#putAll failed.", e);
            }
         }

      }

      private void throwCodecConfigurationException(String reason, Exception cause) {
         throw new CodecConfigurationException(String.format("Cannot use getter in '%s' to set '%s'. %s", this.wrapped.getPropertyMetadata().getDeclaringClassName(), this.wrapped.getPropertyMetadata().getName(), reason), cause);
      }

      // $FF: synthetic method
      PrivatePropertyAccessor(PropertyAccessorImpl x0, Object x1) {
         this(x0);
      }
   }
}
