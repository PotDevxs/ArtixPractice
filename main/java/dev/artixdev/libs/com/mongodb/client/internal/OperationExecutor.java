package dev.artixdev.libs.com.mongodb.client.internal;

import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.WriteOperation;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public interface OperationExecutor {
   <T> T execute(ReadOperation<T> var1, ReadPreference var2, ReadConcern var3);

   <T> T execute(WriteOperation<T> var1, ReadConcern var2);

   <T> T execute(ReadOperation<T> var1, ReadPreference var2, ReadConcern var3, @Nullable ClientSession var4);

   <T> T execute(WriteOperation<T> var1, ReadConcern var2, @Nullable ClientSession var3);
}
