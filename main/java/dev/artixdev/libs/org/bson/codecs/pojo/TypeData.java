package dev.artixdev.libs.org.bson.codecs.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import dev.artixdev.libs.org.bson.assertions.Assertions;

final class TypeData<T> implements TypeWithTypeParameters<T> {
   private final Class<T> type;
   private final List<TypeData<?>> typeParameters;
   private static final Map<Class<?>, Class<?>> PRIMITIVE_CLASS_MAP;

   public static <T> TypeData.Builder<T> builder(Class<T> type) {
      return new TypeData.Builder((Class)Assertions.notNull("type", type));
   }

   public static TypeData<?> newInstance(Method method) {
      return PropertyReflectionUtils.isGetter(method) ? newInstance(method.getGenericReturnType(), method.getReturnType()) : newInstance(method.getGenericParameterTypes()[0], method.getParameterTypes()[0]);
   }

   public static TypeData<?> newInstance(Field field) {
      return newInstance(field.getGenericType(), field.getType());
   }

   public static <T> TypeData<T> newInstance(Type genericType, Class<T> clazz) {
      TypeData.Builder<T> builder = builder(clazz);
      if (genericType instanceof ParameterizedType) {
         ParameterizedType pType = (ParameterizedType)genericType;
         Type[] var4 = pType.getActualTypeArguments();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Type argType = var4[var6];
            getNestedTypeData(builder, argType);
         }
      }

      return builder.build();
   }

   private static <T> void getNestedTypeData(TypeData.Builder<T> builder, Type type) {
      if (type instanceof ParameterizedType) {
         ParameterizedType pType = (ParameterizedType)type;
         TypeData.Builder paramBuilder = builder((Class)pType.getRawType());
         Type[] var4 = pType.getActualTypeArguments();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Type argType = var4[var6];
            getNestedTypeData(paramBuilder, argType);
         }

         builder.addTypeParameter(paramBuilder.build());
      } else if (type instanceof WildcardType) {
         builder.addTypeParameter(builder((Class)((WildcardType)type).getUpperBounds()[0]).build());
      } else if (type instanceof TypeVariable) {
         builder.addTypeParameter(builder(Object.class).build());
      } else if (type instanceof Class) {
         builder.addTypeParameter(builder((Class)type).build());
      }

   }

   public Class<T> getType() {
      return this.type;
   }

   public List<TypeData<?>> getTypeParameters() {
      return this.typeParameters;
   }

   public String toString() {
      String typeParams = this.typeParameters.isEmpty() ? "" : ", typeParameters=[" + nestedTypeParameters(this.typeParameters) + "]";
      return "TypeData{type=" + this.type.getSimpleName() + typeParams + "}";
   }

   private static String nestedTypeParameters(List<TypeData<?>> typeParameters) {
      StringBuilder builder = new StringBuilder();
      int count = 0;
      int last = typeParameters.size();
      Iterator var4 = typeParameters.iterator();

      while(var4.hasNext()) {
         TypeData<?> typeParameter = (TypeData)var4.next();
         ++count;
         builder.append(typeParameter.getType().getSimpleName());
         if (!typeParameter.getTypeParameters().isEmpty()) {
            builder.append(String.format("<%s>", nestedTypeParameters(typeParameter.getTypeParameters())));
         }

         if (count < last) {
            builder.append(", ");
         }
      }

      return builder.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof TypeData)) {
         return false;
      } else {
         TypeData<?> that = (TypeData)o;
         if (!this.getType().equals(that.getType())) {
            return false;
         } else {
            return this.getTypeParameters().equals(that.getTypeParameters());
         }
      }
   }

   public int hashCode() {
      int result = this.getType().hashCode();
      result = 31 * result + this.getTypeParameters().hashCode();
      return result;
   }

   private TypeData(Class<T> type, List<TypeData<?>> typeParameters) {
      this.type = this.boxType(type);
      this.typeParameters = typeParameters;
   }

   boolean isAssignableFrom(Class<?> cls) {
      return this.type.isAssignableFrom(this.boxType(cls));
   }

   private <S> Class<S> boxType(Class<S> clazz) {
      return clazz.isPrimitive() ? (Class)PRIMITIVE_CLASS_MAP.get(clazz) : clazz;
   }

   // $FF: synthetic method
   TypeData(Class x0, List x1, Object x2) {
      this(x0, x1);
   }

   static {
      Map<Class<?>, Class<?>> map = new HashMap();
      map.put(Boolean.TYPE, Boolean.class);
      map.put(Byte.TYPE, Byte.class);
      map.put(Character.TYPE, Character.class);
      map.put(Double.TYPE, Double.class);
      map.put(Float.TYPE, Float.class);
      map.put(Integer.TYPE, Integer.class);
      map.put(Long.TYPE, Long.class);
      map.put(Short.TYPE, Short.class);
      map.put(Void.TYPE, Void.class);
      PRIMITIVE_CLASS_MAP = map;
   }

   public static final class Builder<T> {
      private final Class<T> type;
      private final List<TypeData<?>> typeParameters;

      private Builder(Class<T> type) {
         this.typeParameters = new ArrayList();
         this.type = type;
      }

      public <S> TypeData.Builder<T> addTypeParameter(TypeData<S> typeParameter) {
         this.typeParameters.add((TypeData)Assertions.notNull("typeParameter", typeParameter));
         return this;
      }

      public TypeData.Builder<T> addTypeParameters(List<TypeData<?>> typeParameters) {
         Assertions.notNull("typeParameters", typeParameters);
         Iterator var2 = typeParameters.iterator();

         while(var2.hasNext()) {
            TypeData<?> typeParameter = (TypeData)var2.next();
            this.addTypeParameter(typeParameter);
         }

         return this;
      }

      public TypeData<T> build() {
         return new TypeData(this.type, Collections.unmodifiableList(this.typeParameters));
      }

      // $FF: synthetic method
      Builder(Class x0, Object x1) {
         this(x0);
      }
   }
}
