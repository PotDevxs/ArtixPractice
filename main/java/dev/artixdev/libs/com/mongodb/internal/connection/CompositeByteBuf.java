package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.assertions.Assertions;

class CompositeByteBuf implements ByteBuf {
   private final List<CompositeByteBuf.Component> components;
   private final AtomicInteger referenceCount = new AtomicInteger(1);
   private int position;
   private int limit;

   CompositeByteBuf(List<ByteBuf> buffers) {
      Assertions.notNull("buffers", buffers);
      Assertions.isTrueArgument("buffer list not empty", !buffers.isEmpty());
      this.components = new ArrayList(buffers.size());
      int offset = 0;

      CompositeByteBuf.Component component;
      for(Iterator var3 = buffers.iterator(); var3.hasNext(); offset = component.endOffset) {
         ByteBuf cur = (ByteBuf)var3.next();
         component = new CompositeByteBuf.Component(cur.asReadOnly().order(ByteOrder.LITTLE_ENDIAN), offset);
         this.components.add(component);
      }

      this.limit = ((CompositeByteBuf.Component)this.components.get(this.components.size() - 1)).endOffset;
   }

   CompositeByteBuf(CompositeByteBuf from) {
      this.components = from.components;
      this.position = from.position();
      this.limit = from.limit();
   }

   public ByteBuf order(ByteOrder byteOrder) {
      if (byteOrder == ByteOrder.BIG_ENDIAN) {
         throw new UnsupportedOperationException(String.format("Only %s is supported", ByteOrder.BIG_ENDIAN));
      } else {
         return this;
      }
   }

   public int capacity() {
      return ((CompositeByteBuf.Component)this.components.get(this.components.size() - 1)).endOffset;
   }

   public int remaining() {
      return this.limit() - this.position();
   }

   public boolean hasRemaining() {
      return this.remaining() > 0;
   }

   public int position() {
      return this.position;
   }

   public ByteBuf position(int newPosition) {
      if (newPosition >= 0 && newPosition <= this.limit) {
         this.position = newPosition;
         return this;
      } else {
         throw new IndexOutOfBoundsException(String.format("%d is out of bounds", newPosition));
      }
   }

   public ByteBuf clear() {
      this.position = 0;
      this.limit = this.capacity();
      return this;
   }

   public int limit() {
      return this.limit;
   }

   public byte get() {
      this.checkIndex(this.position);
      ++this.position;
      return this.get(this.position - 1);
   }

   public byte get(int index) {
      this.checkIndex(index);
      CompositeByteBuf.Component component = this.findComponent(index);
      return component.buffer.get(index - component.offset);
   }

   public ByteBuf get(byte[] bytes) {
      this.checkIndex(this.position, bytes.length);
      this.position += bytes.length;
      return this.get(this.position - bytes.length, bytes);
   }

   public ByteBuf get(int index, byte[] bytes) {
      return this.get(index, bytes, 0, bytes.length);
   }

   public ByteBuf get(byte[] bytes, int offset, int length) {
      this.checkIndex(this.position, length);
      this.position += length;
      return this.get(this.position - length, bytes, offset, length);
   }

   public ByteBuf get(int index, byte[] bytes, int offset, int length) {
      this.checkDstIndex(index, length, offset, bytes.length);
      int i = this.findComponentIndex(index);
      int curIndex = index;
      int curOffset = offset;

      for(int curLength = length; curLength > 0; ++i) {
         CompositeByteBuf.Component c = (CompositeByteBuf.Component)this.components.get(i);
         int localLength = Math.min(curLength, c.buffer.capacity() - (curIndex - c.offset));
         c.buffer.get(curIndex - c.offset, bytes, curOffset, localLength);
         curIndex += localLength;
         curOffset += localLength;
         curLength -= localLength;
      }

      return this;
   }

   public long getLong() {
      this.position += 8;
      return this.getLong(this.position - 8);
   }

