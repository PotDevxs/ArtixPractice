package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.Iterator;

final class ConventionDefaultsImpl implements Convention {
   public void apply(ClassModelBuilder<?> classModelBuilder) {
      if (classModelBuilder.getDiscriminatorKey() == null) {
         classModelBuilder.discriminatorKey("_t");
      }

      if (classModelBuilder.getDiscriminator() == null && classModelBuilder.getType() != null) {
         classModelBuilder.discriminator(classModelBuilder.getType().getName());
      }

      Iterator<PropertyModelBuilder<?>> iterator = classModelBuilder.getPropertyModelBuilders().iterator();

      while(true) {
         String propertyName;
         do {
            PropertyModelBuilder propertyModel;
            do {
               if (!iterator.hasNext()) {
                  return;
               }

               propertyModel = iterator.next();
            } while(classModelBuilder.getIdPropertyName() != null);

            propertyName = propertyModel.getName();
         } while(!propertyName.equals("_id") && !propertyName.equals("id"));

         classModelBuilder.idPropertyName(propertyName);
      }
   }
}
