package dev.artixdev.libs.org.bson.internal;

import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

interface CycleDetectingCodecRegistry extends CodecRegistry {
   <T> Codec<T> get(ChildCodecRegistry<T> var1);
}
