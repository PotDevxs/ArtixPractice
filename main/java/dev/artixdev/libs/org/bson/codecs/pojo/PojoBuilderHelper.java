package dev.artixdev.libs.org.bson.codecs.pojo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import dev.artixdev.libs.org.bson.assertions.Assertions;

final class PojoBuilderHelper {
   static <T> void configureClassModelBuilder(ClassModelBuilder<T> classModelBuilder, Class<T> clazz) {
      classModelBuilder.type((Class)Assertions.notNull("clazz", clazz));
      ArrayList<Annotation> annotations = new ArrayList();
      Set<String> propertyNames = new TreeSet();
      Map<String, TypeParameterMap> propertyTypeParameterMap = new HashMap();
      Class<? super T> currentClass = clazz;
      String declaringClassName = clazz.getSimpleName();
      TypeData<?> parentClassTypeData = null;
      HashMap propertyNameMap = new HashMap();

      int var11;
      int var12;
      label132:
      while(!currentClass.isEnum() && currentClass.getSuperclass() != null) {
         annotations.addAll(Arrays.asList(currentClass.getDeclaredAnnotations()));
         List<String> genericTypeNames = new ArrayList();
         TypeVariable[] var10 = currentClass.getTypeParameters();
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            TypeVariable<? extends Class<? super T>> classTypeVariable = var10[var12];
            genericTypeNames.add(classTypeVariable.getName());
         }

         PropertyReflectionUtils.PropertyMethods propertyMethods = PropertyReflectionUtils.getPropertyMethods(currentClass);
         Iterator var25 = propertyMethods.getSetterMethods().iterator();

         while(true) {
            PropertyMetadata propertyMetadata;
            Annotation[] var15;
            int var16;
            int var17;
            Annotation annotation;
            Method method;
            do {
               String propertyName;
               if (!var25.hasNext()) {
                  var25 = propertyMethods.getGetterMethods().iterator();

                  while(true) {
                     do {
                        do {
                           if (!var25.hasNext()) {
                              Field[] var26 = currentClass.getDeclaredFields();
                              var12 = var26.length;

                              for(int var30 = 0; var30 < var12; ++var30) {
                                 Field field = var26[var30];
                                 propertyNames.add(field.getName());
                                 PropertyMetadata<?> fieldPropertyMetadata = getOrCreateFieldPropertyMetadata(field.getName(), declaringClassName, propertyNameMap, TypeData.newInstance(field), propertyTypeParameterMap, parentClassTypeData, genericTypeNames, field.getGenericType());
                                 if (fieldPropertyMetadata != null && fieldPropertyMetadata.getField() == null) {
                                    fieldPropertyMetadata.field(field);
                                    Annotation[] var34 = field.getDeclaredAnnotations();
                                    var17 = var34.length;

                                    for(int var35 = 0; var35 < var17; ++var35) {
                                       Annotation fieldAnnotation = var34[var35];
                                       fieldPropertyMetadata.addReadAnnotation(fieldAnnotation);
                                       fieldPropertyMetadata.addWriteAnnotation(fieldAnnotation);
                                    }
                                 }
                              }

                              parentClassTypeData = TypeData.newInstance(currentClass.getGenericSuperclass(), currentClass);
                              currentClass = currentClass.getSuperclass();
                              continue label132;
                           }

                           method = (Method)var25.next();
                           propertyName = PropertyReflectionUtils.toPropertyName(method);
                           propertyNames.add(propertyName);
                           propertyMetadata = (PropertyMetadata)propertyNameMap.get(propertyName);
                        } while(propertyMetadata != null && propertyMetadata.getGetter() != null);

                        propertyMetadata = getOrCreateMethodPropertyMetadata(propertyName, declaringClassName, propertyNameMap, TypeData.newInstance(method), propertyTypeParameterMap, parentClassTypeData, genericTypeNames, getGenericType(method));
                     } while(propertyMetadata.getGetter() != null);

                     propertyMetadata.setGetter(method);
                     var15 = method.getDeclaredAnnotations();
                     var16 = var15.length;

                     for(var17 = 0; var17 < var16; ++var17) {
                        annotation = var15[var17];
                        propertyMetadata.addReadAnnotation(annotation);
                     }
                  }
               }

               method = (Method)var25.next();
               propertyName = PropertyReflectionUtils.toPropertyName(method);
               propertyNames.add(propertyName);
               propertyMetadata = getOrCreateMethodPropertyMetadata(propertyName, declaringClassName, propertyNameMap, TypeData.newInstance(method), propertyTypeParameterMap, parentClassTypeData, genericTypeNames, getGenericType(method));
            } while(propertyMetadata.getSetter() != null);

            propertyMetadata.setSetter(method);
            var15 = method.getDeclaredAnnotations();
            var16 = var15.length;

            for(var17 = 0; var17 < var16; ++var17) {
               annotation = var15[var17];
               propertyMetadata.addWriteAnnotation(annotation);
            }
         }
      }

