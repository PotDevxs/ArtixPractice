package dev.artixdev.libs.org.bson.codecs.pojo;

import dev.artixdev.libs.org.bson.codecs.Codec;

abstract class PojoCodec<T> implements Codec<T> {
   abstract ClassModel<T> getClassModel();

   abstract DiscriminatorLookup getDiscriminatorLookup();
}
