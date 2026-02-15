package dev.artixdev.libs.com.mongodb.internal.operation;

import dev.artixdev.libs.com.mongodb.ExplainVerbosity;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.Decoder;

public interface AsyncExplainableReadOperation<T> extends AsyncReadOperation<T> {
   <R> AsyncReadOperation<R> asAsyncExplainableOperation(@Nullable ExplainVerbosity var1, Decoder<R> var2);
}
