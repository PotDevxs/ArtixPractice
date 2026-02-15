package dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection;

public interface ReflectionObjectWriter {
   void writeBoolean(int var1, boolean var2);

   void writeByte(int var1, byte var2);

   void writeShort(int var1, short var2);

   void writeInt(int var1, int var2);

   void writeLong(int var1, long var2);

   void writeFloat(int var1, float var2);

   void writeDouble(int var1, double var2);

   void writeString(int var1, String var2);

   void writeBooleanArray(int var1, boolean[] var2);

   void writeByteArray(int var1, byte[] var2);

   void writeShortArray(int var1, short[] var2);

   void writeIntArray(int var1, int[] var2);

   void writeLongArray(int var1, long[] var2);

   void writeFloatArray(int var1, float[] var2);

   void writeDoubleArray(int var1, double[] var2);

   void writeStringArray(int var1, String[] var2);

   void writeObject(int var1, Object var2);

   void writeAnyObject(int var1, Object var2);

   void writeEnumConstant(int var1, Enum<?> var2);
}
