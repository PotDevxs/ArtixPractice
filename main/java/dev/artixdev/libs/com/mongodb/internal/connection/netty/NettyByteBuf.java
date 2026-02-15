package dev.artixdev.libs.com.mongodb.internal.connection.netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import dev.artixdev.libs.org.bson.ByteBuf;

public final class NettyByteBuf implements ByteBuf {
   private io.netty.buffer.ByteBuf proxied;
   private boolean isWriting;

   public NettyByteBuf(io.netty.buffer.ByteBuf proxied) {
      this.isWriting = true;
      this.proxied = proxied.order(ByteOrder.LITTLE_ENDIAN);
   }

   private NettyByteBuf(io.netty.buffer.ByteBuf proxied, boolean isWriting) {
      this(proxied);
      this.isWriting = isWriting;
   }

   public io.netty.buffer.ByteBuf asByteBuf() {
      return this.proxied;
   }

   public int capacity() {
      return this.proxied.capacity();
   }

   public ByteBuf put(int index, byte b) {
      this.proxied.setByte(index, b);
      return this;
   }

   public int remaining() {
      return this.isWriting ? this.proxied.writableBytes() : this.proxied.readableBytes();
   }

   public ByteBuf put(byte[] src, int offset, int length) {
      this.proxied.writeBytes(src, offset, length);
      return this;
   }

   public boolean hasRemaining() {
      return this.remaining() > 0;
   }

   public ByteBuf put(byte b) {
      this.proxied.writeByte(b);
      return this;
   }

   public ByteBuf flip() {
      this.isWriting = !this.isWriting;
      return this;
   }

   public byte[] array() {
      return this.proxied.array();
   }

   public int limit() {
      return this.isWriting ? this.proxied.writerIndex() + this.remaining() : this.proxied.readerIndex() + this.remaining();
   }

   public ByteBuf position(int newPosition) {
      if (this.isWriting) {
         this.proxied.writerIndex(newPosition);
      } else {
         this.proxied.readerIndex(newPosition);
      }

      return this;
   }

   public ByteBuf clear() {
      this.proxied.clear();
      return this;
   }

   public ByteBuf order(ByteOrder byteOrder) {
      this.proxied = this.proxied.order(byteOrder);
      return this;
   }

   public byte get() {
      return this.proxied.readByte();
   }

   public byte get(int index) {
      return this.proxied.getByte(index);
   }

   public ByteBuf get(byte[] bytes) {
      this.proxied.readBytes(bytes);
      return this;
   }

   public ByteBuf get(int index, byte[] bytes) {
      this.proxied.getBytes(index, bytes);
      return this;
   }

   public ByteBuf get(byte[] bytes, int offset, int length) {
      this.proxied.readBytes(bytes, offset, length);
      return this;
   }

   public ByteBuf get(int index, byte[] bytes, int offset, int length) {
      this.proxied.getBytes(index, bytes, offset, length);
      return this;
   }

   public long getLong() {
      return this.proxied.readLong();
   }

   public long getLong(int index) {
      return this.proxied.getLong(index);
   }

   public double getDouble() {
      return this.proxied.readDouble();
   }

   public double getDouble(int index) {
      return this.proxied.getDouble(index);
   }

   public int getInt() {
      return this.proxied.readInt();
   }

   public int getInt(int index) {
      return this.proxied.getInt(index);
   }

   public int position() {
      return this.isWriting ? this.proxied.writerIndex() : this.proxied.readerIndex();
   }

   public ByteBuf limit(int newLimit) {
      if (this.isWriting) {
         throw new UnsupportedOperationException("Can not set the limit while writing");
      } else {
         this.proxied.writerIndex(newLimit);
         return this;
      }
   }

   public ByteBuf asReadOnly() {
      return this;
   }

   public ByteBuf duplicate() {
      return new NettyByteBuf(this.proxied.duplicate().retain(), this.isWriting);
   }

   public ByteBuffer asNIO() {
      return this.isWriting ? this.proxied.nioBuffer(this.proxied.writerIndex(), this.proxied.writableBytes()) : this.proxied.nioBuffer(this.proxied.readerIndex(), this.proxied.readableBytes());
   }

   public int getReferenceCount() {
      return this.proxied.refCnt();
   }

   public ByteBuf retain() {
      this.proxied.retain();
      return this;
   }

   public void release() {
      this.proxied.release();
   }
}
