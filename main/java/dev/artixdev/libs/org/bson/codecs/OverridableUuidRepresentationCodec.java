package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.UuidRepresentation;

public interface OverridableUuidRepresentationCodec<T> {
   Codec<T> withUuidRepresentation(UuidRepresentation var1);
}
