package dev.artixdev.libs.com.google.gson.internal;

import java.lang.reflect.Type;

public final class Primitives {
   private Primitives() {
   }

   public static boolean isPrimitive(Type type) {
      return type instanceof Class && ((Class)type).isPrimitive();
   }

   public static boolean isWrapperType(Type type) {
      return type == Integer.class || type == Float.class || type == Byte.class || type == Double.class || type == Long.class || type == Character.class || type == Boolean.class || type == Short.class || type == Void.class;
   }

   public static <T> Class<T> wrap(Class<T> type) {
      if (type == Integer.TYPE) {
         return (Class<T>) Integer.class;
      } else if (type == Float.TYPE) {
         return (Class<T>) Float.class;
      } else if (type == Byte.TYPE) {
         return (Class<T>) Byte.class;
      } else if (type == Double.TYPE) {
         return (Class<T>) Double.class;
      } else if (type == Long.TYPE) {
         return (Class<T>) Long.class;
      } else if (type == Character.TYPE) {
         return (Class<T>) Character.class;
      } else if (type == Boolean.TYPE) {
         return (Class<T>) Boolean.class;
      } else if (type == Short.TYPE) {
         return (Class<T>) Short.class;
      } else {
         return type == Void.TYPE ? (Class<T>) Void.class : type;
      }
   }

   public static <T> Class<T> unwrap(Class<T> type) {
      if (type == Integer.class) {
         return (Class<T>) Integer.TYPE;
      } else if (type == Float.class) {
         return (Class<T>) Float.TYPE;
      } else if (type == Byte.class) {
         return (Class<T>) Byte.TYPE;
      } else if (type == Double.class) {
         return (Class<T>) Double.TYPE;
      } else if (type == Long.class) {
         return (Class<T>) Long.TYPE;
      } else if (type == Character.class) {
         return (Class<T>) Character.TYPE;
      } else if (type == Boolean.class) {
         return (Class<T>) Boolean.TYPE;
      } else if (type == Short.class) {
         return (Class<T>) Short.TYPE;
      } else {
         return type == Void.class ? (Class<T>) Void.TYPE : type;
      }
   }
}