   public long getLong(int index) {
      this.checkIndex(index, 8);
      CompositeByteBuf.Component component = this.findComponent(index);
      return index + 8 <= component.endOffset ? component.buffer.getLong(index - component.offset) : (long)this.getInt(index) & 4294967295L | ((long)this.getInt(index + 4) & 4294967295L) << 32;
   }

   public double getDouble() {
      this.position += 8;
      return this.getDouble(this.position - 8);
   }

   public double getDouble(int index) {
      return Double.longBitsToDouble(this.getLong(index));
   }

   public int getInt() {
      this.position += 4;
      return this.getInt(this.position - 4);
   }

   public int getInt(int index) {
      this.checkIndex(index, 4);
      CompositeByteBuf.Component component = this.findComponent(index);
      return index + 4 <= component.endOffset ? component.buffer.getInt(index - component.offset) : this.getShort(index) & '\uffff' | (this.getShort(index + 2) & '\uffff') << 16;
   }

   private int getShort(int index) {
      this.checkIndex(index, 2);
      return (short)(this.get(index) & 255 | (this.get(index + 1) & 255) << 8);
   }

   public byte[] array() {
      throw new UnsupportedOperationException("Not implemented yet!");
   }

   public ByteBuf limit(int newLimit) {
      if (newLimit >= 0 && newLimit <= this.capacity()) {
         this.limit = newLimit;
         return this;
      } else {
         throw new IndexOutOfBoundsException(String.format("%d is out of bounds", newLimit));
      }
   }

   public ByteBuf put(int index, byte b) {
      throw new UnsupportedOperationException();
   }

   public ByteBuf put(byte[] src, int offset, int length) {
      throw new UnsupportedOperationException();
   }

   public ByteBuf put(byte b) {
      throw new UnsupportedOperationException();
   }

   public ByteBuf flip() {
      throw new UnsupportedOperationException();
   }

   public ByteBuf asReadOnly() {
      throw new UnsupportedOperationException();
   }

   public ByteBuf duplicate() {
      return new CompositeByteBuf(this);
   }

   public ByteBuffer asNIO() {
      if (this.components.size() == 1) {
         ByteBuffer byteBuffer = ((CompositeByteBuf.Component)this.components.get(0)).buffer.asNIO().duplicate();
         byteBuffer.position(this.position).limit(this.limit);
         return byteBuffer;
      } else {
         byte[] bytes = new byte[this.remaining()];
         this.get(this.position, bytes, 0, bytes.length);
         return ByteBuffer.wrap(bytes);
      }
   }

   public int getReferenceCount() {
      return this.referenceCount.get();
   }

   public ByteBuf retain() {
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
      }
   }

   private CompositeByteBuf.Component findComponent(int index) {
      return (CompositeByteBuf.Component)this.components.get(this.findComponentIndex(index));
   }

   private int findComponentIndex(int index) {
      for(int i = this.components.size() - 1; i >= 0; --i) {
         CompositeByteBuf.Component cur = (CompositeByteBuf.Component)this.components.get(i);
         if (index >= cur.offset) {
            return i;
         }
      }

      throw new IndexOutOfBoundsException(String.format("%d is out of bounds", index));
   }

   private void checkIndex(int index) {
      this.ensureAccessible();
      if (index < 0 || index >= this.capacity()) {
         throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", index, this.capacity()));
      }
   }

   private void checkIndex(int index, int fieldLength) {
      this.ensureAccessible();
      if (index < 0 || index > this.capacity() - fieldLength) {
         throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", index, fieldLength, this.capacity()));
      }
   }

   private void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
      this.checkIndex(index, length);
      if (dstIndex < 0 || dstIndex > dstCapacity - length) {
         throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dstCapacity));
      }
   }

   private void ensureAccessible() {
      if (this.referenceCount.get() == 0) {
         throw new IllegalStateException("Reference count is 0");
      }
   }

   private static final class Component {
      private final ByteBuf buffer;
      private final int length;
      private final int offset;
      private final int endOffset;

      Component(ByteBuf buffer, int offset) {
         this.buffer = buffer;
         this.length = buffer.limit() - buffer.position();
         this.offset = offset;
         this.endOffset = offset + this.length;
      }
   }
}
