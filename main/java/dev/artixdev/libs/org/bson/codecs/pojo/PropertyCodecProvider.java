package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.codecs.Codec;

public interface PropertyCodecProvider {
   <T> Codec<T> get(TypeWithTypeParameters<T> var1, PropertyCodecRegistry var2);
}
