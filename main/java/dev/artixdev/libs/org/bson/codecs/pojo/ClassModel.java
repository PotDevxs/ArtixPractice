package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ClassModel<T> {
   private final String name;
   private final Class<T> type;
   private final boolean hasTypeParameters;
   private final InstanceCreatorFactory<T> instanceCreatorFactory;
   private final boolean discriminatorEnabled;
   private final String discriminatorKey;
   private final String discriminator;
   private final IdPropertyModelHolder<?> idPropertyModelHolder;
   private final List<PropertyModel<?>> propertyModels;
   private final Map<String, TypeParameterMap> propertyNameToTypeParameterMap;

   ClassModel(Class<T> clazz, Map<String, TypeParameterMap> propertyNameToTypeParameterMap, InstanceCreatorFactory<T> instanceCreatorFactory, Boolean discriminatorEnabled, String discriminatorKey, String discriminator, IdPropertyModelHolder<?> idPropertyModelHolder, List<PropertyModel<?>> propertyModels) {
      this.name = clazz.getSimpleName();
      this.type = clazz;
      this.hasTypeParameters = clazz.getTypeParameters().length > 0;
      this.propertyNameToTypeParameterMap = Collections.unmodifiableMap(new HashMap(propertyNameToTypeParameterMap));
      this.instanceCreatorFactory = instanceCreatorFactory;
      this.discriminatorEnabled = discriminatorEnabled;
      this.discriminatorKey = discriminatorKey;
      this.discriminator = discriminator;
      this.idPropertyModelHolder = idPropertyModelHolder;
      this.propertyModels = Collections.unmodifiableList(new ArrayList(propertyModels));
   }

   public static <S> ClassModelBuilder<S> builder(Class<S> type) {
      return new ClassModelBuilder(type);
   }

   InstanceCreator<T> getInstanceCreator() {
      return this.instanceCreatorFactory.create();
   }

   public Class<T> getType() {
      return this.type;
   }

   public boolean hasTypeParameters() {
      return this.hasTypeParameters;
   }

   public boolean useDiscriminator() {
      return this.discriminatorEnabled;
   }

   public String getDiscriminatorKey() {
      return this.discriminatorKey;
   }

   public String getDiscriminator() {
      return this.discriminator;
   }

   public PropertyModel<?> getPropertyModel(String propertyName) {
      Iterator<PropertyModel<?>> iterator = this.propertyModels.iterator();

      PropertyModel propertyModel;
      do {
         if (!iterator.hasNext()) {
            return null;
         }

         propertyModel = iterator.next();
      } while(!propertyModel.getName().equals(propertyName));

      return propertyModel;
   }

   public List<PropertyModel<?>> getPropertyModels() {
      return this.propertyModels;
   }

   public PropertyModel<?> getIdPropertyModel() {
      return this.idPropertyModelHolder != null ? this.idPropertyModelHolder.getPropertyModel() : null;
   }

   IdPropertyModelHolder<?> getIdPropertyModelHolder() {
      return this.idPropertyModelHolder;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return "ClassModel{type=" + this.type + "}";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ClassModel<?> that = (ClassModel)o;
         if (this.discriminatorEnabled != that.discriminatorEnabled) {
            return false;
         } else if (!this.getType().equals(that.getType())) {
            return false;
         } else if (!this.getInstanceCreatorFactory().equals(that.getInstanceCreatorFactory())) {
            return false;
         } else {
            if (this.getDiscriminatorKey() != null) {
               if (!this.getDiscriminatorKey().equals(that.getDiscriminatorKey())) {
                  return false;
               }
            } else if (that.getDiscriminatorKey() != null) {
               return false;
            }

            label46: {
               if (this.getDiscriminator() != null) {
                  if (this.getDiscriminator().equals(that.getDiscriminator())) {
                     break label46;
                  }
               } else if (that.getDiscriminator() == null) {
                  break label46;
               }

               return false;
            }

            if (!Objects.equals(this.idPropertyModelHolder, that.idPropertyModelHolder)) {
               return false;
            } else if (!this.getPropertyModels().equals(that.getPropertyModels())) {
               return false;
            } else if (!this.getPropertyNameToTypeParameterMap().equals(that.getPropertyNameToTypeParameterMap())) {
               return false;
            } else {
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.getType().hashCode();
      result = 31 * result + this.getInstanceCreatorFactory().hashCode();
      result = 31 * result + (this.discriminatorEnabled ? 1 : 0);
      result = 31 * result + (this.getDiscriminatorKey() != null ? this.getDiscriminatorKey().hashCode() : 0);
      result = 31 * result + (this.getDiscriminator() != null ? this.getDiscriminator().hashCode() : 0);
      result = 31 * result + (this.getIdPropertyModelHolder() != null ? this.getIdPropertyModelHolder().hashCode() : 0);
      result = 31 * result + this.getPropertyModels().hashCode();
      result = 31 * result + this.getPropertyNameToTypeParameterMap().hashCode();
      return result;
   }

   InstanceCreatorFactory<T> getInstanceCreatorFactory() {
      return this.instanceCreatorFactory;
   }

   Map<String, TypeParameterMap> getPropertyNameToTypeParameterMap() {
      return this.propertyNameToTypeParameterMap;
   }
}
