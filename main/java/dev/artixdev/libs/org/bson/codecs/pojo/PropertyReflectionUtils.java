package dev.artixdev.libs.org.bson.codecs.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class PropertyReflectionUtils {
   private static final String IS_PREFIX = "is";
   private static final String GET_PREFIX = "get";
   private static final String SET_PREFIX = "set";

   private PropertyReflectionUtils() {
   }

   static boolean isGetter(Method method) {
      if (method.getParameterCount() > 0) {
         return false;
      } else if (method.getName().startsWith("get") && method.getName().length() > "get".length()) {
         return Character.isUpperCase(method.getName().charAt("get".length()));
      } else {
         return method.getName().startsWith("is") && method.getName().length() > "is".length() ? Character.isUpperCase(method.getName().charAt("is".length())) : false;
      }
   }

   static boolean isSetter(Method method) {
      return method.getName().startsWith("set") && method.getName().length() > "set".length() && method.getParameterCount() == 1 ? Character.isUpperCase(method.getName().charAt("set".length())) : false;
   }

   static String toPropertyName(Method method) {
      String name = method.getName();
      String propertyName = name.substring(name.startsWith("is") ? 2 : 3);
      char[] chars = propertyName.toCharArray();
      chars[0] = Character.toLowerCase(chars[0]);
      return new String(chars);
   }

   static PropertyReflectionUtils.PropertyMethods getPropertyMethods(Class<?> clazz) {
      List<Method> setters = new ArrayList();
      List<Method> getters = new ArrayList();
      Class[] var3 = clazz.getInterfaces();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         Class<?> i = var3[var5];
         Method[] var7 = i.getDeclaredMethods();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Method method = var7[var9];
            if (method.isDefault()) {
               verifyAddMethodToList(method, getters, setters);
            }
         }
      }

      Method[] var11 = clazz.getDeclaredMethods();
      var4 = var11.length;

      for(var5 = 0; var5 < var4; ++var5) {
         Method method = var11[var5];
         verifyAddMethodToList(method, getters, setters);
      }

      return new PropertyReflectionUtils.PropertyMethods(getters, setters);
   }

   private static void verifyAddMethodToList(Method method, List<Method> getters, List<Method> setters) {
      if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && !method.isBridge()) {
         if (isGetter(method)) {
            getters.add(method);
         } else if (isSetter(method)) {
            setters.add(method);
         }
      }

   }

   static class PropertyMethods {
      private final Collection<Method> getterMethods;
      private final Collection<Method> setterMethods;

      PropertyMethods(Collection<Method> getterMethods, Collection<Method> setterMethods) {
         this.getterMethods = getterMethods;
         this.setterMethods = setterMethods;
      }

      Collection<Method> getGetterMethods() {
         return this.getterMethods;
      }

      Collection<Method> getSetterMethods() {
         return this.setterMethods;
      }
   }
}
