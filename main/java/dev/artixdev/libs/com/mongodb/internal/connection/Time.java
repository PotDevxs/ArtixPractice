package dev.artixdev.libs.com.mongodb.internal.connection;

public final class Time {
   static final long CONSTANT_TIME = 42L;
   private static boolean isConstant;

   public static void makeTimeConstant() {
      isConstant = true;
   }

   public static void makeTimeMove() {
      isConstant = false;
   }

   public static long nanoTime() {
      return isConstant ? 42L : System.nanoTime();
   }

   private Time() {
   }
}
