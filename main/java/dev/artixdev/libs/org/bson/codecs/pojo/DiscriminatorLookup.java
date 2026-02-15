package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

final class DiscriminatorLookup {
   private final Map<String, Class<?>> discriminatorClassMap = new ConcurrentHashMap();
   private final Set<String> packages;

   DiscriminatorLookup(Map<Class<?>, ClassModel<?>> classModels, Set<String> packages) {
      Iterator var3 = classModels.values().iterator();

      while(var3.hasNext()) {
         ClassModel<?> classModel = (ClassModel)var3.next();
         if (classModel.getDiscriminator() != null) {
            this.discriminatorClassMap.put(classModel.getDiscriminator(), classModel.getType());
         }
      }

      this.packages = packages;
   }

   public Class<?> lookup(String discriminator) {
      if (this.discriminatorClassMap.containsKey(discriminator)) {
         return (Class)this.discriminatorClassMap.get(discriminator);
      } else {
         Class<?> clazz = this.getClassForName(discriminator);
         if (clazz == null) {
            clazz = this.searchPackages(discriminator);
         }

         if (clazz == null) {
            throw new CodecConfigurationException(String.format("A class could not be found for the discriminator: '%s'.", discriminator));
         } else {
            this.discriminatorClassMap.put(discriminator, clazz);
            return clazz;
         }
      }
   }

   void addClassModel(ClassModel<?> classModel) {
      if (classModel.getDiscriminator() != null) {
         this.discriminatorClassMap.put(classModel.getDiscriminator(), classModel.getType());
      }

   }

   private Class<?> getClassForName(String discriminator) {
      Class clazz = null;

      try {
         clazz = Class.forName(discriminator);
      } catch (ClassNotFoundException ignored) {
      }

      return clazz;
   }

   private Class<?> searchPackages(String discriminator) {
      Class<?> clazz = null;
      Iterator var3 = this.packages.iterator();

      do {
         if (!var3.hasNext()) {
            return clazz;
         }

         String packageName = (String)var3.next();
         clazz = this.getClassForName(packageName + "." + discriminator);
      } while(clazz == null);

      return clazz;
   }
}
