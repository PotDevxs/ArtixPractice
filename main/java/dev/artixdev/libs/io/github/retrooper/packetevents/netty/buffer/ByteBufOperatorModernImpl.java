package dev.artixdev.libs.io.github.retrooper.packetevents.netty.buffer;

import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;

public class ByteBufOperatorModernImpl implements ByteBufOperator {
   public int capacity(Object buffer) {
      return ((ByteBuf)buffer).capacity();
   }

   public Object capacity(Object buffer, int capacity) {
      return ((ByteBuf)buffer).capacity(capacity);
   }

   public int readerIndex(Object buffer) {
      return ((ByteBuf)buffer).readerIndex();
   }

   public Object readerIndex(Object buffer, int readerIndex) {
      return ((ByteBuf)buffer).readerIndex(readerIndex);
   }

   public int writerIndex(Object buffer) {
      return ((ByteBuf)buffer).writerIndex();
   }

   public Object writerIndex(Object buffer, int writerIndex) {
      return ((ByteBuf)buffer).writerIndex(writerIndex);
   }

   public int readableBytes(Object buffer) {
      return ((ByteBuf)buffer).readableBytes();
   }

   public int writableBytes(Object buffer) {
      return ((ByteBuf)buffer).writableBytes();
   }

   public Object clear(Object buffer) {
      return ((ByteBuf)buffer).clear();
   }

   public byte readByte(Object buffer) {
      return ((ByteBuf)buffer).readByte();
   }

   public short readShort(Object buffer) {
      return ((ByteBuf)buffer).readShort();
   }

   public int readInt(Object buffer) {
      return ((ByteBuf)buffer).readInt();
   }

   public long readUnsignedInt(Object buffer) {
      return ((ByteBuf)buffer).readUnsignedInt();
   }

   public long readLong(Object buffer) {
      return ((ByteBuf)buffer).readLong();
   }

   public void writeByte(Object buffer, int value) {
      ((ByteBuf)buffer).writeByte(value);
   }

   public void writeShort(Object buffer, int value) {
      ((ByteBuf)buffer).writeShort(value);
   }

   public void writeInt(Object buffer, int value) {
      ((ByteBuf)buffer).writeInt(value);
   }

   public void writeLong(Object buffer, long value) {
      ((ByteBuf)buffer).writeLong(value);
   }

   public Object getBytes(Object buffer, int index, byte[] destination) {
      return ((ByteBuf)buffer).getBytes(index, destination);
   }

   public short getUnsignedByte(Object buffer, int index) {
      return ((ByteBuf)buffer).getUnsignedByte(index);
   }

   public boolean isReadable(Object buffer) {
      return ((ByteBuf)buffer).isReadable();
   }

   public Object copy(Object buffer) {
      return ((ByteBuf)buffer).copy();
   }

   public Object duplicate(Object buffer) {
      return ((ByteBuf)buffer).duplicate();
   }

   public boolean hasArray(Object buffer) {
      return ((ByteBuf)buffer).hasArray();
   }

   public byte[] array(Object buffer) {
      return ((ByteBuf)buffer).array();
   }

   public Object retain(Object buffer) {
      return ((ByteBuf)buffer).retain();
   }

   public Object retainedDuplicate(Object buffer) {
      return ((ByteBuf)buffer).duplicate().retain();
   }

   public Object readSlice(Object buffer, int length) {
      return ((ByteBuf)buffer).readSlice(length);
   }

   public Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length) {
      return ((ByteBuf)buffer).readBytes(destination, destinationIndex, length);
   }

   public Object readBytes(Object buffer, int length) {
      return ((ByteBuf)buffer).readBytes(length);
   }

   public Object writeBytes(Object buffer, Object src) {
      return ((ByteBuf)buffer).writeBytes((ByteBuf)src);
   }

   public Object writeBytes(Object buffer, byte[] bytes) {
      return ((ByteBuf)buffer).writeBytes(bytes);
   }

   public Object writeBytes(Object buffer, byte[] bytes, int offset, int length) {
      return ((ByteBuf)buffer).writeBytes(bytes, offset, length);
   }

   public void readBytes(Object buffer, byte[] bytes) {
      ((ByteBuf)buffer).readBytes(bytes);
   }

   public boolean release(Object buffer) {
      return ((ByteBuf)buffer).release();
   }

   public int refCnt(Object buffer) {
      return ((ByteBuf)buffer).refCnt();
   }

   public Object skipBytes(Object buffer, int length) {
      return ((ByteBuf)buffer).skipBytes(length);
   }

   public String toString(Object buffer, int index, int length, Charset charset) {
      return ((ByteBuf)buffer).toString(index, length, charset);
   }

   public Object markReaderIndex(Object buffer) {
      return ((ByteBuf)buffer).markReaderIndex();
   }

   public Object resetReaderIndex(Object buffer) {
      return ((ByteBuf)buffer).resetReaderIndex();
   }

   public Object markWriterIndex(Object buffer) {
      return ((ByteBuf)buffer).markWriterIndex();
   }

   public Object resetWriterIndex(Object buffer) {
      return ((ByteBuf)buffer).resetWriterIndex();
   }
}
