package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import dev.artixdev.libs.com.mongodb.MongoCompressor;

class ZlibCompressor extends Compressor {
   private final int level;

   ZlibCompressor(MongoCompressor mongoCompressor) {
      this.level = (Integer)mongoCompressor.getPropertyNonNull("LEVEL", -1);
   }

   public String getName() {
      return "zlib";
   }

   public byte getId() {
      return 2;
   }

   InputStream getInputStream(InputStream source) {
      return new InflaterInputStream(source);
   }

   OutputStream getOutputStream(OutputStream source) {
      return new DeflaterOutputStream(source, new Deflater(this.level));
   }
}
