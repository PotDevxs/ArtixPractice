package dev.artixdev.libs.it.unimi.dsi.fastutil;

public final class Arrays {
   private Arrays() {
   }

   public static void ensureFromTo(int length, int from, int to) {
      if (from < 0) {
         throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
      } else if (from > to) {
         throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
      } else if (to > length) {
         throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than array length (" + length + ")");
      }
   }

   public static void ensureOffsetLength(int arrayLength, int offset, int length) {
      if (offset < 0) {
         throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
      } else if (length < 0) {
         throw new IllegalArgumentException("Length (" + length + ") is negative");
      } else if (length > arrayLength - offset) {
         throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than array length (" + arrayLength + ")");
      }
   }
}
