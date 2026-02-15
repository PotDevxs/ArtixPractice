package dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.introspector;

import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;
import dev.artixdev.libs.org.simpleyaml.configuration.implementation.snakeyaml.lib.util.PlatformFeatureDetector;

public class PropertyUtils {
   private final Map<Class<?>, Map<String, Property>> propertiesCache;
   private final Map<Class<?>, Set<Property>> readableProperties;
   private BeanAccess beanAccess;
   private boolean allowReadOnlyProperties;
   private boolean skipMissingProperties;
   private final PlatformFeatureDetector platformFeatureDetector;
   private static final String TRANSIENT = "transient";

   public PropertyUtils() {
      this(new PlatformFeatureDetector());
   }

   PropertyUtils(PlatformFeatureDetector platformFeatureDetector) {
      this.propertiesCache = new HashMap();
      this.readableProperties = new HashMap();
      this.beanAccess = BeanAccess.DEFAULT;
      this.allowReadOnlyProperties = false;
      this.skipMissingProperties = false;
      this.platformFeatureDetector = platformFeatureDetector;
      if (platformFeatureDetector.isRunningOnAndroid()) {
         this.beanAccess = BeanAccess.FIELD;
      }

   }

   protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
      if (this.propertiesCache.containsKey(type)) {
         return (Map)this.propertiesCache.get(type);
      } else {
         Map<String, Property> properties = new LinkedHashMap();
         boolean inaccessableFieldsExist = false;
         Class c;
         Field[] fields;
         int fieldsLength;
         int i;
         Field field;
         int modifiers;
         if (bAccess == BeanAccess.FIELD) {
            for (c = type; c != null; c = c.getSuperclass()) {
               fields = c.getDeclaredFields();
               fieldsLength = fields.length;

               for (i = 0; i < fieldsLength; ++i) {
                  field = fields[i];
                  modifiers = field.getModifiers();
                  if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && !properties.containsKey(field.getName())) {
                     properties.put(field.getName(), new FieldProperty(field));
                  }
               }
            }
         } else {
            try {
               PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type).getPropertyDescriptors();
               int descriptorsLength = descriptors.length;

               for (int j = 0; j < descriptorsLength; ++j) {
                  PropertyDescriptor property = descriptors[j];
                  Method readMethod = property.getReadMethod();
                  if ((readMethod == null || !readMethod.getName().equals("getClass")) && !this.isTransient(property)) {
                     properties.put(property.getName(), new MethodProperty(property));
                  }
               }
            } catch (IntrospectionException e) {
               throw new YAMLException(e);
            }

            for (c = type; c != null; c = c.getSuperclass()) {
               fields = c.getDeclaredFields();
               fieldsLength = fields.length;

               for (i = 0; i < fieldsLength; ++i) {
                  field = fields[i];
                  modifiers = field.getModifiers();
                  if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                     if (Modifier.isPublic(modifiers)) {
                        properties.put(field.getName(), new FieldProperty(field));
                     } else {
                        inaccessableFieldsExist = true;
                     }
                  }
               }
            }
         }

         if (properties.isEmpty() && inaccessableFieldsExist) {
            throw new YAMLException("No JavaBean properties found in " + type.getName());
         } else {
            this.propertiesCache.put(type, properties);
            return properties;
         }
      }
   }

   private boolean isTransient(FeatureDescriptor fd) {
      return Boolean.TRUE.equals(fd.getValue("transient"));
   }

   public Set<Property> getProperties(Class<? extends Object> type) {
      return this.getProperties(type, this.beanAccess);
   }

   public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) {
      if (this.readableProperties.containsKey(type)) {
         return (Set)this.readableProperties.get(type);
      } else {
         Set<Property> properties = this.createPropertySet(type, bAccess);
         this.readableProperties.put(type, properties);
         return properties;
      }
   }

   protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) {
      Set<Property> properties = new TreeSet();
      Collection<Property> props = this.getPropertiesMap(type, bAccess).values();
      Iterator<Property> propIterator = props.iterator();

      while (true) {
         Property property;
         do {
            do {
               if (!propIterator.hasNext()) {
                  return properties;
               }

               property = propIterator.next();
            } while(!property.isReadable());
         } while(!this.allowReadOnlyProperties && !property.isWritable());

         properties.add(property);
      }
   }

   public Property getProperty(Class<? extends Object> type, String name) {
      return this.getProperty(type, name, this.beanAccess);
   }

   public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) {
      Map<String, Property> properties = this.getPropertiesMap(type, bAccess);
      Property property = (Property)properties.get(name);
      if (property == null && this.skipMissingProperties) {
         property = new MissingProperty(name);
      }

      if (property == null) {
         throw new YAMLException("Unable to find property '" + name + "' on class: " + type.getName());
      } else {
         return (Property)property;
      }
   }

   public void setBeanAccess(BeanAccess beanAccess) {
      if (this.platformFeatureDetector.isRunningOnAndroid() && beanAccess != BeanAccess.FIELD) {
         throw new IllegalArgumentException("JVM is Android - only BeanAccess.FIELD is available");
      } else {
         if (this.beanAccess != beanAccess) {
            this.beanAccess = beanAccess;
            this.propertiesCache.clear();
            this.readableProperties.clear();
         }

      }
   }

   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
      if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
         this.allowReadOnlyProperties = allowReadOnlyProperties;
         this.readableProperties.clear();
      }

   }

   public boolean isAllowReadOnlyProperties() {
      return this.allowReadOnlyProperties;
   }

   public void setSkipMissingProperties(boolean skipMissingProperties) {
      if (this.skipMissingProperties != skipMissingProperties) {
         this.skipMissingProperties = skipMissingProperties;
         this.readableProperties.clear();
      }

   }

   public boolean isSkipMissingProperties() {
      return this.skipMissingProperties;
   }
}
