package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public interface ExplainableReadOperation<T> extends ReadOperation<T> {
   <R> ReadOperation<R> asExplainableOperation(@Nullable ExplainVerbosity var1, Decoder<R> var2);
}
