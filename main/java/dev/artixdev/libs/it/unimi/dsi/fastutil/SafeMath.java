package dev.artixdev.libs.it.unimi.dsi.fastutil;

/**
 * Safe conversion methods between primitive types that throw on overflow/out-of-range.
 */
public final class SafeMath {
   private SafeMath() {
   }

   public static byte safeIntToByte(int value) {
      if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
         throw new ArithmeticException("int value " + value + " out of byte range");
      }
      return (byte) value;
   }

   public static short safeIntToShort(int value) {
      if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
         throw new ArithmeticException("int value " + value + " out of short range");
      }
      return (short) value;
   }

   public static char safeIntToChar(int value) {
      if (value < Character.MIN_VALUE || value > Character.MAX_VALUE) {
         throw new ArithmeticException("int value " + value + " out of char range");
      }
      return (char) value;
   }

   public static int safeLongToInt(long value) {
      if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
         throw new ArithmeticException("long value " + value + " out of int range");
      }
      return (int) value;
   }

   public static float safeDoubleToFloat(double value) {
      if (Double.isFinite(value) && (value > Float.MAX_VALUE || value < -Float.MAX_VALUE)) {
         throw new ArithmeticException("double value " + value + " out of float range");
      }
      return (float) value;
   }
}
