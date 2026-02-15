package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonWriter;

public interface Encoder<T> {
   void encode(BsonWriter var1, T var2, EncoderContext var3);

   Class<T> getEncoderClass();
}
