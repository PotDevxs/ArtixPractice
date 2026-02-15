package dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class Reflection {
   public static Field[] getFields(Class<?> cls) {
      if (cls == null) {
         return new Field[0];
      } else {
         Field[] declaredFields = cls.getDeclaredFields();
         Field[] var2 = declaredFields;
         int var3 = declaredFields.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field f = var2[var4];
            f.setAccessible(true);
         }

         return declaredFields;
      }
   }

   public static Field getField(Class<?> cls, String name) {
      if (cls == null) {
         return null;
      } else {
         try {
            Field field = cls.getDeclaredField(name);
            field.setAccessible(true);
            return field;
         } catch (NoSuchFieldException e) {
            try {
               Field field = cls.getField(name);
               field.setAccessible(true);
               return field;
            } catch (NoSuchFieldException ignored) {
               return cls.getSuperclass() != null ? getField(cls.getSuperclass(), name) : null;
            }
         }
      }
   }

   public static Field getField(Class<?> cls, Class<?> dataType, int index) {
      if (dataType != null && cls != null) {
         int currentIndex = 0;
         Field[] var4 = getFields(cls);
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field f = var4[var6];
            if (dataType.isAssignableFrom(f.getType()) && currentIndex++ == index) {
               return f;
            }
         }

         if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), dataType, index);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Field getField(Class<?> cls, Class<?> dataType, int index, boolean ignoreStatic) {
      if (dataType != null && cls != null) {
         int currentIndex = 0;
         Field[] var5 = getFields(cls);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Field f = var5[var7];
            if (dataType.isAssignableFrom(f.getType()) && (!ignoreStatic || !Modifier.isStatic(f.getModifiers())) && currentIndex++ == index) {
               return f;
            }
         }

         if (cls.getSuperclass() != null) {
            return getField(cls.getSuperclass(), dataType, index);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Field getField(Class<?> cls, int index) {
      if (cls == null) {
         return null;
      } else {
         try {
            return getFields(cls)[index];
         } catch (Exception e) {
            return cls.getSuperclass() != null ? getFields(cls.getSuperclass())[index] : null;
         }
      }
   }

   public static Method getMethod(Class<?> cls, String name, Class<?>... params) {
      if (cls == null) {
         return null;
      } else {
         try {
            Method m = cls.getDeclaredMethod(name, params);
            m.setAccessible(true);
            return m;
         } catch (NoSuchMethodException e) {
            try {
               Method m = cls.getMethod(name, params);
               m.setAccessible(true);
               return m;
            } catch (NoSuchMethodException ignored) {
               return cls.getSuperclass() != null ? getMethod(cls.getSuperclass(), name, params) : null;
            }
         }
      }
   }

   public static Method getMethod(Class<?> cls, int index, Class<?>... params) {
      if (cls == null) {
         return null;
      } else {
         int currentIndex = 0;
         Method[] var4 = cls.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method m = var4[var6];
            if ((params == null || Arrays.equals(m.getParameterTypes(), params)) && index == currentIndex++) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), index, params);
         } else {
            return null;
         }
      }
   }

   public static Method getMethod(Class<?> cls, Class<?> returning, int index, Class<?>... params) {
      if (cls == null) {
         return null;
      } else {
         int currentIndex = 0;
         Method[] var5 = cls.getDeclaredMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method m = var5[var7];
            if (Arrays.equals(m.getParameterTypes(), params) && (returning == null || m.getReturnType().equals(returning)) && index == currentIndex++) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), (Class)null, index, params);
         } else {
            return null;
         }
      }
   }

   public static Method getMethodExact(Class<?> cls, String name, Class<?> returning, Class<?>... params) {
      if (cls == null) {
         return null;
      } else {
         Method[] var4 = cls.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method m = var4[var6];
            if (m.getName().equals(name) && Arrays.equals(m.getParameterTypes(), params) && (returning == null || m.getReturnType().equals(returning))) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethodExact(cls.getSuperclass(), name, (Class)null, params);
         } else {
            return null;
         }
      }
   }

   public static Method getMethod(Class<?> cls, String name, int index) {
      if (cls == null) {
         return null;
      } else {
         int currentIndex = 0;
         Method[] var4 = cls.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method m = var4[var6];
            if (m.getName().equals(name) && index == currentIndex++) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), name, index);
         } else {
            return null;
         }
      }
   }

   public static Method getMethod(Class<?> cls, Class<?> returning, int index) {
      if (cls == null) {
         return null;
      } else {
         int currentIndex = 0;
         Method[] var4 = cls.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method m = var4[var6];
            if ((returning == null || m.getReturnType().equals(returning)) && index == currentIndex++) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethod(cls.getSuperclass(), returning, index);
         } else {
            return null;
         }
      }
   }

   public static Method getMethodCheckContainsString(Class<?> cls, String nameContainsThisStr, Class<?> returning) {
      if (cls == null) {
         return null;
      } else {
         Method[] var3 = cls.getDeclaredMethods();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Method m = var3[var5];
            if (m.getName().contains(nameContainsThisStr) && (returning == null || m.getReturnType().equals(returning))) {
               m.setAccessible(true);
               return m;
            }
         }

         if (cls.getSuperclass() != null) {
            return getMethodCheckContainsString(cls.getSuperclass(), nameContainsThisStr, returning);
         } else {
            return null;
         }
      }
   }

   public static List<Method> getMethods(Class<?> cls, String name, Class<?> returning) {
      if (cls == null) {
         return null;
      } else {
         List<Method> methods = new ArrayList();
         Method[] var4 = cls.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method m = var4[var6];
            if (m.getName().equals(name) && (returning == null || m.getReturnType().equals(returning))) {
               m.setAccessible(true);
               methods.add(m);
            }
         }

         if (cls.getSuperclass() != null) {
            methods.addAll(getMethods(cls.getSuperclass(), name, returning));
         }

         return methods;
      }
   }

   @Nullable
   public static Class<?> getClassByNameWithoutException(String name) {
      try {
         return Class.forName(name);
      } catch (ClassNotFoundException ignored) {
         return null;
      }
   }

   public static Constructor<?> getConstructor(Class<?> cls, int index) {
      if (cls == null) {
         return null;
      } else {
         Constructor<?> constructor = cls.getDeclaredConstructors()[index];
         constructor.setAccessible(true);
         return constructor;
      }
   }

   public static Constructor<?> getConstructor(Class<?> cls, Class<?>... params) {
      if (cls == null) {
         return null;
      } else {
         try {
            Constructor<?> c = cls.getDeclaredConstructor(params);
            c.setAccessible(true);
            return c;
         } catch (NoSuchMethodException e) {
            try {
               Constructor<?> c = cls.getConstructor(params);
               c.setAccessible(true);
               return c;
            } catch (NoSuchMethodException ignored) {
               return null;
            }
         }
      }
   }
}
