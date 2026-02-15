package dev.artixdev.libs.com.mongodb.client.gridfs.codecs;

import dev.artixdev.libs.com.mongodb.client.gridfs.model.GridFSFile;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecProvider;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class GridFSFileCodecProvider implements CodecProvider {
   public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
      return clazz.equals(GridFSFile.class) ? (Codec<T>) new GridFSFileCodec(registry) : null;
   }

   public String toString() {
      return "GridFSFileCodecProvider{}";
   }
}
