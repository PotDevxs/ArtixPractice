package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

@ThreadSafe
public interface Server {
   Connection getConnection(OperationContext var1);

   void getConnectionAsync(OperationContext var1, SingleResultCallback<AsyncConnection> var2);

   int operationCount();
}
