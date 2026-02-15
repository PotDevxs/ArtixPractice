package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonType;

public interface RepresentationConfigurable<T> {
   BsonType getRepresentation();

   Codec<T> withRepresentation(BsonType var1);
}
