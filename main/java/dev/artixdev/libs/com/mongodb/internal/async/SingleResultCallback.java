package dev.artixdev.libs.com.mongodb.internal.async;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface SingleResultCallback<T> {
   void onResult(@Nullable T var1, @Nullable Throwable var2);
}
