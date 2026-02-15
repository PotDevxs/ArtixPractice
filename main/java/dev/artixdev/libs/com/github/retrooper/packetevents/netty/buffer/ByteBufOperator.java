package dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer;

import java.nio.charset.Charset;

public interface ByteBufOperator {
   int capacity(Object var1);

   Object capacity(Object var1, int var2);

   int readerIndex(Object var1);

   Object readerIndex(Object var1, int var2);

   int writerIndex(Object var1);

   Object writerIndex(Object var1, int var2);

   int readableBytes(Object var1);

   int writableBytes(Object var1);

   Object clear(Object var1);

   byte readByte(Object var1);

   short readShort(Object var1);

   int readInt(Object var1);

   long readUnsignedInt(Object var1);

   long readLong(Object var1);

   void writeByte(Object var1, int var2);

   void writeShort(Object var1, int var2);

   void writeInt(Object var1, int var2);

   void writeLong(Object var1, long var2);

   Object getBytes(Object var1, int var2, byte[] var3);

   short getUnsignedByte(Object var1, int var2);

   boolean isReadable(Object var1);

   Object copy(Object var1);

   Object duplicate(Object var1);

   boolean hasArray(Object var1);

   byte[] array(Object var1);

   Object retain(Object var1);

   Object retainedDuplicate(Object var1);

   Object readSlice(Object var1, int var2);

   Object readBytes(Object var1, byte[] var2, int var3, int var4);

   Object readBytes(Object var1, int var2);

   void readBytes(Object var1, byte[] var2);

   Object writeBytes(Object var1, Object var2);

   Object writeBytes(Object var1, byte[] var2);

   Object writeBytes(Object var1, byte[] var2, int var3, int var4);

   boolean release(Object var1);

   int refCnt(Object var1);

   Object skipBytes(Object var1, int var2);

   String toString(Object var1, int var2, int var3, Charset var4);

   Object markReaderIndex(Object var1);

   Object resetReaderIndex(Object var1);

   Object markWriterIndex(Object var1);

   Object resetWriterIndex(Object var1);

   default float readFloat(Object buffer) {
      return Float.intBitsToFloat(this.readInt(buffer));
   }

   default void writeFloat(Object buffer, float value) {
      this.writeInt(buffer, Float.floatToIntBits(value));
   }

   default double readDouble(Object buffer) {
      return Double.longBitsToDouble(this.readLong(buffer));
   }

   default void writeDouble(Object buffer, double value) {
      this.writeLong(buffer, Double.doubleToLongBits(value));
   }

   default char readChar(Object buffer) {
      return (char)this.readShort(buffer);
   }

   default void writeChar(Object buffer, int value) {
      this.writeShort(buffer, value);
   }

   default int readUnsignedShort(Object buffer) {
      return this.readShort(buffer) & '\uffff';
   }

   default short readUnsignedByte(Object buffer) {
      return (short)(this.readByte(buffer) & 255);
   }

   default boolean readBoolean(Object buffer) {
      return this.readByte(buffer) != 0;
   }

   default void writeBoolean(Object buffer, boolean value) {
      this.writeByte(buffer, value ? 1 : 0);
   }
}
