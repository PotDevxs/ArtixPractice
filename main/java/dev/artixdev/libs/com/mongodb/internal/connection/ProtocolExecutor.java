package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface ProtocolExecutor {
   @Nullable
   <T> T execute(CommandProtocol<T> var1, InternalConnection var2, SessionContext var3);

   <T> void executeAsync(CommandProtocol<T> var1, InternalConnection var2, SessionContext var3, SingleResultCallback<T> var4);
}
