package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.BufferProvider;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.OutputBuffer;

public class ByteBufferBsonOutput extends OutputBuffer {
   private static final int MAX_SHIFT = 31;
   private static final int INITIAL_SHIFT = 10;
   public static final int INITIAL_BUFFER_SIZE = 1024;
   public static final int MAX_BUFFER_SIZE = 16777216;
   private final BufferProvider bufferProvider;
   private final List<ByteBuf> bufferList = new ArrayList();
   private int curBufferIndex = 0;
   private int position = 0;
   private boolean closed;

   public ByteBufferBsonOutput(BufferProvider bufferProvider) {
      this.bufferProvider = (BufferProvider)Assertions.notNull("bufferProvider", bufferProvider);
   }

   public void writeBytes(byte[] bytes, int offset, int length) {
      this.ensureOpen();
      int currentOffset = offset;

      int bytesToPutInCurrentBuffer;
      for(int remainingLen = length; remainingLen > 0; currentOffset += bytesToPutInCurrentBuffer) {
         ByteBuf buf = this.getCurrentByteBuffer();
         bytesToPutInCurrentBuffer = Math.min(buf.remaining(), remainingLen);
         buf.put(bytes, currentOffset, bytesToPutInCurrentBuffer);
         remainingLen -= bytesToPutInCurrentBuffer;
      }

      this.position += length;
   }

   public void writeByte(int value) {
      this.ensureOpen();
      this.getCurrentByteBuffer().put((byte)value);
      ++this.position;
   }

   private ByteBuf getCurrentByteBuffer() {
      ByteBuf curByteBuffer = this.getByteBufferAtIndex(this.curBufferIndex);
      if (curByteBuffer.hasRemaining()) {
         return curByteBuffer;
      } else {
         ++this.curBufferIndex;
         return this.getByteBufferAtIndex(this.curBufferIndex);
      }
   }

   private ByteBuf getByteBufferAtIndex(int index) {
      if (this.bufferList.size() < index + 1) {
         this.bufferList.add(this.bufferProvider.getBuffer(index >= 21 ? 16777216 : Math.min(1024 << index, 16777216)));
      }

      return (ByteBuf)this.bufferList.get(index);
   }

   public int getPosition() {
      this.ensureOpen();
      return this.position;
   }

   public int getSize() {
      this.ensureOpen();
      return this.position;
   }

   protected void write(int absolutePosition, int value) {
      this.ensureOpen();
      if (absolutePosition < 0) {
         throw new IllegalArgumentException(String.format("position must be >= 0 but was %d", absolutePosition));
      } else if (absolutePosition > this.position - 1) {
         throw new IllegalArgumentException(String.format("position must be <= %d but was %d", this.position - 1, absolutePosition));
      } else {
         ByteBufferBsonOutput.BufferPositionPair bufferPositionPair = this.getBufferPositionPair(absolutePosition);
         ByteBuf byteBuffer = this.getByteBufferAtIndex(bufferPositionPair.bufferIndex);
         byteBuffer.put(bufferPositionPair.position++, (byte)value);
      }
   }

   public List<ByteBuf> getByteBuffers() {
      this.ensureOpen();
      List<ByteBuf> buffers = new ArrayList(this.bufferList.size());
      Iterator var2 = this.bufferList.iterator();

      while(var2.hasNext()) {
         ByteBuf cur = (ByteBuf)var2.next();
         buffers.add(cur.duplicate().order(ByteOrder.LITTLE_ENDIAN).flip());
      }

      return buffers;
   }

   public int pipe(OutputStream out) throws IOException {
      this.ensureOpen();
      byte[] tmp = new byte[1024];
      int total = 0;

      ByteBuf dup;
      for(Iterator var4 = this.getByteBuffers().iterator(); var4.hasNext(); total += dup.limit()) {
         ByteBuf cur = (ByteBuf)var4.next();
         dup = cur.duplicate();

         while(dup.hasRemaining()) {
            int numBytesToCopy = Math.min(dup.remaining(), tmp.length);
            dup.get(tmp, 0, numBytesToCopy);
            out.write(tmp, 0, numBytesToCopy);
         }
      }

      return total;
   }

   public void truncateToPosition(int newPosition) {
      this.ensureOpen();
      if (newPosition <= this.position && newPosition >= 0) {
         ByteBufferBsonOutput.BufferPositionPair bufferPositionPair = this.getBufferPositionPair(newPosition);
         ((ByteBuf)this.bufferList.get(bufferPositionPair.bufferIndex)).position(bufferPositionPair.position);

         while(this.bufferList.size() > bufferPositionPair.bufferIndex + 1) {
            ByteBuf buffer = (ByteBuf)this.bufferList.remove(this.bufferList.size() - 1);
            buffer.release();
         }

         this.curBufferIndex = bufferPositionPair.bufferIndex;
         this.position = newPosition;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void close() {
      Iterator var1 = this.bufferList.iterator();

      while(var1.hasNext()) {
         ByteBuf cur = (ByteBuf)var1.next();
         cur.release();
      }

      this.bufferList.clear();
      this.closed = true;
   }

   private ByteBufferBsonOutput.BufferPositionPair getBufferPositionPair(int absolutePosition) {
      int positionInBuffer = absolutePosition;
      int bufferIndex = 0;
      int bufferSize = 1024;

      for(int startPositionOfBuffer = 0; startPositionOfBuffer + bufferSize <= absolutePosition; bufferSize = ((ByteBuf)this.bufferList.get(bufferIndex)).limit()) {
         ++bufferIndex;
         startPositionOfBuffer += bufferSize;
         positionInBuffer -= bufferSize;
      }

      return new ByteBufferBsonOutput.BufferPositionPair(bufferIndex, positionInBuffer);
   }

   private void ensureOpen() {
      if (this.closed) {
         throw new IllegalStateException("The output is closed");
      }
   }

   private static final class BufferPositionPair {
      private final int bufferIndex;
      private int position;

      BufferPositionPair(int bufferIndex, int position) {
         this.bufferIndex = bufferIndex;
         this.position = position;
      }
   }
}
