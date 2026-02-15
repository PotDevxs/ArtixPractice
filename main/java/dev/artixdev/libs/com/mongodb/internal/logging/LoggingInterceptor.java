package dev.artixdev.libs.com.mongodb.internal.logging;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;

@FunctionalInterface
@ThreadSafe
public interface LoggingInterceptor {
   void intercept(LogMessage var1);
}
