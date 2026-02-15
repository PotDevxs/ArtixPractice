package dev.artixdev.libs.org.bson;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

public class ByteBufNIO implements ByteBuf {
   private ByteBuffer buf;
   private final AtomicInteger referenceCount = new AtomicInteger(1);

   public ByteBufNIO(ByteBuffer buf) {
      this.buf = buf.order(ByteOrder.LITTLE_ENDIAN);
   }

   public int getReferenceCount() {
      return this.referenceCount.get();
   }

   public ByteBufNIO retain() {
      if (this.referenceCount.incrementAndGet() == 1) {
         this.referenceCount.decrementAndGet();
         throw new IllegalStateException("Attempted to increment the reference count when it is already 0");
      } else {
         return this;
      }
   }

   public void release() {
      if (this.referenceCount.decrementAndGet() < 0) {
         this.referenceCount.incrementAndGet();
         throw new IllegalStateException("Attempted to decrement the reference count below 0");
      } else {
         if (this.referenceCount.get() == 0) {
            this.buf = null;
         }

      }
   }

   public int capacity() {
      return this.buf.capacity();
   }

   public ByteBuf put(int index, byte b) {
      this.buf.put(index, b);
      return this;
   }

   public int remaining() {
      return this.buf.remaining();
   }

   public ByteBuf put(byte[] src, int offset, int length) {
      this.buf.put(src, offset, length);
      return this;
   }

   public boolean hasRemaining() {
      return this.buf.hasRemaining();
   }

   public ByteBuf put(byte b) {
      this.buf.put(b);
      return this;
   }

   public ByteBuf flip() {
      this.buf.flip();
      return this;
   }

   public byte[] array() {
      return this.buf.array();
   }

   public int limit() {
      return this.buf.limit();
   }

   public ByteBuf position(int newPosition) {
      this.buf.position(newPosition);
      return this;
   }

   public ByteBuf clear() {
      this.buf.clear();
      return this;
   }

   public ByteBuf order(ByteOrder byteOrder) {
      this.buf.order(byteOrder);
      return this;
   }

   public byte get() {
      return this.buf.get();
   }

   public byte get(int index) {
      return this.buf.get(index);
   }

   public ByteBuf get(byte[] bytes) {
      this.buf.get(bytes);
      return this;
   }

   public ByteBuf get(int index, byte[] bytes) {
      return this.get(index, bytes, 0, bytes.length);
   }

   public ByteBuf get(byte[] bytes, int offset, int length) {
      this.buf.get(bytes, offset, length);
      return this;
   }

   public ByteBuf get(int index, byte[] bytes, int offset, int length) {
      for(int i = 0; i < length; ++i) {
         bytes[offset + i] = this.buf.get(index + i);
      }

      return this;
   }

   public long getLong() {
      return this.buf.getLong();
   }

   public long getLong(int index) {
      return this.buf.getLong(index);
   }

   public double getDouble() {
      return this.buf.getDouble();
   }

   public double getDouble(int index) {
      return this.buf.getDouble(index);
   }

   public int getInt() {
      return this.buf.getInt();
   }

   public int getInt(int index) {
      return this.buf.getInt(index);
   }

   public int position() {
      return this.buf.position();
   }

   public ByteBuf limit(int newLimit) {
      this.buf.limit(newLimit);
      return this;
   }

   public ByteBuf asReadOnly() {
      return new ByteBufNIO(this.buf.asReadOnlyBuffer());
   }

   public ByteBuf duplicate() {
      return new ByteBufNIO(this.buf.duplicate());
   }

   public ByteBuffer asNIO() {
      return this.buf;
   }
}
