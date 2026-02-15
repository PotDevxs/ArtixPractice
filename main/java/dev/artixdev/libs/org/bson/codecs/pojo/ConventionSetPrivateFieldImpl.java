package dev.artixdev.libs.org.bson.codecs.pojo;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class ConventionSetPrivateFieldImpl implements Convention {
   public void apply(ClassModelBuilder<?> classModelBuilder) {
      Iterator<PropertyModelBuilder<?>> iterator = classModelBuilder.getPropertyModelBuilders().iterator();

      while(iterator.hasNext()) {
         PropertyModelBuilder<?> propertyModelBuilder = iterator.next();
         if (!(propertyModelBuilder.getPropertyAccessor() instanceof PropertyAccessorImpl)) {
            throw new CodecConfigurationException(String.format("The SET_PRIVATE_FIELDS_CONVENTION is not compatible with propertyModelBuilder instance that have custom implementations of org.bson.codecs.pojo.PropertyAccessor: %s", propertyModelBuilder.getPropertyAccessor().getClass().getName()));
         }

         PropertyAccessorImpl<?> defaultAccessor = (PropertyAccessorImpl)propertyModelBuilder.getPropertyAccessor();
         PropertyMetadata<?> propertyMetaData = defaultAccessor.getPropertyMetadata();
         if (!propertyMetaData.isDeserializable() && propertyMetaData.getField() != null && Modifier.isPrivate(propertyMetaData.getField().getModifiers())) {
            this.setPropertyAccessor(propertyModelBuilder);
         }
      }

   }

   private <T> void setPropertyAccessor(PropertyModelBuilder<T> propertyModelBuilder) {
      propertyModelBuilder.propertyAccessor(new FieldPropertyAccessor((PropertyAccessorImpl)propertyModelBuilder.getPropertyAccessor()));
   }
}
