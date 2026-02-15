package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface CommandProtocol<T> {
   @Nullable
   T execute(InternalConnection var1);

   void executeAsync(InternalConnection var1, SingleResultCallback<T> var2);

   CommandProtocol<T> sessionContext(SessionContext var1);
}
