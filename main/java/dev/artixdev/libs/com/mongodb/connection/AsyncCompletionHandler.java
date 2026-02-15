package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface AsyncCompletionHandler<T> {
   void completed(@Nullable T var1);

   void failed(Throwable var1);
}
