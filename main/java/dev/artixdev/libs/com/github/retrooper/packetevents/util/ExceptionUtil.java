package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Set;

public class ExceptionUtil {
   public static boolean isException(Throwable t, Class<?> clazz) {
      while(t != null) {
         if (clazz.isAssignableFrom(t.getClass())) {
            return true;
         }

         t = t.getCause();
      }

      return false;
   }

   public static boolean isExceptionContainedIn(Throwable t, Set<Class<? extends Throwable>> exceptions) {
      return exceptions.contains(t.getClass());
   }
}
