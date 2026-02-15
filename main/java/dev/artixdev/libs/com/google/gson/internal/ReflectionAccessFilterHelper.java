package dev.artixdev.libs.com.google.gson.internal;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.google.gson.ReflectionAccessFilter;

public class ReflectionAccessFilterHelper {
   private ReflectionAccessFilterHelper() {
   }

   public static boolean isJavaType(Class<?> c) {
      return isJavaType(c.getName());
   }

   private static boolean isJavaType(String className) {
      return className.startsWith("java.") || className.startsWith("javax.");
   }

   public static boolean isAndroidType(Class<?> c) {
      return isAndroidType(c.getName());
   }

   private static boolean isAndroidType(String className) {
      return className.startsWith("android.") || className.startsWith("androidx.") || isJavaType(className);
   }

   public static boolean isAnyPlatformType(Class<?> c) {
      String className = c.getName();
      return isAndroidType(className) || className.startsWith("kotlin.") || className.startsWith("kotlinx.") || className.startsWith("scala.");
   }

   public static ReflectionAccessFilter.FilterResult getFilterResult(List<ReflectionAccessFilter> reflectionFilters, Class<?> c) {
      Iterator<ReflectionAccessFilter> filterIterator = reflectionFilters.iterator();

      ReflectionAccessFilter.FilterResult result;
      do {
         if (!filterIterator.hasNext()) {
            return ReflectionAccessFilter.FilterResult.ALLOW;
         }

         ReflectionAccessFilter filter = filterIterator.next();
         result = filter.check(c);
      } while(result == ReflectionAccessFilter.FilterResult.INDECISIVE);

      return result;
   }

   public static boolean canAccess(AccessibleObject accessibleObject, Object object) {
      return ReflectionAccessFilterHelper.AccessChecker.INSTANCE.canAccess(accessibleObject, object);
   }

   private abstract static class AccessChecker {
      public static final ReflectionAccessFilterHelper.AccessChecker INSTANCE;

      private AccessChecker() {
      }

      public abstract boolean canAccess(AccessibleObject accessibleObject, Object object);

      AccessChecker(Object ignored) {
         this();
      }

      static {
         ReflectionAccessFilterHelper.AccessChecker accessChecker = null;
         if (JavaVersion.isJava9OrLater()) {
            try {
               final Method canAccessMethod = AccessibleObject.class.getDeclaredMethod("canAccess", Object.class);
               accessChecker = new ReflectionAccessFilterHelper.AccessChecker() {
                  public boolean canAccess(AccessibleObject accessibleObject, Object object) {
                     try {
                        return (Boolean)canAccessMethod.invoke(accessibleObject, object);
                     } catch (Exception e) {
                        throw new RuntimeException("Failed invoking canAccess", e);
                     }
                  }
               };
            } catch (NoSuchMethodException ignored) {
            }
         }

         if (accessChecker == null) {
            accessChecker = new ReflectionAccessFilterHelper.AccessChecker() {
               public boolean canAccess(AccessibleObject accessibleObject, Object object) {
                  return true;
               }
            };
         }

         INSTANCE = accessChecker;
      }
   }
}
