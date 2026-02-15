package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;

public interface Decoder<T> {
   T decode(BsonReader var1, DecoderContext var2);
}
