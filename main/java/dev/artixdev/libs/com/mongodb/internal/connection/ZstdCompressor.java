package dev.artixdev.libs.com.mongodb.internal.connection;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.io.BsonOutput;

class ZstdCompressor extends Compressor {
   public String getName() {
      return "zstd";
   }

   public byte getId() {
      return 3;
   }

   public void compress(List<ByteBuf> source, BsonOutput target) {
      int uncompressedSize = this.getUncompressedSize(source);
      byte[] singleByteArraySource = new byte[uncompressedSize];
      this.copy(source, singleByteArraySource);

      try {
         byte[] out = new byte[(int)Zstd.compressBound((long)uncompressedSize)];
         int compressedSize = (int)Zstd.compress(out, singleByteArraySource, 3);
         target.writeBytes(out, 0, compressedSize);
      } catch (Exception exception) {
         throw new MongoInternalException("Unexpected exception", exception);
      }
   }

   private int getUncompressedSize(List<ByteBuf> source) {
      int uncompressedSize = 0;

      ByteBuf cur;
      for(Iterator<ByteBuf> iterator = source.iterator(); iterator.hasNext(); uncompressedSize += cur.remaining()) {
         cur = iterator.next();
      }

      return uncompressedSize;
   }

   private void copy(List<ByteBuf> source, byte[] in) {
      int offset = 0;

      int remaining;
      for(Iterator<ByteBuf> iterator = source.iterator(); iterator.hasNext(); offset += remaining) {
         ByteBuf cur = iterator.next();
         remaining = cur.remaining();
         cur.get(in, offset, remaining);
      }

   }

   InputStream getInputStream(InputStream source) throws IOException {
      return new ZstdInputStream(source);
   }
}
