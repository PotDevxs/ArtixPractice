package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.org.bson.ByteBuf;

/** @deprecated */
@Deprecated
@ThreadSafe
public interface BufferProvider {
   ByteBuf getBuffer(int var1);
}
