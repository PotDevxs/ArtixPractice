package dev.artixdev.libs.org.bson.io;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import dev.artixdev.libs.org.bson.BsonSerializationException;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.types.ObjectId;

public class ByteBufferBsonInput implements BsonInput {
   private static final String[] ONE_BYTE_ASCII_STRINGS = new String[128];
   private ByteBuf buffer;

   public ByteBufferBsonInput(ByteBuf buffer) {
      if (buffer == null) {
         throw new IllegalArgumentException("buffer can not be null");
      } else {
         this.buffer = buffer;
         buffer.order(ByteOrder.LITTLE_ENDIAN);
      }
   }

   public int getPosition() {
      this.ensureOpen();
      return this.buffer.position();
   }

   public byte readByte() {
      this.ensureOpen();
      this.ensureAvailable(1);
      return this.buffer.get();
   }

   public void readBytes(byte[] bytes) {
      this.ensureOpen();
      this.ensureAvailable(bytes.length);
      this.buffer.get(bytes);
   }

   public void readBytes(byte[] bytes, int offset, int length) {
      this.ensureOpen();
      this.ensureAvailable(length);
      this.buffer.get(bytes, offset, length);
   }

   public long readInt64() {
      this.ensureOpen();
      this.ensureAvailable(8);
      return this.buffer.getLong();
   }

   public double readDouble() {
      this.ensureOpen();
      this.ensureAvailable(8);
      return this.buffer.getDouble();
   }

   public int readInt32() {
      this.ensureOpen();
      this.ensureAvailable(4);
      return this.buffer.getInt();
   }

   public ObjectId readObjectId() {
      this.ensureOpen();
      byte[] bytes = new byte[12];
      this.readBytes(bytes);
      return new ObjectId(bytes);
   }

   public String readString() {
      this.ensureOpen();
      int size = this.readInt32();
      if (size <= 0) {
         throw new BsonSerializationException(String.format("While decoding a BSON string found a size that is not a positive number: %d", size));
      } else {
         this.ensureAvailable(size);
         return this.readString(size);
      }
   }

   public String readCString() {
      int mark = this.buffer.position();
      this.skipCString();
      int size = this.buffer.position() - mark;
      this.buffer.position(mark);
      return this.readString(size);
   }

   private String readString(int size) {
      byte nullByte;
      if (size == 2) {
         byte asciiByte = this.buffer.get();
         nullByte = this.buffer.get();
         if (nullByte != 0) {
            throw new BsonSerializationException("Found a BSON string that is not null-terminated");
         } else {
            return asciiByte < 0 ? StandardCharsets.UTF_8.newDecoder().replacement() : ONE_BYTE_ASCII_STRINGS[asciiByte];
         }
      } else {
         byte[] bytes = new byte[size - 1];
         this.buffer.get(bytes);
         nullByte = this.buffer.get();
         if (nullByte != 0) {
            throw new BsonSerializationException("Found a BSON string that is not null-terminated");
         } else {
            return new String(bytes, StandardCharsets.UTF_8);
         }
      }
   }

   public void skipCString() {
      this.ensureOpen();

      for(boolean checkNext = true; checkNext; checkNext = this.buffer.get() != 0) {
         if (!this.buffer.hasRemaining()) {
            throw new BsonSerializationException("Found a BSON string that is not null-terminated");
         }
      }

   }

   public void skip(int numBytes) {
      this.ensureOpen();
      this.buffer.position(this.buffer.position() + numBytes);
   }

   public BsonInputMark getMark(int readLimit) {
      return new BsonInputMark() {
         private final int mark;

         {
            this.mark = ByteBufferBsonInput.this.buffer.position();
         }

         public void reset() {
            ByteBufferBsonInput.this.ensureOpen();
            ByteBufferBsonInput.this.buffer.position(this.mark);
         }
      };
   }

   public boolean hasRemaining() {
      this.ensureOpen();
      return this.buffer.hasRemaining();
   }

   public void close() {
      this.buffer.release();
      this.buffer = null;
   }

   private void ensureOpen() {
      if (this.buffer == null) {
         throw new IllegalStateException("Stream is closed");
      }
   }

   private void ensureAvailable(int bytesNeeded) {
      if (this.buffer.remaining() < bytesNeeded) {
         throw new BsonSerializationException(String.format("While decoding a BSON document %d bytes were required, but only %d remain", bytesNeeded, this.buffer.remaining()));
      }
   }

   static {
      for(int b = 0; b < ONE_BYTE_ASCII_STRINGS.length; ++b) {
         ONE_BYTE_ASCII_STRINGS[b] = String.valueOf((char)b);
      }

   }
}
