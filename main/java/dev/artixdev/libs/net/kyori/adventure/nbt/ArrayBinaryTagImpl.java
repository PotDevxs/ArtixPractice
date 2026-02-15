package dev.artixdev.libs.net.kyori.adventure.nbt;

abstract class ArrayBinaryTagImpl extends AbstractBinaryTag implements ArrayBinaryTag {
   static void checkIndex(int index, int length) {
      if (index < 0 || index >= length) {
         throw new IndexOutOfBoundsException("Index out of bounds: " + index);
      }
   }
}
