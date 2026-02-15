package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class PropertyAccessorImpl<T> implements PropertyAccessor<T> {
   private final PropertyMetadata<T> propertyMetadata;

   PropertyAccessorImpl(PropertyMetadata<T> propertyMetadata) {
      this.propertyMetadata = propertyMetadata;
   }

   public <S> T get(S instance) {
      try {
         if (this.propertyMetadata.isSerializable()) {
            @SuppressWarnings("unchecked")
            T result = this.propertyMetadata.getGetter() != null ? (T) this.propertyMetadata.getGetter().invoke(instance) : (T) this.propertyMetadata.getField().get(instance);
            return result;
         } else {
            throw this.getError((Exception)null);
         }
      } catch (Exception e) {
         throw this.getError(e);
      }
   }

   public <S> void set(S instance, T value) {
      try {
         if (this.propertyMetadata.isDeserializable()) {
            if (this.propertyMetadata.getSetter() != null) {
               this.propertyMetadata.getSetter().invoke(instance, value);
            } else {
               this.propertyMetadata.getField().set(instance, value);
            }
         }

      } catch (Exception e) {
         throw this.setError(e);
      }
   }

   PropertyMetadata<T> getPropertyMetadata() {
      return this.propertyMetadata;
   }

   private CodecConfigurationException getError(Exception cause) {
      return new CodecConfigurationException(String.format("Unable to get value for property '%s' in %s", this.propertyMetadata.getName(), this.propertyMetadata.getDeclaringClassName()), cause);
   }

   private CodecConfigurationException setError(Exception cause) {
      return new CodecConfigurationException(String.format("Unable to set value for property '%s' in %s", this.propertyMetadata.getName(), this.propertyMetadata.getDeclaringClassName()), cause);
   }
}
