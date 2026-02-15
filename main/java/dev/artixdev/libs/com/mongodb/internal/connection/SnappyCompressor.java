package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyInputStream;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.BsonOutput;

class SnappyCompressor extends Compressor {
   public String getName() {
      return "snappy";
   }

   public byte getId() {
      return 1;
   }

   public void compress(List<ByteBuf> source, BsonOutput target) {
      int uncompressedSize = this.getUncompressedSize(source);
      byte[] singleByteArraySource = new byte[uncompressedSize];
      this.copy(source, singleByteArraySource);

      try {
         byte[] out = new byte[Snappy.maxCompressedLength(uncompressedSize)];
         int compressedSize = Snappy.compress(singleByteArraySource, 0, singleByteArraySource.length, out, 0);
         target.writeBytes(out, 0, compressedSize);
      } catch (IOException e) {
         throw new MongoInternalException("Unexpected IOException", e);
      }
   }

   private int getUncompressedSize(List<ByteBuf> source) {
      int uncompressedSize = 0;

      ByteBuf cur;
      for(Iterator var3 = source.iterator(); var3.hasNext(); uncompressedSize += cur.remaining()) {
         cur = (ByteBuf)var3.next();
      }

      return uncompressedSize;
   }

   private void copy(List<ByteBuf> source, byte[] in) {
      int offset = 0;

      int remaining;
      for(Iterator var4 = source.iterator(); var4.hasNext(); offset += remaining) {
         ByteBuf cur = (ByteBuf)var4.next();
         remaining = cur.remaining();
         cur.get(in, offset, remaining);
      }

   }

   InputStream getInputStream(InputStream source) throws IOException {
      return new SnappyInputStream(source);
   }
}
