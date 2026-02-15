package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.BsonOutput;

abstract class Compressor {
   static final int BUFFER_SIZE = 256;

   abstract String getName();

   abstract byte getId();

   void compress(List<ByteBuf> source, BsonOutput target) {
      Compressor.BufferExposingByteArrayOutputStream baos = new Compressor.BufferExposingByteArrayOutputStream(1024);

      try {
         OutputStream outputStream = this.getOutputStream(baos);

         try {
            byte[] scratch = new byte[256];
            Iterator var6 = source.iterator();

            while(var6.hasNext()) {
               ByteBuf cur = (ByteBuf)var6.next();

               while(cur.hasRemaining()) {
                  int numBytes = Math.min(cur.remaining(), scratch.length);
                  cur.get(scratch, 0, numBytes);
                  outputStream.write(scratch, 0, numBytes);
               }
            }
         } catch (Throwable e) {
            if (outputStream != null) {
               try {
                  outputStream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (outputStream != null) {
            outputStream.close();
         }
      } catch (IOException e) {
         throw new MongoInternalException("Unexpected IOException", e);
      }

      target.writeBytes(baos.getInternalBytes(), 0, baos.size());
   }

   void uncompress(ByteBuf source, ByteBuf target) {
      try {
         InputStream inputStream = this.getInputStream(new Compressor.ByteBufInputStream(source));

         try {
            byte[] scratch = new byte[256];

            for(int numBytes = inputStream.read(scratch); numBytes != -1; numBytes = inputStream.read(scratch)) {
               target.put(scratch, 0, numBytes);
            }
         } catch (Throwable e) {
            if (inputStream != null) {
               try {
                  inputStream.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (inputStream != null) {
            inputStream.close();
         }

      } catch (IOException e) {
         throw new MongoInternalException("Unexpected IOException", e);
      }
   }

   OutputStream getOutputStream(OutputStream source) throws IOException {
      throw new UnsupportedEncodingException();
   }

   InputStream getInputStream(InputStream source) throws IOException {
      throw new UnsupportedOperationException();
   }

   private static final class BufferExposingByteArrayOutputStream extends ByteArrayOutputStream {
      BufferExposingByteArrayOutputStream(int size) {
         super(size);
      }

      byte[] getInternalBytes() {
         return this.buf;
      }
   }

   private static final class ByteBufInputStream extends InputStream {
      private final ByteBuf source;

      ByteBufInputStream(ByteBuf source) {
         this.source = source;
      }

      public int read(byte[] bytes, int offset, int length) {
         if (!this.source.hasRemaining()) {
            return -1;
         } else {
            int bytesToRead = length > this.source.remaining() ? this.source.remaining() : length;
            this.source.get(bytes, offset, bytesToRead);
            return bytesToRead;
         }
      }

      public int read() {
         throw new UnsupportedOperationException();
      }
   }
}
