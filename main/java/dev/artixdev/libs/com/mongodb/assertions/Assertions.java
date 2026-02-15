package dev.artixdev.libs.com.mongodb.assertions;

import java.util.Collection;
import java.util.Iterator;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class Assertions {
   public static <T> T notNull(String name, T value) {
      if (value == null) {
         throw new IllegalArgumentException(name + " can not be null");
      } else {
         return value;
      }
   }

   public static <T> Iterable<T> notNullElements(String name, Iterable<T> values) {
      if (values == null) {
         throw new IllegalArgumentException(name + " can not be null");
      } else {
         Iterator<T> iterator = values.iterator();

         Object value;
         do {
            if (!iterator.hasNext()) {
               return values;
            }

            value = iterator.next();
         } while(value != null);

         throw new IllegalArgumentException(name + " can not contain null");
      }
   }

   public static <T> T notNull(String name, T value, SingleResultCallback<?> callback) {
      if (value == null) {
         IllegalArgumentException exception = new IllegalArgumentException(name + " can not be null");
         callback.onResult(null, exception);
         throw exception;
      } else {
         return value;
      }
   }

   public static void isTrue(String name, boolean condition) {
      if (!condition) {
         throw new IllegalStateException("state should be: " + name);
      }
   }

   public static void isTrue(String name, boolean condition, SingleResultCallback<?> callback) {
      if (!condition) {
         IllegalStateException exception = new IllegalStateException("state should be: " + name);
         callback.onResult(null, exception);
         throw exception;
      }
   }

   public static void isTrueArgument(String name, boolean condition) {
      if (!condition) {
         throw new IllegalArgumentException("state should be: " + name);
      }
   }

   public static void doesNotContainNull(String name, Collection<?> collection) {
      Iterator<?> iterator = collection.iterator();

      Object o;
      do {
         if (!iterator.hasNext()) {
            return;
         }

         o = iterator.next();
      } while(o != null);

      throw new IllegalArgumentException(name + " can not contain a null value");
   }

   @Nullable
   public static <T> T assertNull(@Nullable T value) throws AssertionError {
      if (value != null) {
         throw new AssertionError(value.toString());
      } else {
         return null;
      }
   }

   public static <T> T assertNotNull(@Nullable T value) throws AssertionError {
      if (value == null) {
         throw new AssertionError();
      } else {
         return value;
      }
   }

   public static boolean assertTrue(boolean value) throws AssertionError {
      if (!value) {
         throw new AssertionError();
      } else {
         return true;
      }
   }

   public static boolean assertFalse(boolean value) throws AssertionError {
      if (value) {
         throw new AssertionError();
      } else {
         return false;
      }
   }

   public static AssertionError fail() throws AssertionError {
      throw new AssertionError();
   }

   public static AssertionError fail(String msg) throws AssertionError {
      throw new AssertionError(assertNotNull(msg));
   }

   private Assertions() {
   }
}