      if (currentClass.isInterface()) {
         annotations.addAll(Arrays.asList(currentClass.getDeclaredAnnotations()));
      }

      Iterator var20 = propertyNames.iterator();

      while(true) {
         PropertyMetadata propertyMetadata;
         do {
            if (!var20.hasNext()) {
               Collections.reverse(annotations);
               classModelBuilder.annotations(annotations);
               classModelBuilder.propertyNameToTypeParameterMap(propertyTypeParameterMap);
               Constructor<T> noArgsConstructor = null;
               Constructor[] var24 = clazz.getDeclaredConstructors();
               var11 = var24.length;

               for(var12 = 0; var12 < var11; ++var12) {
                  Constructor<?> constructor = var24[var12];
                  if (constructor.getParameterCount() == 0 && (Modifier.isPublic(constructor.getModifiers()) || Modifier.isProtected(constructor.getModifiers()))) {
                     @SuppressWarnings("unchecked")
                     Constructor<T> typedConstructor = (Constructor<T>) constructor;
                     noArgsConstructor = typedConstructor;
                     constructor.setAccessible(true);
                  }
               }

               classModelBuilder.instanceCreatorFactory(new InstanceCreatorFactoryImpl(new CreatorExecutable(clazz, noArgsConstructor)));
               return;
            }

            String propertyName = (String)var20.next();
            propertyMetadata = (PropertyMetadata)propertyNameMap.get(propertyName);
         } while(!propertyMetadata.isSerializable() && !propertyMetadata.isDeserializable());

         classModelBuilder.addProperty(createPropertyModelBuilder(propertyMetadata));
      }
   }

   private static <T, S> PropertyMetadata<T> getOrCreateMethodPropertyMetadata(String propertyName, String declaringClassName, Map<String, PropertyMetadata<?>> propertyNameMap, TypeData<T> typeData, Map<String, TypeParameterMap> propertyTypeParameterMap, TypeData<S> parentClassTypeData, List<String> genericTypeNames, Type genericType) {
      PropertyMetadata<T> propertyMetadata = getOrCreatePropertyMetadata(propertyName, declaringClassName, propertyNameMap, typeData);
      if (!isAssignableClass(propertyMetadata.getTypeData().getType(), typeData.getType())) {
         propertyMetadata.setError(String.format("Property '%s' in %s, has differing data types: %s and %s.", propertyName, declaringClassName, propertyMetadata.getTypeData(), typeData));
      }

      cachePropertyTypeData(propertyMetadata, propertyTypeParameterMap, parentClassTypeData, genericTypeNames, genericType);
      return propertyMetadata;
   }

   private static boolean isAssignableClass(Class<?> propertyTypeClass, Class<?> typeDataClass) {
      Assertions.notNull("propertyTypeClass", propertyTypeClass);
      Assertions.notNull("typeDataClass", typeDataClass);
      return propertyTypeClass.isAssignableFrom(typeDataClass) || typeDataClass.isAssignableFrom(propertyTypeClass);
   }

   private static <T, S> PropertyMetadata<T> getOrCreateFieldPropertyMetadata(String propertyName, String declaringClassName, Map<String, PropertyMetadata<?>> propertyNameMap, TypeData<T> typeData, Map<String, TypeParameterMap> propertyTypeParameterMap, TypeData<S> parentClassTypeData, List<String> genericTypeNames, Type genericType) {
      PropertyMetadata<T> propertyMetadata = getOrCreatePropertyMetadata(propertyName, declaringClassName, propertyNameMap, typeData);
      if (!propertyMetadata.getTypeData().getType().isAssignableFrom(typeData.getType())) {
         return null;
      } else {
         cachePropertyTypeData(propertyMetadata, propertyTypeParameterMap, parentClassTypeData, genericTypeNames, genericType);
         return propertyMetadata;
      }
   }

   private static <T> PropertyMetadata<T> getOrCreatePropertyMetadata(String propertyName, String declaringClassName, Map<String, PropertyMetadata<?>> propertyNameMap, TypeData<T> typeData) {
      PropertyMetadata<T> propertyMetadata = (PropertyMetadata)propertyNameMap.get(propertyName);
      if (propertyMetadata == null) {
         propertyMetadata = new PropertyMetadata(propertyName, declaringClassName, typeData);
         propertyNameMap.put(propertyName, propertyMetadata);
      }

      return propertyMetadata;
   }

   private static <T, S> void cachePropertyTypeData(PropertyMetadata<T> propertyMetadata, Map<String, TypeParameterMap> propertyTypeParameterMap, TypeData<S> parentClassTypeData, List<String> genericTypeNames, Type genericType) {
      TypeParameterMap typeParameterMap = getTypeParameterMap(genericTypeNames, genericType);
      propertyTypeParameterMap.put(propertyMetadata.getName(), typeParameterMap);
      propertyMetadata.typeParameterInfo(typeParameterMap, parentClassTypeData);
   }

   private static Type getGenericType(Method method) {
      return PropertyReflectionUtils.isGetter(method) ? method.getGenericReturnType() : method.getGenericParameterTypes()[0];
   }

   static <T> PropertyModelBuilder<T> createPropertyModelBuilder(PropertyMetadata<T> propertyMetadata) {
      PropertyModelBuilder<T> propertyModelBuilder = PropertyModel.<T>builder().propertyName(propertyMetadata.getName()).readName(propertyMetadata.getName()).writeName(propertyMetadata.getName()).typeData(propertyMetadata.getTypeData()).readAnnotations(propertyMetadata.getReadAnnotations()).writeAnnotations(propertyMetadata.getWriteAnnotations()).propertySerialization(new PropertyModelSerializationImpl()).propertyAccessor(new PropertyAccessorImpl(propertyMetadata)).setError(propertyMetadata.getError());
      if (propertyMetadata.getTypeParameters() != null) {
         propertyModelBuilder.typeData(PojoSpecializationHelper.specializeTypeData(propertyModelBuilder.getTypeData(), propertyMetadata.getTypeParameters(), propertyMetadata.getTypeParameterMap()));
      }

      return propertyModelBuilder;
   }

   private static TypeParameterMap getTypeParameterMap(List<String> genericTypeNames, Type propertyType) {
      int classParamIndex = genericTypeNames.indexOf(propertyType.toString());
      TypeParameterMap.Builder builder = TypeParameterMap.builder();
      if (classParamIndex != -1) {
         builder.addIndex(classParamIndex);
      } else if (propertyType instanceof ParameterizedType) {
         ParameterizedType pt = (ParameterizedType)propertyType;

         for(int i = 0; i < pt.getActualTypeArguments().length; ++i) {
            classParamIndex = genericTypeNames.indexOf(pt.getActualTypeArguments()[i].toString());
            if (classParamIndex != -1) {
               builder.addIndex(i, classParamIndex);
            } else {
               builder.addIndex(i, getTypeParameterMap(genericTypeNames, pt.getActualTypeArguments()[i]));
            }
         }
      }

      return builder.build();
   }

   static <V> V stateNotNull(String property, V value) {
      if (value == null) {
         throw new IllegalStateException(String.format("%s cannot be null", property));
      } else {
         return value;
      }
   }

   private PojoBuilderHelper() {
   }
}
