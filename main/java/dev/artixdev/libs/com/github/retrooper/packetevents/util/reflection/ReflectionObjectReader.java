package dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection;

public interface ReflectionObjectReader {
   boolean readBoolean(int var1);

   byte readByte(int var1);

   short readShort(int var1);

   int readInt(int var1);

   long readLong(int var1);

   float readFloat(int var1);

   double readDouble(int var1);

   boolean[] readBooleanArray(int var1);

   byte[] readByteArray(int var1);

   short[] readShortArray(int var1);

   int[] readIntArray(int var1);

   long[] readLongArray(int var1);

   float[] readFloatArray(int var1);

   double[] readDoubleArray(int var1);

   String[] readStringArray(int var1);

   String readString(int var1);

   <T> T readObject(int var1, Class<? extends T> var2);

   Enum<?> readEnumConstant(int var1, Class<? extends Enum<?>> var2);

   Object readAnyObject(int var1);
}
